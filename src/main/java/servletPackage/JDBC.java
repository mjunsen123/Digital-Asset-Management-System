
package servletPackage;

import java.sql.*;

public class JDBC {
    public static Connection iniDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://pavedb.crhm1pha7clu.ap-southeast-1.rds.amazonaws.com:3306/dams", "paveAdmin", "password4admin");
        return con;
    }
}
