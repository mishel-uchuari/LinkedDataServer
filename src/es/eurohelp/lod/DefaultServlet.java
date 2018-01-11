package es.eurohelp.lod;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * This servlet resolves sparql calls
 * @author grozadilla
 *
 */
public class DefaultServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1031422249396784970L;
	
	
	private static final Logger LOGGER = LogManager.getLogger(DefaultServlet.class);
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		try {
			goToWeb(req, resp); 
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	
	
	/**
	 * Redirects to initial page
	 * @param req the request
	 * @param resp the response
	 * @throws Exception exception
	 */
	private void goToWeb(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        InputStream in = FileUtils.getInstance().getInputStream("configuration.yml");
        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
		getServletContext().getRequestDispatcher(keysValues.get("defaultpage")).forward
           (req, resp); 
	}
	
}

