package classPackage;

import servletPackage.JDBC;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;

public class action {

    public static void delete(String path, String type) {
        try {
            if (!type.equals("folder")) {
                new File(path).delete();
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + path + " deleted [file]");
            } else {
                FileUtils.deleteDirectory(new File(path));
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + path + " deleted [folder]");
            }

            //Delete data in database
            Connection con = JDBC.iniDB();
            PreparedStatement getID = con.prepareStatement("SELECT shareID FROM share WHERE PATH LIKE ?");
            String sqlPath = path.replace("\\", "\\\\\\"); //in linux there is not backslash "\"
            getID.setString(1, sqlPath + "%");
            ResultSet rs = getID.executeQuery();
            while (rs.next()) {
                String shareID = rs.getString("shareID");
                PreparedStatement ps = con.prepareStatement("DELETE FROM recipient WHERE shareID = ?");
                PreparedStatement ps1 = con.prepareStatement("DELETE FROM share WHERE shareID = ?");
                ps.setString(1, shareID);
                ps1.setString(1, shareID);
                int i = ps.executeUpdate();
                if (i > 0) {
                    int j = ps1.executeUpdate();
                    if (j < 1) {
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to delete data in share table");
                    }
                } else {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to delete data in recipient table");
                }
                ps.close();
                ps1.close();
            }
            con.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String rename(String oldPath, String newPath, String newName) {
        String success = null;
        File sourceFile = new File(oldPath);
        File destFile = new File(newPath);
        boolean nameValidation = true;
        if (!destFile.exists()) {
            if (sourceFile.isDirectory()) {
                String validation = "^[^\\\\/?%*:|\"<>\\.]+$";
                if (!newName.matches(validation)) {
                    nameValidation = false;
                }
            }else{
                if (newName.contains("/") || newName.contains("\\")) {
                    nameValidation = false;
                }
            }
            if (nameValidation) {
                if (sourceFile.renameTo(destFile)) {
                    success = "1";
                    //Update Database
                    try {
                        Connection con = JDBC.iniDB();
                        PreparedStatement getID = con.prepareStatement("SELECT shareID,path FROM share WHERE PATH LIKE ?");
                        String sqlPath = oldPath.replace("\\", "\\\\\\");
                        getID.setString(1, sqlPath + "%");
                        ResultSet rs = getID.executeQuery();
                        while (rs.next()) {
                            String dbPath = rs.getString("path");
                            String shareID = rs.getString("shareID");
                            String replacePath = dbPath.replace(oldPath, newPath);
                            PreparedStatement ps = con.prepareStatement("UPDATE share SET path = ? WHERE shareID = ?");
                            ps.setString(1, replacePath);
                            ps.setString(2, shareID);
                            int i = ps.executeUpdate();
                            if (i < 1) {
                                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to update new path in database");
                            }
                            ps.close();
                        }
                        con.close();
                    } catch (ClassNotFoundException | SQLException e) {
                        System.err.println(e.getMessage());
                    }
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Rename file " + oldPath + " into " + newName);
                }
            } else {
                success = "0";
            }
        } else {
            success = "-1";
        }
        return success;
    }

}
