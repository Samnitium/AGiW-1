package org;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import edu.cmu.lemurproject.WarcHTMLResponseRecord;
import edu.cmu.lemurproject.WarcRecord;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * author: Mark Watson
 */
public class ReadS3Bucket {
  static public void process(AmazonS3 s3, String bucketName, String prefix, int max) {
    int count = 0;

    // use a callback class for handling WARC record data:
    IProcessWarcRecord processor = new SampleProcessWarcRecord();

    ObjectListing list = s3.listObjects(bucketName, prefix);

    do {  // reading summaries code derived from stackoverflow example posted by Alberto A. Medina:

      List<S3ObjectSummary> summaries = list.getObjectSummaries();
      for (S3ObjectSummary summary : summaries) {
        try {
          String key = summary.getKey();
          //System.out.println("+ key: " + key);
          S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
          InputStream objectData = object.getObjectContent();
          GZIPInputStream gzInputStream=new GZIPInputStream(objectData);
          DataInputStream inStream = new DataInputStream(gzInputStream);

          WarcRecord thisWarcRecord;
          while ((thisWarcRecord = WarcRecord.readNextWarcRecord(inStream)) != null) {
        	  
            //System.out.println("-- thisWarcRecord.getHeaderRecordType() = " + thisWarcRecord.getHeaderRecordType());
            if (thisWarcRecord.getHeaderRecordType().equals("response")) {
              WarcHTMLResponseRecord htmlRecord = new WarcHTMLResponseRecord(thisWarcRecord);
              String thisTargetURI = htmlRecord.getTargetURI();
              String thisContentUtf8 = htmlRecord.getRawRecord().getContentUTF8();
              String warc_data = htmlRecord.getRawRecord().getHeaderMetadataItem("WARC-Date").substring(0, 19).concat("Z");
              System.out.println("Data= "+warc_data);
              // handle WARC record content:
              processor.process(thisTargetURI, thisContentUtf8, warc_data);
            }
          }
          inStream.close();
        } catch (Exception ex) {
          //ex.printStackTrace();
        }
        if (++count >= max) return;
      }
      list = s3.listNextBatchOfObjects(list);
    } while (list.isTruncated());
    // done processing all WARC records:
    processor.done();

  }

  static public void main(String[] args) {
    AmazonS3Client s3 = new AmazonS3Client();
    //process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00000-ip-10-180-136-8.ec2.internal.warc.gz", 20);
   /* ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00001-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00002-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00003-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00004-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00005-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00006-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00007-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00008-ip-10-180-136-8.ec2.internal.warc.gz", 20);
    ReadS3Bucket.process(s3, "aws-publicdatasets", "common-crawl/crawl-data/CC-MAIN-2014-35/segments/1408500800168.29/warc/CC-MAIN-20140820021320-00009-ip-10-180-136-8.ec2.internal.warc.gz", 20);*/
  }
}
