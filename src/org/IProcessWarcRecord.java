package org;

/**
 * author: Mark Watson
 */

/**
 * callback interface for handling WARC record data
 */
public interface IProcessWarcRecord {
  public void process(String url, String content, String warc_data);
  public void done();  // called once when there is no more data to be processed
}
