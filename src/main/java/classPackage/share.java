package classPackage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import servletPackage.JDBC;

public class share{

    public static boolean share(String[] recipient, String path, String owner) {
        //check share exists
        //if exist get share id and add recepient
        //if no exists add new share row and add recepient 
        boolean result = true;
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM share WHERE path = ? AND owner = ?");
            ps.setString(1, path);
            ps.setString(2, owner);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //Share ID found in share table, hence only update or insert in recipient table.
                String shareID = rs.getString("shareID");
                //check is recepient exists in table
                for (String rec : recipient) {
                    ps = con.prepareStatement("SELECT * FROM recipient WHERE shareID = ? AND recipientEmail = ?");
                    ps.setString(1, shareID);
                    ps.setString(2, rec);
                    ResultSet rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        if (!rs1.getString("status").equals("A")) {
                            //update status to A since user need share to recipient
                            PreparedStatement ps1 = con.prepareStatement("update recipient set status = 'A' where shareID = ? AND recipientEmail = ?");
                            ps1.setString(1, shareID);
                            ps1.setString(2, rec);
                            int i = ps1.executeUpdate();
                            if (i > 0) {
                                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success update sharing status for [" + shareID + "]");
                            } else {
                                result = false;
                                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Fail to update sharing status for [" + shareID + "]");
                            }
                        } else {
                            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION The recepient [" + rec + "] already have the sharing access");
                        }
                    } else {
                        if (!insertRecipients(shareID, rec, "A")) {
                            result = false;
                        }
                    }
                }

            } else {
                //Record not found, generate new share ID
                String shareID = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                String key = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                LocalDateTime getTime = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String time = getTime.format(format);
                ps = con.prepareStatement("insert into share values (?,?,?,?,?)");
                ps.setString(1, shareID);
                ps.setString(2, owner);
                ps.setString(3, key);
                ps.setString(4, path);
                ps.setString(5, time);
                int i = ps.executeUpdate();
                if (i > 0) {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success insert new share path [" + path + "]");
                    //After insert new share data, now add recepients
                    for (String rec : recipient) {
                        if (!insertRecipients(shareID, rec, "A")) {
                            result = false;
                        }
                    }
                } else {
                    result = false;
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Fail to insert new share path [" + path + "]");
                }
            }
            con.close();
            ps.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }

    public static String share(String path, String owner) {
        String link = "";
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM share WHERE path = ? AND owner = ?");
            ps.setString(1, path);
            ps.setString(2, owner);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //record exists
                String shareID = rs.getString("shareID");
                String key = rs.getString("shareKey");
                String encryptKey = URLEncoder.encode(securityUtils.encrypt(path, key), "UTF-8");
                link = "http://pave.freeddns.org/shareLinkServlet?key1=" + URLEncoder.encode(shareID, "UTF-8") + "&key2=" + encryptKey;
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Using existing share link [" + link + "]");
            } else {
                //record no exists, add new share record
                String shareID = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                String key = Base64.getEncoder().encodeToString(securityUtils.genSalt(32));
                LocalDateTime getTime = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String time = getTime.format(format);
                ps = con.prepareStatement("insert into share values (?,?,?,?,?)");
                ps.setString(1, shareID);
                ps.setString(2, owner);
                ps.setString(3, key);
                ps.setString(4, path);
                ps.setString(5, time);
                int i = ps.executeUpdate();
                if (i > 0) {
                    String encryptKey = URLEncoder.encode(securityUtils.encrypt(path, key), "UTF-8");
                    link = "http://pave.freeddns.org/shareLinkServlet?key1=" + URLEncoder.encode(shareID, "UTF-8") + "&key2=" + encryptKey;
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success to generate new share link [" + link + "]");
                } else {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Fail to generate new share link for [" + path + "]");
                }
            }
            con.close();
            ps.close();
        } catch (UnsupportedEncodingException | ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return link;
    }

    public static boolean checkLink(String shareID, String key) {
        Boolean valid = false;
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM share WHERE shareID = ?");
            ps.setString(1, shareID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String shareKey = rs.getString("shareKey");
                String path = rs.getString("path");
                String decrypt = securityUtils.decrypt(key, shareKey);
                if (path.equals(decrypt)) {
                    valid = true;
                }
            }
            con.close();
            ps.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return valid;
    }

    public static boolean insertRecipients(String shareID, String recipient, String status) {
        boolean success = false;
        try {
            Connection con = JDBC.iniDB();
            //before insert make sure recipient data is not exists
            PreparedStatement ps = con.prepareStatement("SELECT * FROM recipient WHERE shareID = ? AND recipientEmail = ?");
            ps.setString(1, shareID);
            ps.setString(2, recipient);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {

                //make sure recipient == owner
                ps = con.prepareStatement("SELECT owner FROM share WHERE shareID = ?");
                ps.setString(1, shareID);
                rs = ps.executeQuery();
                rs.next();
                String owner = rs.getString("owner");

                if (!owner.equals(recipient)) {
                    ps = con.prepareStatement("insert into recipient values (?,?,?)");
                    ps.setString(1, shareID);
                    ps.setString(2, recipient);
                    ps.setString(3, status);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        success = true;
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success insert recepient [" + recipient + "] for [" + shareID + "]");
                    } else {
                        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Fail to insert recepient data for [" + shareID + "]");
                    }
                } else {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Fail to insert recepient [" + recipient + "] (Owner and recipient cannot be same) ");
                }

            } else {
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Recepient [" + recipient + "] already exists in [" + shareID + "]");
                success = true;
            }
            con.close();
            ps.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return success;
    }

}
