package es.eurohelp.opendata;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * LinkedDataServer Prototype, partly inspired by
 * https://github.com/clarkparsia/sparql-proxy/blob/master/src/com/clarkparsia/
 * sparql/SparqlEndpointProxy.java
 * 
 * @author Mikel Egaña Aranguren
 * 
 */

//@WebServlet({ "/*", "/resource/*", "/class/*", "/property/*", "/value/*" })
public class LinkedDataServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LinkedDataServer() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO: map external and internal URIs
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		response.getWriter().println(request.getHeader("Accept"));
//		response.getWriter().println(request.getRequestURI());
//		response.getWriter().println(request.getRequestURL());

		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

		// Write the response message, in an HTML page
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<title>Linked Data Server</title></head>");
			out.println("<body>");
			out.println("<h1>Linked Data Server</h1>"); // says Hello
			// Echo client's request information
			out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
			out.println("<p>Request URL: " + request.getRequestURL() + "</p>");
			out.println("<p>Protocol: " + request.getProtocol() + "</p>");
			out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
			out.println("<p>Context Path: " + request.getContextPath() + "</p>");
			out.println("<p>Accept header: " + request.getHeader("Accept") + "</p>");
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close(); // Always close the output writer
		}
		
		
		

		String query = "DESCRIBE <http://opendata.euskadi.eus/AV-GASTEIZ/2016-02-06/PM10-AirQuality>";
		String endpoint = "http://ckan.eurohelp.es:7200/repositories/opendata-euskadi?query=";
		String complete_query = endpoint + URLEncoder.encode(query);

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(complete_query);

		Enumeration<String> req_headers = request.getHeaderNames();
		while (req_headers.hasMoreElements()) {
			String header_name = req_headers.nextElement();
			String header_value = request.getHeader(header_name);
			httpGet.setHeader(header_name, header_value);
		}

		// TODO: redirect server response to response
		CloseableHttpResponse triple_store_response = httpclient.execute(httpGet);
		HttpEntity entity = triple_store_response.getEntity();
		String entity_string = EntityUtils.toString(entity);
		Iterator<?> i = triple_store_response.headerIterator();
		while (i.hasNext()) {
			System.out.println(i.next());
		}

		// TODO: render in proper HTML if no accept header or accept header
		// something like application/x-ms-application, image/jpeg,
		// application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap,
		// application/vnd.ms-excel, application/vnd.ms-powerpoint,
		// application/msword, */*
		System.out.println(entity_string);
		triple_store_response.close();
	}
}
