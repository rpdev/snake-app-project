/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
public class LoginServlet extends HttpServlet {

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

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("login")) {
            UserAcc newUser = new UserAcc(request.getParameter("user_name"), request.getParameter("password"), "");
            List<UserAcc> userList = Database.getInstance().getEntityList(UserAcc.class);
            for (UserAcc user : userList) {
                if (user.equals(newUser)) {
                    request.getSession().setAttribute("user", user);
                    request.getRequestDispatcher("index.xhtml").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("register.xhtml");

        } else if (action.equals("logout")) {
            request.getSession().setAttribute("user", null);
            request.getRequestDispatcher("index.xhtml").forward(request, response);

        } else if (action.equals("register")) {
            UserAcc newUser = new UserAcc(request.getParameter("user_name"), request.getParameter("password"), "emalme");
            SnakeMap sm = new SnakeMap(newUser);
            Database.getInstance().mergeObject(newUser);
            Database.getInstance().mergeObject(sm);
            response.sendRedirect("index.xhtml");
        } else if (action.equals("load")) {
            PrintWriter out = response.getWriter();
            UserAcc user = (UserAcc) request.getSession().getAttribute("user");
            try {
                if (user == null) {
                    out.println("<p><form action=\"Login\" method=\"POST\">");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"login\"/>");
                    out.println("<table>");
                    out.println("<tr>"
                            + "<td colspan=\"2\">"
                            + "<input type=\"text\" name=\"user_name\" id=\"username\" placeholder=\"Username\" style=\"width: 140px;\" />"
                            + "</td>"
                            + "</tr>");
                    out.println("<tr>"
                            + "<td colspan=\"2\">"
                            + "<input type=\"password\" name=\"password\" id=\"password\" style=\"width: 140px;\" />"
                            + "</td>"
                            + "<td><input type=\"submit\" value=\"Login\" /></td>"
                            + "</tr>");
                    out.println("</table>"
                            + "</form>"
                            + "</p>");
                } else {
                    out.println("<p><form action=\"Login\" method=\"POST\">");
                    out.println("Välkommen: " + user.getUserName() + "<br />");
                    out.println("<input type=\"hidden\" name=\"action\" value=\"logout\"/>");
                    out.println("<input type=\"submit\" value=\"Logout\" />");
                    out.println("</form></p>");


                }

            } finally {
                out.close();
            }
        } else if (action.equals("getForm")) {
            response.setContentType("text/xml;charset=UTF-8");
            PrintWriter out = response.getWriter();
            UserAcc user = (UserAcc) request.getSession().getAttribute("user");
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}