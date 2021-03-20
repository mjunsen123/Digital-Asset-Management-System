/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.shareFiles;
import classPackage.user;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yee_j
 */
public class shareFilesServlet extends HttpServlet {

    protected void getFiles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession(false);
            user u = (user) session.getAttribute("user");
            String email = u.getEmail();
            List<shareFiles> fileList = shareFiles.getShareFilesList(email);
            List<shareFiles> pendingList = shareFiles.getShareStatus(email, "pending");
            List<shareFiles> approvedList = shareFiles.getShareStatus(email, "approved");

            session.setAttribute("shareList", fileList);
            session.setAttribute("pendingList", pendingList);
            session.setAttribute("approvedList", approvedList);
            session.setAttribute("currentSharePath", null);
            session.setAttribute("parentFile", null);
            session.setAttribute("displayPath", null);
            session.setAttribute("metadataArray", null);
            response.sendRedirect("shareWithMe.jsp");

        } catch (IOException e) {
            System.err.println(e.getMessage());
            response.sendRedirect("login.jsp");
        }

    }

    protected void navigate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int row = Integer.parseInt(request.getParameter("selectedRow"));
        HttpSession session = request.getSession(false);
        List<shareFiles> shareList = (List<shareFiles>) session.getAttribute("shareList");
        String path = shareList.get(row).getPath();
        String owner = shareList.get(row).getOwner();
        List<shareFiles> newList = shareFiles.getShareFiles(path, owner);
        shareFiles sf = shareList.get(row);
        //If current share path is null, means that user havent navigate folder in share page before
        //Then this navigate process will be the first navigate process
        //Record down the parentpath for for back purpose
        if (session.getAttribute("currentSharePath") == null) {
            session.setAttribute("parentFile", sf);
        }
        //get the current path by hiding the master path
        shareFiles parentFile = (shareFiles) session.getAttribute("parentFile");
        int i = parentFile.getPath().lastIndexOf('/');
        int l = path.length();
        String displayPath = path.substring(i, l);
        session.setAttribute("displayPath", displayPath);
        session.setAttribute("shareList", newList);
        session.setAttribute("currentSharePath", path);
    }

    protected void back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        user u = (user) session.getAttribute("user");
        String email = u.getEmail();
        String currentPath = (String) session.getAttribute("currentSharePath");
        if (currentPath != null) {
            shareFiles parentFile = (shareFiles) session.getAttribute("parentFile");
            String parentPath = parentFile.getPath();
            String owner = parentFile.getOwner();
            int i = currentPath.lastIndexOf('/');
            String temp = currentPath.substring(0, i);
            if (currentPath.equals(parentPath)) {
                List<shareFiles> fileList = shareFiles.getShareFilesList(email);
                session.setAttribute("shareList", fileList);
                session.setAttribute("currentSharePath", null);
                session.setAttribute("parentFile", null);
                session.setAttribute("displayPath", null);
            } else {
                List<shareFiles> newList = shareFiles.getShareFiles(temp, owner);
                i = parentPath.lastIndexOf('/');
                int l = temp.length();
                String displayPath = temp.substring(i, l);
                session.setAttribute("displayPath", displayPath);
                session.setAttribute("shareList", newList);
                session.setAttribute("currentSharePath", temp);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getFiles(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("selectedRow") != null) {
            navigate(request, response);
        } else if (request.getParameter("action").equals("back")) {
            back(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
