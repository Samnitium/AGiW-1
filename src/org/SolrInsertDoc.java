package org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.common.SolrInputDocument;

public class SolrInsertDoc {

	//public static void insert(String url, String content, String warc_data, String title, List<String> images) {
	public static void insert(String url, String content, String warc_data, String title) {
		String solr_url = "http://localhost:8983/solr/new_core";
		HttpSolrServer server = new HttpSolrServer( solr_url );
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc = new SolrInputDocument();
		// change schema.xml to add header field
		doc.addField("id", url);
		doc.addField("content", content);
		//doc.addField("warc_data", "2009-03-65T08:43:19Z");
		doc.addField("warc_data", warc_data);
		doc.addField("title", title);
		
		//add an image field
		//doc.addField("images", images);
		
		docs.add(doc);

		try {
			server.add(docs);
			//server.commit();
			//server.optimize();
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
	}
	public static void commit_optimize() {
		String solr_url = "http://localhost:8983/solr/new_core";

		HttpSolrServer server = new HttpSolrServer( solr_url );

		try {
		//	server.add(docs);
			server.commit();
			server.optimize();
			System.out.println("FATTOOOOOOO");
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
	}

}
