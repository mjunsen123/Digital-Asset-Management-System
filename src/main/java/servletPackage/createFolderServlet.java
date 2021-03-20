/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.digitalFiles;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yee_j
 */
public class createFolderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        createFolder(request, response);

    }

    void createFolder(HttpServletRequest request, HttpServletResponse response) {
        try {
            String error = "0";
            HttpSession session = request.getSession(false);
            String currentPath = digitalFiles.masterPath() + request.getParameter("currentPath");
            File checkFile = new File(currentPath);
            File filesList[] = checkFile.listFiles();
            String dir = currentPath + "/" + request.getParameter("folderName");

            for (File f : filesList) {
                if (f.getName().equals(request.getParameter("folderName"))) {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Folder create fail [Folder name exists]");
                    error = "2";
                    break;
                }
            }

            if (error.equals("0")) {
                File file = new File(dir);
                boolean bol = file.mkdir();
                if (!bol) {
                    error = "1";
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Folder create fail [Invalid folder name]");
                } else {
                    System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Folder create [" + dir + "]");
                }
                digitalFiles.getFiles(currentPath, session);
            }

            response.setContentType("text/plain");
            response.getWriter().write(error);

        } catch (IOException e) {
            e.getMessage();
        }
    }

}
