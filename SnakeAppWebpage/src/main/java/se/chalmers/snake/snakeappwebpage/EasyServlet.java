package se.chalmers.snake.snakeappwebpage;

import javax.servlet.annotation.WebServlet;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;

@WebServlet(name = "EasyServlet", urlPatterns = {"/EasyServlet"})
public class EasyServlet extends HttpServletBuilder {

	/**
	 * Processes all HTTP requests, 
	 * use <code>httpMeta.isMethodPostGet()</code> for test if POST or GET Method.
	 * @param httpMeta The HTTP Meta data, controller for Session, Cookie, RequestScope...
	 * @param httpOutput The HTTP output unit, for return data to the users.
	 */
	@Override
	protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {
		if (httpMeta.isMethodPostGet()) {
			httpMeta.setContentType("text/html;charset=UTF-8");
			httpOutput.forward("index.xhtml");

			
		}
	}
}
