package es.eurohelp.lod.server;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;
import es.eurohelp.lod.utils.HttpManager;
import es.eurohelp.lod.utils.MIMEtype;
import es.eurohelp.lod.utils.Methodtype;

public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1031422249396784970L;

	private static final Logger LOGGER = LogManager.getLogger(ResourceServlet.class);

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// DESCRIBE
		// <http://donostia.eus/data/id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10>
		try {

			InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
			HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
			String base = keysValues.get("base");
			String triplestore = keysValues.get("triplestore");
			String accept = req.getHeader("Accept");
			String resourceURI = req.getRequestURI()
					.substring(req.getRequestURI().indexOf(req.getContextPath()) + req.getContextPath().length());
			String completeURI = base + resourceURI;
			String query = MessageFormat.format(keysValues.get("SPARQLRetrieveResourceQuery"), completeURI);
			String url = triplestore + "?query=" + URLEncoder.encode(query, "UTF-8");
			if (accept.contains(MIMEtype.HTML.mimetypevalue())) {
				accept = "application/rdf+xml";
				
//				POST http://localhost:9999/blazegraph/namespace/replicate/sparql?query=DESCRIBE+%3Chttp%3A%2F%2Fdonostia.eus%2Fdata%2Fid%2Fmedio-ambiente%2Fmedicion%2Furumea-txominenea-riesgo-2017-11-10-02-10%3E
//			    http://localhost:8080/ROOT/id/medio-ambiente/medicion/urumea-txominenea-riesgo-2017-11-10-02-10
				
//				HttpResponse response = HttpManager.getInstance().doGetRequest(req, null, url, accept);
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);
				httpget.setHeader("Accept", "application/rdf+xml");
				HttpResponse blzgResponse = httpclient.execute(httpget);

				Source             text        = new StreamSource(blzgResponse.getEntity().getContent());				
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				InputStream stream = classLoader.getResourceAsStream(keysValues.get("xslt"));
			    Source             xslt        = new StreamSource(stream);
			    
			    TransformerFactory factory     = TransformerFactory.newInstance();
			    Transformer        transformer = factory.newTransformer(xslt); 
			    
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    StreamResult result = new StreamResult(baos);
			    transformer.transform(text, result);
			    
			    resp.setContentType("text/html;charset=UTF-8");

				PrintWriter out = resp.getWriter();

				try {
					out.print(result.getOutputStream().toString());
				} finally {
					out.close(); 
				}
			    
			}
			else{
				// Throw 404 if RDF empty?
				HttpManager.getInstance().redirectGetRequest(req, resp, url, accept);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
