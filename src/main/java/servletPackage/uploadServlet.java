package servletPackage;

import classPackage.digitalFiles;
import classPackage.tensorFlow;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FilenameUtils;

public class uploadServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private String success;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //In Multipart, there are at least 2 collection, first collection store current path, second onward store files selected.
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                success = "0";
            } else {
                DiskFileItemFactory itemFact = new DiskFileItemFactory();
                ServletFileUpload sf = new ServletFileUpload(itemFact);
                List<FileItem> multiparts = sf.parseRequest(request);
                filePath = digitalFiles.masterPath() + multiparts.get(0).getString();
                String newSuperPath = null;

                //Check folder
                for (FileItem item : multiparts) {
                    if (item.getFieldName().equals("files[]") && !item.isFormField() && item.getName().contains("/")) {
                        String filename = item.getName();
                        System.out.println(filename);
                        int i = filename.indexOf("/");
                        String superPath = filename.substring(0, i);
                        newSuperPath = checkFolder(filePath, superPath);
                        break;
                    }
                }

                for (FileItem item : multiparts) {
                    if (item.getFieldName().equals("files[]")) {
                        if (!item.isFormField()) {
                            String filename = item.getName();
                            if (filename.contains("/")) {
                                int i = filename.indexOf("/");
                                filename = newSuperPath + filename.substring(i, filename.length());
                                System.out.println(filename);
                            } else {
                                filename = checkFile(filePath, filename);
                            }
                            File f = new File(filename);
                            try {
                                item.write(f);

                                //tensorFlow
                                if (digitalFiles.getExtention(f).equals("png") || digitalFiles.getExtention(f).equals("jpg") || digitalFiles.getExtention(f).equals("jpeg")) {
                                    String tag = tensorFlow.imageTag(f.getAbsolutePath());
                                    if (tag != null) {
                                        String metadataArray[] = classPackage.metadata.getMetadata(f.getAbsolutePath());
                                        String newMetadata = "";
                                        if (metadataArray != null) {
                                            for (String s : metadataArray) {
                                                newMetadata = newMetadata + s + ",";
                                            }
                                        }
                                        newMetadata = newMetadata + tag + ",";
                                        classPackage.metadata.deleteMetadata(f.getAbsolutePath());
                                        classPackage.metadata.setMetadata(f.getAbsolutePath(), newMetadata);
                                    }
                                }
                                System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Uploaded " + item.getName() + " in " + filePath);
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }

                        }
                    }
                }
                FileCleaningTracker tracker = itemFact.getFileCleaningTracker();
                if (tracker != null) {
                    tracker.exitWhenFinished();
                }
                success = "1";
            }
            digitalFiles.getFiles(filePath, session);
            response.setContentType("text/plain");
            response.getWriter().write(success);
        } catch (IOException | FileUploadException e) {
            System.err.println(e.getMessage());
        }

    }

    static String checkFile(String filePath, String fileName) {
        String fullPath = filePath + "/" + fileName;
        String name = FilenameUtils.getBaseName(fileName);
        String extension = FilenameUtils.getExtension(fileName);
        Path path = Paths.get(fullPath);
        int counter = 1;
        while (Files.exists(path)) {
            fullPath = filePath + "/" + name + "(" + counter + ")." + extension;
            path = Paths.get(fullPath);
            counter++;
        }
        return fullPath;
    }

    static String checkFolder(String filePath, String fileName) {
        String fullPath = filePath + "/" + fileName;
        Path path = Paths.get(fullPath);
        int counter = 1;
        while (Files.exists(path)) {
            fullPath = filePath + "/" + fileName + "(" + counter + ")";
            path = Paths.get(fullPath);
            counter++;
        }
        return fullPath;
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
