package servletPackage;

import classPackage.digitalFiles;
import classPackage.email;
import classPackage.securityUtils;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class registerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String error = "2";
            String usr = request.getParameter("email");
            String salt = Base64.getEncoder().encodeToString(securityUtils.genSalt(128));
            String pwd = securityUtils.hashing(request.getParameter("pwd") + salt);
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String key = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                       
            if (emailValid(usr)) {
                Connection con = JDBC.iniDB();
                PreparedStatement ps = con.prepareStatement("insert into `user` values (?,?,?,?,?,?,?)");
                ps.setString(1, usr);
                ps.setString(2, pwd);
                ps.setString(3, salt);
                ps.setString(4, fname);
                ps.setString(5, lname);
                ps.setInt(6, 0);
                ps.setString(7, key);

                int i = ps.executeUpdate();
                if (i > 0) {
                    error = "0";
                    File file = new File(digitalFiles.masterPath()+"/"+usr);
                    file.mkdir();
                    String encryptKey = URLEncoder.encode(securityUtils.encrypt(pwd, key), "UTF-8");
                    email.sendEmail(usr,encryptKey,fname,"verification");
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " registration success");
                } else {
                    error = "1";
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " registration failed");
                }
            } else {
                error = "2";
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " registration failed [email exists]");
            }
            response.setContentType("text/plain");
            response.getWriter().write(error);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("error");
            System.err.println(e.getMessage());
        }
    }

    boolean emailValid(String email) throws SQLException, ClassNotFoundException {
        Connection con = JDBC.iniDB();
        PreparedStatement ps = con.prepareStatement("select * from `user` where email=?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            rs.close();
            con.close();
            ps.close();
            return false;
        } else {
            rs.close();
            con.close();
            ps.close();
            return true;
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
