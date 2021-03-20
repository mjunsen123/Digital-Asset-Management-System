package servletPackage;

import classPackage.action;
import classPackage.digitalFiles;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class renameServlet extends HttpServlet {

    protected void rename(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String currentPath = digitalFiles.masterPath() + request.getParameter("currentPath");
        String newName = request.getParameter("name");
        int row = Integer.parseInt(request.getParameter("row"));
        List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
        String oldPath = list.get(row).getPath();
        String newPath = currentPath + "/" + newName;
        String success = action.rename(oldPath, newPath, newName);
        digitalFiles.getFiles(currentPath, session);
        response.setContentType("text/plain");
        response.getWriter().write(success);
    }

    protected void renamePreview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String currentPath = digitalFiles.masterPath() + request.getParameter("currentPath");
        String newName = request.getParameter("name");
        String oldPath = (String) session.getAttribute("previewPath");
        int i = oldPath.lastIndexOf("/");
        String newPath = oldPath.substring(0,i) + "/" + newName;
        String success = action.rename(oldPath, newPath, newName);
        digitalFiles.getFiles(currentPath, session);
        response.setContentType("text/plain");
        response.getWriter().write(success);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("source") == null) {
            rename(request, response);
        } else {
            renamePreview(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
