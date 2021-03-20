package servletPackage;

import classPackage.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String success = "0";
            String usr = request.getParameter("email");
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("select * from `user` where email = ?");
            ps.setString(1, usr);
            ResultSet rs = ps.executeQuery();
            HttpSession session = request.getSession(true);

            if (rs.next()) {
                String dpwd = rs.getString("password");
                String salt = rs.getString("salt");
                String pwd_salt = securityUtils.hashing(request.getParameter("pwd") + salt);
                String fname = rs.getString("firstName");
                String lname = rs.getString("lastName");
                int verify = rs.getInt("verification");

                if (dpwd.equals(pwd_salt)) {
                    //login success
                    if (verify == 1) {
                        user u = new user();
                        u.setEmail(usr);
                        u.setPassword(pwd_salt);
                        u.setFirstName(fname);
                        u.setLastName(lname);
                        success = "2";
                        session.setAttribute("user", u);
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " login successfully");
                    } else if (verify == 0){
                        success = "1";
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " email not verified");
                    }
                } else {
                    success = "0";
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " login failed");
                }
            } else {
                success = "0";
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + usr + " login failed");
            }

            response.setContentType("text/plain");
            response.getWriter().write(success);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("error");
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
