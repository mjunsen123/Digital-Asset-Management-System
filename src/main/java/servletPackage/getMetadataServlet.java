package servletPackage;

import classPackage.digitalFiles;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class getMetadataServlet extends HttpServlet {

    protected void getMetadata(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
        if (request.getParameter("source")!=null) {
            list = (List<digitalFiles>) session.getAttribute("shareList");
        }
        session.setAttribute("metadataArray", null);
        session.setAttribute("previewPath", null);
        int row = Integer.parseInt(request.getParameter("row"));
        String path = list.get(row).getPath();
        String name = list.get(row).getName();
        String metadata[] = classPackage.metadata.getMetadata(path);
        session.setAttribute("metadataArray", metadata);
        session.setAttribute("previewPath", path);
        response.setContentType("text/plain");
        response.getWriter().write(name);
    }

    protected void deleteMetadata(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = (String) session.getAttribute("previewPath");
        String metadataArray[] = classPackage.metadata.getMetadata(path);
        int targetIndex = Integer.parseInt(request.getParameter("index"));
        String newMetadatList = "";
        for (int i = 0; i < metadataArray.length; i++) {
            if (targetIndex != i) {
                newMetadatList = newMetadatList + metadataArray[i] + ",";
            }
        }
        classPackage.metadata.deleteMetadata(path);
        classPackage.metadata.setMetadata(path, newMetadatList);
        String metadata[] = classPackage.metadata.getMetadata(path);
        session.setAttribute("metadataArray", metadata);
        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + "Delete [" + metadataArray[targetIndex] + "] from metadata");
    }

    protected void addMetadata(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = (String) session.getAttribute("previewPath");
        String metadataArray[] = classPackage.metadata.getMetadata(path);
        String tag = request.getParameter("tag");
        String newMetadata = "";
        if (metadataArray != null) {
            for (String s : metadataArray) {
                newMetadata = newMetadata + s + ",";
            }
        }
        newMetadata = newMetadata + tag + ",";
        classPackage.metadata.deleteMetadata(path);
        classPackage.metadata.setMetadata(path, newMetadata);
        String metadata[] = classPackage.metadata.getMetadata(path);
        session.setAttribute("metadataArray", metadata);
        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION " + "Add [" + tag + "] to metadata");
    }
    
    protected void getSearchMetadata(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("searchTemp");
        session.setAttribute("metadataArray", null);
        session.setAttribute("previewPath", null);
        int row = Integer.parseInt(request.getParameter("row"));
        String path = list.get(row).getPath();
        String name = list.get(row).getName();
        String metadata[] = classPackage.metadata.getMetadata(path);
        session.setAttribute("metadataArray", metadata);
        session.setAttribute("previewPath", path);
        response.setContentType("text/plain");
        response.getWriter().write(name);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "get":
                getMetadata(request, response);
                break;
            case "delete":
                deleteMetadata(request, response);
                break;
            case "add":
                addMetadata(request, response);
                break;
            case "getSearch":
                getSearchMetadata(request, response);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
