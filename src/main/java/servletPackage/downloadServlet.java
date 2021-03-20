/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletPackage;

import classPackage.shareFiles;
import classPackage.digitalFiles;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class downloadServlet extends HttpServlet {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    protected void processRequest(HttpServletResponse response, String path)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            File downloadFile = new File(path);
            OutputStream outStream;
            try ( FileInputStream inStream = new FileInputStream(downloadFile)) {
                ServletContext context = getServletContext();
                String mimeType = context.getMimeType(path);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setContentLength((int) downloadFile.length());
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
                response.setHeader(headerKey, headerValue);
                outStream = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            outStream.close();
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Download file " + path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("source") != null) {
            if (request.getParameter("type") != null) {
                //Download folder from shareing
                HttpSession session = request.getSession(false);
                List<shareFiles> list = (List<shareFiles>) session.getAttribute("shareList");
                int row = Integer.parseInt(request.getParameter("row"));
                String path = list.get(row).getPath();
                response.setContentType("application/zip");
                File fileToZip = new File(path);
                String[] files = fileToZip.list();
                if (files != null && files.length > 0) {
                    byte[] zip = zipFiles(fileToZip, files);
                    OutputStream out = response.getOutputStream();
                    response.setContentType("application/zip");
                    String headerKey = "Content-Disposition";
                    String headerValue = String.format("attachment; filename=\"%s\"", fileToZip.getName() + ".zip");
                    response.setHeader(headerKey, headerValue);
                    out.write(zip);
                    out.flush();
                }
            } else {
                //Download file from sharing page
                HttpSession session = request.getSession(false);
                List<shareFiles> list = (List<shareFiles>) session.getAttribute("shareList");
                int row = Integer.parseInt(request.getParameter("row"));
                String path = list.get(row).getPath();
                processRequest(response, path);
            }

        } else {
            if (request.getParameter("row") != null) {
                if (request.getParameter("type") != null) {
                    //Download folder from table list
                    HttpSession session = request.getSession(false);
                    List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
                    int row = Integer.parseInt(request.getParameter("row"));
                    String path = list.get(row).getPath();
                    response.setContentType("application/zip");
                    File fileToZip = new File(path);
                    String[] files = fileToZip.list();
                    if (files != null && files.length > 0) {
                        byte[] zip = zipFiles(fileToZip, files);
                        OutputStream out = response.getOutputStream();
                        response.setContentType("application/zip");
                        String headerKey = "Content-Disposition";
                        String headerValue = String.format("attachment; filename=\"%s\"", fileToZip.getName() + ".zip");
                        response.setHeader(headerKey, headerValue);
                        out.write(zip);
                        out.flush();
                    }

                } else {
                    //Download file from table list
                    HttpSession session = request.getSession(false);
                    List<digitalFiles> list = (List<digitalFiles>) session.getAttribute("fileList");
                    int row = Integer.parseInt(request.getParameter("row"));
                    String path = list.get(row).getPath();
                    processRequest(response, path);
                }
            } else {
                //Download file from previewer
                HttpSession session = request.getSession(false);
                String path = (String) session.getAttribute("previewPath");
                processRequest(response, path);
            }
        }

    }

    public static byte[] zipFiles(File directory, String[] files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        for (String fileName : files) {
            FileInputStream fis = new FileInputStream(directory.getPath() + downloadServlet.FILE_SEPARATOR + fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            zos.putNextEntry(new ZipEntry(fileName));

            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();

        return baos.toByteArray();

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
