package org;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.response.*;

public class SolrConnectionTest {
	
	public static void main(String[] args) {
		 String url = "http://localhost:8983/solr/new_core";
		  /*
		    HttpSolrServer is thread-safe and if you are using the following constructor,
		    you *MUST* re-use the same instance for all requests.  If instances are created on
		    the fly, it can cause a connection leak. The recommended practice is to keep a
		    static instance of HttpSolrServer per solr server url and share it for all requests.
		    See https://issues.apache.org/jira/browse/SOLR-861 for more details
		  */
		  HttpSolrServer server = new HttpSolrServer( url );
		  SolrQuery solrQuery = new SolrQuery();
		  solrQuery.setQuery("*");
		  solrQuery.setStart(0);
		  solrQuery.setRows(20);

		  try {
			QueryResponse response = server.query(solrQuery);
			System.out.println(response.toString());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
