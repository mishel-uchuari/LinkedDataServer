package es.eurohelp.lod.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

import es.eurohelp.lod.utils.HttpManager;

import es.eurohelp.lod.utils.MIMEtype;


public class ReplicateLinkedDataServer {
	
	private static final Logger LOGGER = LogManager.getLogger(ReplicateLinkedDataServer.class);
	
	private static HashMap<String, String> configKeysValues = null;	
	private static String host = null;
	private static String hostsparql = null;
	private static String queryURLParam = "?query=";
	private static String resource = "id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10";
	private static String nonexistentresource = "id/trololo";
	private static String sparqlSELECT = "SELECT * WHERE { ?sub ?pred ?obj } LIMIT 3 ";
	private static String sparqlASK = "ASK { ?s ?p <http://dbpedia.org/resource/Bilbao> }";
	private static String sparqlDESCRIBE = "DESCRIBE <http://dbpedia.org/resource/Bilbao>";
	private static String sparqlCONSTRUCT = "CONSTRUCT { <http://donostia.eus/data/id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10> rdfs:comment ?label } WHERE { <http://donostia.eus/data/id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10> rdfs:label ?label }";
	private static String sparqlDELETE = "DELETE { ?s rdfs:label ?label  } WHERE { ?s rdfs:label ?label }";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
		configKeysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
		host = configKeysValues.get("host");
		hostsparql = host + configKeysValues.get("SPARQLendpoint");
//		hostsparql = "http://localhost:9999/blazegraph/namespace/replicate/sparql";
	}

	@Test
	public void GETResourceRDFXML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + resource, MIMEtype.RDFXML.mimetypevalue());
			assertEquals(200, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETResourceJSONLD() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + resource, MIMEtype.JSONLD.mimetypevalue());
			assertEquals(200, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETResourceTURTLE() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + resource, MIMEtype.Turtle.mimetypevalue());
			assertEquals(200, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETNonExistentResourceRDFXML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + nonexistentresource, MIMEtype.RDFXML.mimetypevalue());
			assertEquals(404, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void GETNonExistentResourceHTML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + nonexistentresource, MIMEtype.HTML.mimetypevalue());
			assertEquals(404, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETSPARQLSELECTSPARQLXML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(hostsparql + queryURLParam + URLEncoder.encode(sparqlSELECT, "UTF-8"), MIMEtype.SPARQLXMLResultsFormat.mimetypevalue());
			assertTrue(EntityUtils.toString(response.getEntity()).contains("<literal>Ez dago arriskurik"));			
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	
	@Test
	public void POSTSPARQLSELECTSPARQLXML() {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("query", sparqlSELECT));
		    
		    List<BasicHeader> headers = new ArrayList<BasicHeader>();
		    headers.add(new BasicHeader("Accept", MIMEtype.SPARQLXMLResultsFormat.mimetypevalue()));
		    headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
		    
			HttpResponse response = HttpManager.getInstance().doPostRequest(hostsparql, headers, params);
			assertTrue(EntityUtils.toString(response.getEntity()).contains("<literal>Ez dago arriskurik"));			
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
		
	@Test
	public void GETSPARQLASKSPARQLXML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(hostsparql + queryURLParam + URLEncoder.encode(sparqlASK, "UTF-8"), MIMEtype.SPARQLXMLResultsFormat.mimetypevalue());
			assertTrue(EntityUtils.toString(response.getEntity()).contains("<boolean>true"));			
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETSPARQLDESCRIBERDFXML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(hostsparql + queryURLParam + URLEncoder.encode(sparqlDESCRIBE, "UTF-8"), MIMEtype.RDFXML.mimetypevalue());
			assertTrue(EntityUtils.toString(response.getEntity()).contains("<rdf:Description rdf:about=\"http://donostia.eus/data/id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10\">"));			
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void GETSPARQLDELETE() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(hostsparql + queryURLParam + URLEncoder.encode(sparqlDELETE, "UTF-8"));
			assertTrue(EntityUtils.toString(response.getEntity()).contains("org.openrdf.query.MalformedQueryException"));
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void GETSPARQLSELECTSPARQLHTML() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(hostsparql + queryURLParam + URLEncoder.encode(sparqlSELECT, "UTF-8"), MIMEtype.HTML.mimetypevalue());
			LOGGER.info(hostsparql + queryURLParam + URLEncoder.encode(sparqlSELECT, "UTF-8"));
			assertTrue(EntityUtils.toString(response.getEntity()).contains("<html>"));			
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
}
