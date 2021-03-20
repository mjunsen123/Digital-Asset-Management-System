package servletPackage;

import classPackage.action;
import classPackage.digitalFiles;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class deleteServlet extends HttpServlet {

    protected void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession(false);
            String currentPath = digitalFiles.masterPath() + request.getParameter("currentPath");
            List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
            String[] str = request.getParameterValues("target[]");
            int size = str.length;
            int[] selected = new int[size];
            for (int i = 0; i < size; i++) {
                selected[i] = Integer.parseInt(str[i]);
            }
            for (int i = 0; i < selected.length; i++) {
                String path = list.get(selected[i]).getPath();
                String type = list.get(selected[i]).getType();
                action.delete(path, type);
            }
            digitalFiles.getFiles(currentPath, session);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void deletePreview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String currentPath = digitalFiles.masterPath() + request.getParameter("currentPath");
            String deletePath = (String) session.getAttribute("previewPath");
            action.delete(deletePath, "files");
            session.setAttribute("metadataArray", null);
            session.setAttribute("previewPath", null);
            digitalFiles.getFiles(currentPath, session);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("source") == null) {
            delete(request, response);
        }else{
            deletePreview(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
