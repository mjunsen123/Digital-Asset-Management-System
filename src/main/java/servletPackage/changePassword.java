/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.securityUtils;
import classPackage.user;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;

public class changePassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = "0";
            HttpSession session = request.getSession(false);
            user u = (user) session.getAttribute("user");
            String email = u.getEmail();
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("SELECT salt,password FROM user WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String salt = rs.getString("salt");
                String dbPwd = rs.getString("password");
                String oldPwd = securityUtils.hashing(request.getParameter("opwd") + salt);
                String newPwd = securityUtils.hashing(request.getParameter("npwd") + salt);
                if (oldPwd.equals(dbPwd)) {
                    ps = con.prepareStatement("UPDATE user SET password = ? WHERE email = ?");
                    ps.setString(1, newPwd);
                    ps.setString(2, email);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        status = "1";
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success change password for " + email);
                        u.setPassword(newPwd);
                    } else {
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to change password for " + email);
                    }
                }else{
                    status = "2";
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to change password for " + email);
                }

            }
            response.setContentType("text/plain");
            response.getWriter().write(status);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
