package se.chalmers.snake.snakeappwebpage.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder.HttpMeta;
import se.chalmers.snake.snakeappwebpage.lib.HttpServletBuilder.HttpOutput;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database;
import se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap;
import se.chalmers.snake.snakeappwebpage.serverstorage.UserAcc;

/**
 *
 * @author Alesandro
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("action").equals("login")) {
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

        } else if (request.getParameter("action").equals("register")) {
            UserAcc newUser = new UserAcc(request.getParameter("user_name"), request.getParameter("password"), "emalme");
            SnakeMap sm = new SnakeMap(newUser);
            Database.getInstance().mergeObject(newUser);
            Database.getInstance().mergeObject(sm);
            response.sendRedirect("index.xhtml");
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
