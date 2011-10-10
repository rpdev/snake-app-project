package se.chalmers.snake.snakeappwebpage.lib;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A API for use the HttpServlet more easy.
 */
public abstract class HttpServletBuilder extends HttpServlet {
	private static final long serialVersionUID = 6323638897225635090L;

	/**
	 * This Enum is define what type of requestScope the server is do.
	 */
	public static enum MethodType {
		DELETE, GET, HEAD, OPTIONS, POST, PUT, TRACE;
	}

	public class HttpMeta {
		private final MethodType method;
		private final HttpServletRequest request;
		private final HttpServletResponse response;
		private final HttpServletBuilderScope<Cookie> cookie;
		private final HttpServletBuilderScope<Object> requestObj;
		private final HttpServletBuilderScope<Object> session;

		private HttpMeta(MethodType method,HttpServletRequest request, HttpServletResponse response) {
			this.request = request;
			this.response = response;
			this.method = method;
			this.cookie = new HttpServletBuilderScope<Cookie>() {
				@Override
				public void set(String name, Cookie obj) {
					HttpMeta.this.response.addCookie(obj);
				}

				@Override
				public Cookie get(String name) {
					Cookie[] cookie = HttpMeta.this.request.getCookies();
					for (Cookie c : cookie) {
						if (c.getName().equals(name)) {
							return c;
						}
					}
					return null;
				}

				@Override
				public void remove(String name) {
					Cookie c = new Cookie(name, "");
					c.setMaxAge(0);
					HttpMeta.this.response.addCookie(c);
				}

				@Override
				public List<String> keys() {
					Cookie[] cc = HttpMeta.this.request.getCookies();
					ArrayList<String> ls = new ArrayList<String>(cc.length);
					for (Cookie c : cc) {
						ls.add(c.getName());
					}

					return ls;
				}
			};
			this.requestObj = new HttpServletBuilderScope<Object>() {

				@Override
				public void set(String name, Object obj) {
					HttpMeta.this.request.setAttribute(name, obj);
				}

				@Override
				public Object get(String name) {
					return HttpMeta.this.request.getAttribute(name);
				}

				@Override
				public void remove(String name) {
					HttpMeta.this.request.removeAttribute(name);
				}

				@Override
				public List<String> keys() {
					ArrayList<String> ls = new ArrayList<String>();
					Enumeration<String> it = HttpMeta.this.request.getAttributeNames();
					while (it.hasMoreElements()) {
						ls.add(it.nextElement());
					}
					return ls;
				}
			};
			this.session = new HttpServletBuilderScope<Object>() {

				@Override
				public void set(String name, Object obj) {
					HttpMeta.this.request.getSession().setAttribute(name, obj);
				}

				@Override
				public Object get(String name) {
					return HttpMeta.this.request.getSession().getAttribute(name);
				}

				@Override
				public void remove(String name) {
					HttpMeta.this.request.getSession().removeAttribute(name);
				}

				@Override
				public List<String> keys() {
					ArrayList<String> ls = new ArrayList<String>();
					Enumeration<String> it = HttpMeta.this.request.getSession().getAttributeNames();
					while (it.hasMoreElements()) {
						ls.add(it.nextElement());
					}
					return ls;
				}
			};
		}

		public MethodType getMethod() {
			return this.method;
		}
		
		/**
		 * Test if the Method of this request is REQUEST or POST
		 * @return 
		 */
		public boolean isMethodPostGet() {
			return this.method==MethodType.GET || this.method==MethodType.POST;
		}
		
		/**
		 * Get the row level of the HttpServletRequest
		 * @return 
		 */
		public HttpServletRequest getRowRequest() {
			return this.request;
		}

		/**
		 * Get the row level of the HttpServletResponse
		 * @return 
		 */
		public HttpServletResponse getRowResponse() {
			return this.response;
		}

		/**
		 * Get the request URI.
		 * @return 
		 */
		public String getURI() {

			return this.request.getRequestURI();
		}
		/**
		 * Get the request URL.
		 * @return 
		 */
		public String getURL() {
			return this.request.getRequestURL().toString();
		}

		/**
		 * Get a map of all variables from the URL after "?"
		 * @return 
		 */
		public Map<String, String[]> REQUEST() {
			return this.request.getParameterMap();
		}

		/**
		 * Get a variables from the URL after "?"
		 * @param label
		 * @return 
		 */
		public String REQUEST(String label) {
			return this.request.getParameter(label);
		}

		public void setContentType(String string) {
			this.response.setContentType(string);
		}

		public HttpServletBuilderScope<Cookie> cookieScope() {
			return this.cookie;
		}

		public HttpServletBuilderScope<Object> requestScope() {
			return this.requestObj;
		}

		public HttpServletBuilderScope<Object> sessionScope() {
			return this.session;
		}
	}

	public interface HttpServletBuilderScope<T> {
		/**
		 * Set a value to the select scope if the scope support this method.
		 * @param name
		 * @param obj 
		 */
		public void set(String name, T obj);

		/**
		 * Get a value from select scope if the scope support this method.
		 * @param name
		 * @return 
		 */
		public T get(String name);

		/**
		 * Remove a value from select scope if the scope support this method.
		 * @param name 
		 */
		public void remove(String name);

		/**
		 * Return a list of all keys in this scope if the scope support this method.
		 * @return 
		 */
		public List<String> keys();
	}

	public class HttpOutput {

		private HttpServletRequest request;
		private HttpServletResponse response;
		private PrintWriter printWriter;

		private HttpOutput(HttpServletRequest request, HttpServletResponse response,PrintWriter printWriter) {
			this.request = request;
			this.response = response;
			this.printWriter = printWriter;
		}

		/**
		 * Get the Writer Object for this page.
		 * @return
		 * @throws IOException 
		 */
		public PrintWriter getWriter() throws IOException {
			return this.printWriter;
		}

		/**
		 * Send a http status error code to the Web browser.
		 * <code>
		 * 200 Ok, ( Page is found and return ) 
		 * 404 Not Found, ( This page can not be found on the server )
		 * ...
		 * </code>
		 * @param httpError
		 * @return True if no error, else false.
		 */
		public boolean httpstatus(int httpError) {
			try {
				this.response.sendError(httpError);
				return true;
			} catch(Exception ex) {
				return false;
			}
		}

		/**
		 * Redirect the page for the user by send a redirect.
		 * This will do same as call:
		 * <code>this.response.sendRedirect(url);</code>
		 * @param url
		 * @return True if no error, else false.
		 */
		public boolean redirect(String url) {
			try {
				this.response.sendRedirect(url);
				return true;
			} catch(Exception ex) {
				return false;
			}
		}

		/**
		 * Forward a JSP file in this point.
		 * This will do same as call:
		 * <code>this.request.getRequestDispatcher(JSPFileName).forward(this.request, this.response);</code>
		 * @param JSPFileName
		 * @return True if no error, else false.
		 */
		public boolean forward(String JSPFileName) {
			try {
				this.request.getRequestDispatcher(JSPFileName).forward(this.request, this.response);
				return true;
			} catch(Exception ex) {
				return false;
			}
		}

		/**
		 * Include a JSP file in this point.
		 * This will do same as call:
		 * <code>this.request.getRequestDispatcher(JSPFileName).include(this.request, this.response);</code>
		 * @param JSPFileName
		 * @return True if no error, else false.
		 */
		public boolean include(String JSPFileName) {
			try {
				this.request.getRequestDispatcher(JSPFileName).include(this.request, this.response);
				return true;
			} catch(Exception ex) {
				return false;
			}
		}
	}

	
	/**
	 * Processes all HTTP requests, 
	 * use <code>httpMeta.isMethodPostGet()</code> for test if POST or GET Method.
	 * @param httpMeta The HTTP Meta data, controller for Session, Cookie, RequestScope...
	 * @param httpOutput The HTTP output unit, for return data to the users.
	 */
	protected abstract void pageRequest(
			  HttpMeta httpMeta,
			  HttpOutput httpOutput) throws Exception;

	//<editor-fold defaultstate="collapsed" desc="Override Methods">
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.GET, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.POST, request, response);
	}

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.HEAD, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.PUT, request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.DELETE, request, response);

	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.OPTIONS, request, response);
	}

	@Override
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doRequest(MethodType.TRACE, request, response);
	}
	//</editor-fold>

	/**
	 * The private method for prepress the call before handle to the pageRequest
	 * @param method
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void doRequest(MethodType method, HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		try {
			this.pageRequest(new HttpMeta(method,request, response), new HttpOutput(request, response,pw));
		} catch(Exception ex) {
		} finally {
			pw.close();
		}
	}
}