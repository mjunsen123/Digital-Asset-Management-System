/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.digitalFiles;
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
public class shareAction extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        user u = (user) session.getAttribute("user");
        String email = u.getEmail();
        String masterPath = digitalFiles.masterPath()+email;
        String action = request.getParameter("action");
        int row = Integer.parseInt(request.getParameter("row"));
        switch(action){
            case "approve":
                List<shareFiles> pendingList = (List<shareFiles>) session.getAttribute("pendingList");
                String path = masterPath+pendingList.get(row).getPath();
                String recipient = pendingList.get(row).getRecipient();
                String owner = pendingList.get(row).getOwner();
                shareFiles.approve(path, recipient, owner);
                break;
            case "reject":
                pendingList = (List<shareFiles>) session.getAttribute("pendingList");
                path = masterPath+pendingList.get(row).getPath();
                recipient = pendingList.get(row).getRecipient();
                owner = pendingList.get(row).getOwner();
                shareFiles.reject(path, recipient, owner);
                break;
            case "remove":
                List<shareFiles> approvedList = (List<shareFiles>) session.getAttribute("approvedList");
                path = masterPath+approvedList.get(row).getPath();
                recipient = approvedList.get(row).getRecipient();
                owner = approvedList.get(row).getOwner();
                shareFiles.reject(path, recipient, owner);
                break;
        }
        List<shareFiles> pendingList = shareFiles.getShareStatus(email,"pending");
        List<shareFiles> approvedList = shareFiles.getShareStatus(email,"approved");
        session.setAttribute("pendingList", pendingList);
        session.setAttribute("approvedList", approvedList);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
