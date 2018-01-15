package es.eurohelp.lod.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

import es.eurohelp.lod.utils.HttpManager;
import es.eurohelp.lod.utils.MIMEtype;

/**
 * This servlet resolves sparql calls
 * @author grozadilla
 * @author megana
 *
 */
public class SparqlServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5150094619651959338L;
	private static final Logger LOGGER = LogManager.getLogger(SparqlServlet.class);
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains(MIMEtype.HTML.mimetypevalue())){
				if(LOGGER.isDebugEnabled()){
				    LOGGER.debug("Load Yasgui component");
				}
				goToEndpoint(req, resp); 
			}else{
		        InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
		        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
				HttpManager.getInstance().redirectGetRequest(req, resp, keysValues.get("SPARQLendpoint"), null);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (req.getHeader("Accept").contains(MIMEtype.HTML.mimetypevalue())){	
				goToEndpoint(req, resp); 
			}else{
		        InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
		        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
				HttpManager.getInstance().redirectPostRequest(req,resp, keysValues.get("SPARQLendpoint"));
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}	
	}
	
	/**
	 * Redirects to Sparql endpoint page
	 * @param req the request
	 * @param resp the response
	 * @throws Exception exception
	 */
	private void goToEndpoint(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        InputStream in = FileUtils.getInstance().getInputStream("LinkedDataServerConfig.yml");
        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
		req.setAttribute("url", keysValues.get("SPARQLendpoint")); 
		getServletContext().getRequestDispatcher("/pages/endpoint.jsp").forward
           (req, resp); 
	}
}