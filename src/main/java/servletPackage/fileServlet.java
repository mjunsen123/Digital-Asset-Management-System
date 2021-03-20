/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.shareFiles;
import classPackage.digitalFiles;
import classPackage.previewer;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class fileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String extension = request.getParameter("type");
        HttpSession session = request.getSession(false);
        String path = null;
        int row = Integer.parseInt(request.getParameter("row"));
        if ("share".equals(request.getParameter("source"))) {
            List<shareFiles> list = (List<shareFiles>) session.getAttribute("shareList");
            path = list.get(row).getPath();
        } else {
            List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
            path = list.get(row).getPath();
        }
        switch (extension) {
            case "png":
                previewer.previewImagePDF(request, response, path, "image/png");
                break;
            case "jpg":
            case "jpeg":
                previewer.previewImagePDF(request, response, path, "image/jpeg");
                break;
            case "pdf":
                previewer.previewImagePDF(request, response, path, "application/pdf");
                break;
            case "docx":
                previewer.previewDocx(request, response,path);
                break;
            case "doc":
                previewer.previewDoc(request, response,path);
                break;
            case "pptx":
            case "ppt":
                previewer.previewPresentation(request, response, path);
                break;
            case "xlsx":
                previewer.previewSpreadsheet(request, response, path);
                break;
            case "xls":
            case "csv":
                previewer.previewSpreadsheet(request, response, path);
                break;
            case "txt":
            case "java":
                previewer.previewText(request, response, path);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
