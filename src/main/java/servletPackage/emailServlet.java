/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.securityUtils;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class emailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String email = request.getParameter("key1");
            String key = URLDecoder.decode(request.getParameter("key2"), "UTF-8").replaceAll("\\s","+");                
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("select * from `user` where email = ? AND verification = ?");
            ps.setString(1, email);
            ps.setInt(2, 0);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pwd = rs.getString("password");
                String secretKey = rs.getString("key");
                String decryptKey = securityUtils.decrypt(key, secretKey);
                if (pwd.equals(decryptKey)) {
                    PreparedStatement st = con.prepareStatement("update user set verification = 1 where email = ? AND verification = ?");
                    st.setString(1, email);
                    st.setInt(2, 0);
                    int i = st.executeUpdate();
                    if (i>0) {
                        //success
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " verification success");
                        response.sendRedirect("emailConfirmation.html");                         
                    }else{
                        //fail
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " verification failed");
                        response.sendRedirect("confirmationFail.html");                         
                    }
                }else{
                    //fail
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " verification failed [invalid activation link]");
                    response.sendRedirect("confirmationFail.html");                    
                }
            } else{
                //fail
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " verification failed [no data found in database]");
                response.sendRedirect("confirmationFail.html");  
            }
                        
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
            response.sendRedirect("confirmationFail.html");  
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
