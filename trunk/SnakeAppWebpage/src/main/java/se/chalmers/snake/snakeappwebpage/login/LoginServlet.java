/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.login;

import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder.HttpMeta;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder.HttpOutput;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alesandro
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServletBuilder {

	private static final long serialVersionUID = -2455964686168401537L;

	@XmlRootElement
	static class User {

		@XmlElement
		private String userName;
		@XmlElement
		private String userEmail;

		public User() {
		}

		public User(UserAcc user) {
			if (user != null) {
				this.userName = user.getUserName();
				this.userEmail = user.getEmail();
			}
		}
	}

	public static UserAcc getUserAccount(HttpMeta httpMeta) {
		try {
			Object obj = httpMeta.sessionScope().get("user");
			if(obj instanceof UserAcc) {
				return (UserAcc)obj;
			}
		} catch (Exception ex) {
		}
		return null;
	}

	@Override
	protected void pageRequest(HttpMeta httpMeta, HttpOutput httpOutput) throws Exception {
		if (httpMeta.isMethodPostGet()) {
			String action = httpMeta.REQUEST("action");
			if (action.equals("login")) {
				UserAcc newUser = new UserAcc(httpMeta.REQUEST("user_name"), httpMeta.REQUEST("password"), "");
				List<UserAcc> userList = Database.getInstance().getEntityList(UserAcc.class);
				for (UserAcc user : userList) {
					if (user.equals(newUser)) {

						httpMeta.sessionScope().set("user", user);
						httpOutput.forward("main.jsf");
						return;
					}
				}
				httpOutput.redirect("register.jsf");

			} else if (action.equals("logout")) {
				httpMeta.sessionScope().remove("user");
				httpOutput.forward("main.jsf");
			} else if (action.equals("register")) {
				UserAcc newUser = new UserAcc(httpMeta.REQUEST("user_name"), httpMeta.REQUEST("password"), "emalme");
				Database.getInstance().mergeObject(newUser);
				httpMeta.sessionScope().set("user", newUser);
				httpOutput.redirect("main.jsf");
			} else if (action.equals("getForm")) {
				httpMeta.setContentType("text/xml;charset=UTF-8");
				PrintWriter out = httpOutput.getWriter();
				UserAcc user = (UserAcc) httpMeta.sessionScope().get("user");
				User wrapper = new User(user);
				JAXBContext jc;
				try {
					jc = JAXBContext.newInstance(User.class);
					Marshaller m = jc.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
							  Boolean.TRUE);
					// Dump XML data
					m.marshal(wrapper, out);
				} catch (JAXBException ex) {
					Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					out.close();
				}
			}
		}
	}
}