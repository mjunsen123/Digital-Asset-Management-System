package servletPackage;

import classPackage.digitalFiles;
import static classPackage.digitalFiles.compareName;
import classPackage.user;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class dashboardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {

            //Check whether user session exists
            HttpSession session = request.getSession(false);
            user u = (user) session.getAttribute("user");
            String email = u.getEmail();
            List<digitalFiles> list = new ArrayList<>();
            String folderpath = digitalFiles.masterPath() + email;
            String currentPath = folderpath.substring(digitalFiles.masterPath().length(), folderpath.length());

            File path = new File(folderpath);
            File filesList[] = path.listFiles();

            SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            for (File f : filesList) {
                digitalFiles df = new digitalFiles();
                BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class);
                df.setName(f.getName());
                df.setPath(f.getAbsolutePath());
                df.setType(digitalFiles.getExtention(f));
                if (!"folder".equals(df.getType())) {
                    df.setSize(digitalFiles.getSize(attr));
                }
                df.setCdate(dfrmt.format(attr.creationTime().toMillis()));
                df.setMdate(dfrmt.format(attr.lastModifiedTime().toMillis()));
                list.add(df);
            }
            Collections.sort(list, digitalFiles.compareName);
            list = new ArrayList<>(new LinkedHashSet<>(list));
            session.setAttribute("metadataArray", null);
            session.setAttribute("previewPath", null);
            session.setAttribute("fileList", list);
            session.setAttribute("cpath", currentPath);
            response.sendRedirect("dashboard.jsp");

        } catch (IOException e) {
            System.err.println(e.getMessage());
            //If session doesn't exists, null exception thrown, return to login page
            response.sendRedirect("login.jsp");
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
