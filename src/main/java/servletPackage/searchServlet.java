package servletPackage;

import classPackage.digitalFiles;
import classPackage.search;
import classPackage.user;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class searchServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        HttpSession session = request.getSession(false);
        user u = (user)session.getAttribute("user");
        String email = u.getEmail();
        String keyword = request.getParameter("keyword");
        String path = digitalFiles.masterPath() + email;
        List<digitalFiles> searchList = new ArrayList<>();
        searchList = search.searchName(path, keyword, searchList);
        //searchList for frontend // searchTemp for backend
        session.setAttribute("searchList", searchList);
        session.setAttribute("searchTemp", searchList);
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
