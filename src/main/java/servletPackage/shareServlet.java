package servletPackage;

import classPackage.digitalFiles;
import classPackage.share;
import classPackage.user;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class shareServlet extends HttpServlet {

    protected void shareEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        user u = (user) session.getAttribute("user");
        String email = u.getEmail();
        String[] arr = request.getParameterValues("arr[]");
        String path;
        if (request.getParameter("row") == null) {
            path = (String) session.getAttribute("previewPath");
        } else {
            int row = Integer.parseInt(request.getParameter("row"));
            List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
            path = list.get(row).getPath();
        }
        boolean status = share.share(arr, path, email);
        String success = "0";
        if (status) {
            success = "1";
        } else {
            success = "0";
        }
        response.setContentType("text/plain");
        response.getWriter().write(success);
    }

    protected void shareLink(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        user u = (user) session.getAttribute("user");
        String email = u.getEmail();
        String path;
        if (request.getParameter("row") == null) {
            path = (String) session.getAttribute("previewPath");
        } else {
            int row = Integer.parseInt(request.getParameter("row"));
            List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
            path = list.get(row).getPath();
        }

        String link = share.share(path, email);
        response.setContentType("text/plain");
        response.getWriter().write(link);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameterValues("arr[]") == null) {
            shareLink(request, response);
        } else {
            shareEmail(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
