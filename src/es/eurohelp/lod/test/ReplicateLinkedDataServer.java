package es.eurohelp.lod.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;
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
	private static String resource = "id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10";
	private static String nonexistentresource = "id/trololo";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
		configKeysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
		host = configKeysValues.get("host");
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
	public void GETNonExistentResource() {
		try {
			HttpResponse response = HttpManager.getInstance().doSimpleGetRequest(host + nonexistentresource, MIMEtype.RDFXML.mimetypevalue());
			
			
			
			assertEquals(200, response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
}
