// based on an example from http://boston.lti.cs.cmu.edu/clueweb09/wiki/tiki-index.php?page=Working+with+WARC+Files

package org;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import edu.cmu.lemurproject.WarcRecord;
import edu.cmu.lemurproject.WarcHTMLResponseRecord;

public class ReadWARC {

  public static void main(String[] args) throws IOException {

    // use a callback class for handling WARC record data:
    IProcessWarcRecord processor = new SampleProcessWarcRecord();

    String inputWarcFile="/00.warc.gz";
    GZIPInputStream gzInputStream=new GZIPInputStream(new FileInputStream(inputWarcFile));
    DataInputStream inStream=new DataInputStream(gzInputStream);

    WarcRecord thisWarcRecord;
   while ((thisWarcRecord=WarcRecord.readNextWarcRecord(inStream))!=null) {
      System.out.println("%% thisWarcRecord.getHeaderRecordType() = " + thisWarcRecord.getHeaderRecordType());
      if (thisWarcRecord.getHeaderRecordType().equals("response")) {
        WarcHTMLResponseRecord htmlRecord=new WarcHTMLResponseRecord(thisWarcRecord);
        String thisTargetURI=htmlRecord.getTargetURI();
        String thisContentUtf8 = htmlRecord.getRawRecord().getContentUTF8();
        String warc_data = htmlRecord.getRawRecord().getHeaderMetadataItem("WARC-Date").substring(0, 19).concat("Z");
        System.out.println("Data= "+warc_data);
        // handle WARC record content:
        processor.process(thisTargetURI, thisContentUtf8, warc_data);
      }
    }
    inStream.close();
    // done processing all WARC records:
    processor.done();
  }
}
