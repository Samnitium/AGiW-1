package org;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * author: Mark Watson
 */

/**
 * a sample callback class for handling WARC record data by implementing IProcessWarcRecord interface
 */
public class SampleProcessWarcRecord implements IProcessWarcRecord {
  @Override
  public void process(String url, String content) {
	Document doc = Jsoup.parse(content);
;
	// Clean the document.
	doc = new Cleaner(Whitelist.none()).clean(doc);

	// Adjust escape mode
	doc.outputSettings().escapeMode(EscapeMode.xhtml);

	// Get back the string of the body.
	String words = doc.body().html();
	
    System.out.println("url: " + url);
	System.out.println(words);
	System.out.println();
    //System.out.println("content: " + url + "\n\n" + content + "\n");
  }

  @Override
  public void done() {
    // place any code hear to save data, etc.
  }
}
