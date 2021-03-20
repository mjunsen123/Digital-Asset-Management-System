/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classPackage;

import static classPackage.digitalFiles.compareName;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import servletPackage.JDBC;

/**
 *
 * @author yee_j
 */
public class shareFiles extends digitalFiles {

    private String owner;
    private String recipient;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public static Comparator<shareFiles> compareName = new Comparator<shareFiles>() {
        @Override
        public int compare(shareFiles s1, shareFiles s2) {
            String shareFiles1 = s1.getName().toUpperCase();
            String shareFiles2 = s2.getName().toUpperCase();
            return shareFiles1.compareTo(shareFiles2);
        }
    };

    public static List<shareFiles> getShareFilesList(String recipient) throws IOException {
        List<shareFiles> fileList = new ArrayList<>();
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("SELECT s.owner,s.path FROM share s INNER JOIN recipient r ON s.shareID = r.shareID WHERE r.recipientEmail = ? AND r.status = 'A';");
            ps.setString(1, recipient);
            ResultSet rs = ps.executeQuery();
            SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            while (rs.next()) {
                String owner = rs.getString("owner");
                String path = rs.getString("path");
                File f = new File(path);
                shareFiles sf = new shareFiles();
                BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class);
                sf.setName(f.getName());
                sf.setOwner(owner);
                sf.setPath(path);
                sf.setType(digitalFiles.getExtention(f));
                if (!"folder".equals(sf.getType())) {
                    sf.setSize(digitalFiles.getSize(attr));
                }
                sf.setCdate(dfrmt.format(attr.creationTime().toMillis()));
                sf.setMdate(dfrmt.format(attr.lastModifiedTime().toMillis()));
                fileList.add(sf);
            }
            Collections.sort(fileList,compareName);
            fileList = new ArrayList<>(new LinkedHashSet<>(fileList));
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
        return fileList;
    }

    public static List<shareFiles> getShareFiles(String fpath, String owner) {
        List<shareFiles> newList = new ArrayList<>();
        try {
            File path = new File(fpath);
            File filesList[] = path.listFiles();
            SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            for (File f : filesList) {
                shareFiles sf = new shareFiles();
                BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class);
                sf.setName(f.getName());
                sf.setOwner(owner);
                sf.setPath(f.getAbsolutePath());
                sf.setType(digitalFiles.getExtention(f));
                if (!"folder".equals(sf.getType())) {
                    sf.setSize(digitalFiles.getSize(attr));
                }
                sf.setCdate(dfrmt.format(attr.creationTime().toMillis()));
                sf.setMdate(dfrmt.format(attr.lastModifiedTime().toMillis()));
                newList.add(sf);
            }
            Collections.sort(newList,compareName);
            newList = new ArrayList<>(new LinkedHashSet<>(newList));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return newList;
    }

    public static List<shareFiles> getShareStatus(String owner, String status) {
        List<shareFiles> fileList = new ArrayList<>();
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps;
            if (status.equals("pending")) {
                ps = con.prepareStatement("SELECT s.path, r.recipientEmail FROM share s INNER JOIN recipient r ON s.shareID = r.shareID WHERE s.owner = ? AND r.status = 'P'");
            } else {
                ps = con.prepareStatement("SELECT s.path, r.recipientEmail FROM share s INNER JOIN recipient r ON s.shareID = r.shareID WHERE s.owner = ? AND r.status = 'A'");
            }
            ps.setString(1, owner);
            ResultSet rs = ps.executeQuery();
            SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            while (rs.next()) {
                String path = rs.getString("path");
                String displayPath = path.substring(digitalFiles.masterPath().length() + owner.length(), path.length());
                File f = new File(path);
                shareFiles sf = new shareFiles();
                BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class);
                sf.setName(f.getName());
                sf.setOwner(owner);
                sf.setPath(displayPath);
                sf.setType(digitalFiles.getExtention(f));
                if (!"folder".equals(sf.getType())) {
                    sf.setSize(digitalFiles.getSize(attr));
                }
                sf.setCdate(dfrmt.format(attr.creationTime().toMillis()));
                sf.setMdate(dfrmt.format(attr.lastModifiedTime().toMillis()));
                sf.setRecipient(rs.getString("recipientEmail"));
                fileList.add(sf);
            }
            fileList = new ArrayList<>(new LinkedHashSet<>(fileList));
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.err.println(e.getMessage());
        }

        return fileList;
    }

    public static void approve(String path, String recipient, String owner) {
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("UPDATE recipient SET recipient.status = 'A' WHERE recipient.shareID = (SELECT shareID FROM share WHERE owner = ? and path = ?) AND recipient.recipientEmail = ?");
            ps.setString(1, owner);
            ps.setString(2, path);
            ps.setString(3, recipient);
            if (ps.executeUpdate() > 0) {
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success to approve " +recipient+ " to access " + path);
            }else{
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to approve " +recipient+ " to access " + path);
            }
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void reject(String path, String recipient, String owner) {
        try {
            Connection con = JDBC.iniDB();
            PreparedStatement ps = con.prepareStatement("DELETE FROM recipient WHERE recipient.shareID = (SELECT shareID FROM share WHERE owner = ? and path = ?) AND recipient.recipientEmail = ?");
            ps.setString(1, owner);
            ps.setString(2, path);
            ps.setString(3, recipient);
            if (ps.executeUpdate() > 0) {
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Success to delete " +recipient+ " for " + path);
            }else{
                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Failed to delete " +recipient+ " for " + path);
            }
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
