package org;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.SolrInputDocument;

public class SolrInsertText {

	public static void main(String[] args) {
		String url = "http://localhost:8983/solr/new_core";

		HttpSolrServer server = new HttpSolrServer( url );
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc = new SolrInputDocument();
		// change schema.xml to add header field
		doc.addField("header", "test");
		doc.addField("id", "1");
		docs.add(doc);

		try {
			server.add(docs);
			server.commit();
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
	}

}
