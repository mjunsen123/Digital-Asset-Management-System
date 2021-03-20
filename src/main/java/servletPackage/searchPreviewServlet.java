package servletPackage;

import classPackage.digitalFiles;
import classPackage.previewer;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class searchPreviewServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("searchTemp");
        int row = Integer.parseInt(request.getParameter("row"));
        String path = list.get(row).getPath();
        String type = list.get(row).getType();
        if (type.equals("folder")) {
            digitalFiles.getFiles(path, session);
        } else {
            switch (type) {
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
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
