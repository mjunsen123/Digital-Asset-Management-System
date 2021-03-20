package servletPackage;

import classPackage.securityUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class resetPasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String success = "0";
            String email = request.getParameter("email");
            String key = request.getParameter("key");
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("select * from `user` where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pwd = rs.getString("password");
                String salt = rs.getString("salt");
                String secretKey = rs.getString("key");
                String decryptKey = securityUtils.decrypt(key, secretKey);
                String password = securityUtils.hashing(request.getParameter("pwd") + salt);
                String newKey = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                if (pwd.equals(decryptKey)) {
                    if (!pwd.equals(password)) {
                        //success
                        PreparedStatement st = con.prepareStatement("update user set `password` = ?, `key` = ? where email = ?");
                        st.setString(1, password);
                        st.setString(2, newKey);
                        st.setString(3, email);
                        int i = st.executeUpdate();
                        if (i > 0) {
                            success = "1";
                            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " password reset success");
                        }
                    } else {
                        success = "1";
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + email + " password reset success [Same password with database]");
                    }
                }
            }
            response.setContentType("text/plain");
            response.getWriter().write(success);
        } catch (IOException | ClassNotFoundException | SQLException e) {
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
