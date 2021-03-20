package classPackage;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;

public class digitalFiles {

    private String name;
    private String path;
    private String size;
    private String mdate;
    private String type;
    private String cdate;
    private String metadata;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "digitalFiles{" + "name=" + name + ", path=" + path + ", size=" + size + ", mdate=" + mdate + ", type=" + type + ", cdate=" + cdate + ", metadata=" + metadata + '}';
    }

    public static String masterPath() {
//        return "/home/mjunsen123/Documents/DAMS/userFile/";
        return "/home/ubuntu/PAVE/userFile/";
    }

    public static Comparator<digitalFiles> compareName = new Comparator<digitalFiles>() {
        @Override
        public int compare(digitalFiles d1, digitalFiles d2) {
            String digitalFiles1 = d1.getName().toUpperCase();
            String digitalFiles2 = d2.getName().toUpperCase();
            return digitalFiles1.compareTo(digitalFiles2);
        }
    };

    public static String getSize(BasicFileAttributes attr) {
        long size = attr.size();
        String fsize = null;
        double size_kb = size / 1024;
        double size_mb = size_kb / 1024;
        double size_gb = size_mb / 1024;

        if (size > 1073741824) {
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(size_gb);
            fsize = s + " GB";
        } else if (size > 1048576) {
            int s = (int) size_mb;
            fsize = s + " MB";
        } else if (size > 1024) {
            int s = (int) size_kb;
            fsize = s + " KB";
        } else if (size > 0) {
            fsize = "1 KB";
        }

        return fsize;
    }

    public static String getExtention(File file) {
        String extension = "folder";
        String fileName = file.getName();
        Path path = Paths.get(file.getAbsolutePath());
        if (Files.isRegularFile(path)) {
            extension = FilenameUtils.getExtension(fileName);
        }
        return extension;
    }

    public static void getFiles(String fpath, HttpSession session) {
        try {
            File path = new File(fpath);
            List<digitalFiles> newList = new ArrayList<>();
            String currentPath = fpath.substring(masterPath().length(), fpath.length());
            File filesList[] = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return !file.isHidden();
                }
            });
            SimpleDateFormat dfrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            for (File f : filesList) {
                digitalFiles df = new digitalFiles();
                BasicFileAttributes attr = Files.readAttributes(Paths.get(f.getAbsolutePath()), BasicFileAttributes.class);
                df.setName(f.getName());
                df.setPath(f.getAbsolutePath());
                df.setType(digitalFiles.getExtention(f));
                if (!"folder".equals(df.getType())) {
                    df.setSize(digitalFiles.getSize(attr));
                }
                df.setCdate(dfrmt.format(attr.creationTime().toMillis()));
                df.setMdate(dfrmt.format(attr.lastModifiedTime().toMillis()));
                newList.add(df);
            }
            Collections.sort(newList,compareName);
            newList = new ArrayList<>(new LinkedHashSet<>(newList));
            session.setAttribute("fileList", newList);
            session.setAttribute("cpath", currentPath);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
