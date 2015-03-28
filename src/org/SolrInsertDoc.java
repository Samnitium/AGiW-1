package org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.common.SolrInputDocument;

public class SolrInsertDoc {

	public static void insert(String url, String content) {
		String solr_url = "http://localhost:8983/solr/new_core";
		HttpSolrServer server = new HttpSolrServer( solr_url );
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc = new SolrInputDocument();
		// change schema.xml to add header field
		doc.addField("id", url);
		doc.addField("content", content);
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
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
	}

}
