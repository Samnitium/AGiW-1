package org;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.response.*;

public class SolrConnectionTest {

	public static void main(String[] args) {
		String url = "http://localhost:8983/solr/new_core";

		HttpSolrServer server = new HttpSolrServer( url );
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("*");
		solrQuery.setStart(0);
		solrQuery.setRows(20);

		try {
			QueryResponse response = server.query(solrQuery);
			System.out.println(response.toString());
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

}
