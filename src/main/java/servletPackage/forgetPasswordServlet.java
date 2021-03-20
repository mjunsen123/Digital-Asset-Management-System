package servletPackage;

import classPackage.email;
import classPackage.securityUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class forgetPasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userEmail = request.getParameter("resetEmail");
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("select * from `user` where email = ?");
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pwd = rs.getString("password");
                String fname = rs.getString("firstName");
                String key = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                String encryptKey = URLEncoder.encode(securityUtils.encrypt(pwd, key), "UTF-8");
                PreparedStatement psUpdate = con.prepareStatement("update `user` set `key` = ? where `email` = ?");
                psUpdate.setString(1, key);
                psUpdate.setString(2, userEmail);
                psUpdate.executeUpdate();               
                email.sendEmail(userEmail,encryptKey,fname,"reset");
            }else{
                email.sendEmail(userEmail,null,null,"reset");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
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
