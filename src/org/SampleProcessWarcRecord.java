package org;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * author: Mark Watson
 */

/**
 * a sample callback class for handling WARC record data by implementing IProcessWarcRecord interface
 */
public class SampleProcessWarcRecord implements IProcessWarcRecord {
  @Override
  public void process(String url, String content, String warc_data) {
	 //String body = content.substring(content.indexOf("<body>") + 6, content.indexOf("</body"));
	  String body = content.substring(content.indexOf("<") + 1);
	  Document doc = Jsoup.parse(body);
	  String title = doc.title();
	  
	 //estrazione immagini
	  /*
      Elements media = doc.select("[src]");
      List<String> images = null;
      for (Element src : media) {
          if (src.tagName().equals("img"))
              images.add(src.attr("abs:src"));
      }
      */
      
      
	// Clean the document.
	doc = new Cleaner(Whitelist.none()).clean(doc);

	// Adjust escape mode
	doc.outputSettings().escapeMode(EscapeMode.xhtml);

	// Get back the string of the body.
	String words = doc.body().html();
	
	
    System.out.println("url: " + url);
	System.out.println(words);
	System.out.println();
	SolrInsertDoc.insert(url, words, warc_data, title);
	//SolrInsertDoc.insert(url, words, warc_data, title, images);
    //System.out.println("content: " + url + "\n\n" + content + "\n");
  }

  @Override
  public void done() {
	  SolrInsertDoc.commit_optimize();
	  System.out.println("FATTO");
    // place any code hear to save data, etc.
  }
}
