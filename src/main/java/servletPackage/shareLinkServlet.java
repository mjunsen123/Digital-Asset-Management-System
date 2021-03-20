/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.share;
import static classPackage.share.insertRecipients;
import classPackage.user;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yee_j
 */
public class shareLinkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String email = request.getParameter("email");
        String shareID = URLDecoder.decode(request.getParameter("key1"), "UTF-8").replaceAll("\\s", "+");
        String key = URLDecoder.decode(request.getParameter("key2"), "UTF-8").replaceAll("\\s", "+");
        String success = "0";
        if (email == null) {
            user u = (user)session.getAttribute("user");
            if (u == null) {
                if (share.checkLink(shareID, key)) {
                    response.sendRedirect("http://pave.freeddns.org/shareLink.jsp?key1=" + request.getParameter("key1") + "&key2=" + request.getParameter("key2"));
                } else {
                    response.sendRedirect("confirmationFail.html");
                }
            } else {
                email = u.getEmail();
                if (share.checkLink(shareID, key)) {
                    if (insertRecipients(shareID, email, "P")) {
                        response.sendRedirect("fileRequestSuccess.html");
                    } else {
                        response.sendRedirect("confirmationFail.html");
                    }
                } else {
                    response.sendRedirect("confirmationFail.html");
                }
            }
        } else {
            if (share.checkLink(shareID, key)) {
                if (insertRecipients(shareID, email, "P")) {
                    response.sendRedirect("fileRequestSuccess.html");
                } else {
                    response.sendRedirect("confirmationFail.html");
                }
            } else {
                response.sendRedirect("confirmationFail.html");
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
