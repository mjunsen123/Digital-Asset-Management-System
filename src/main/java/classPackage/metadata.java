package classPackage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class metadata {

    public static void setMetadata(String filePath, String data) {
        try {
            Path path = Paths.get(filePath);
            UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path,
                    UserDefinedFileAttributeView.class);
            udfav.write("filetag", Charset.defaultCharset().encode(data));
        } catch (IOException e) {
        }
    }

    public static String[] getMetadata(String filePath) {
        String metadataArray[] = null;
        try {
            Path path = Paths.get(filePath);
            UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path,
                    UserDefinedFileAttributeView.class);
            int size = udfav.size("filetag");
            if (size > 0) {
                ByteBuffer bb = ByteBuffer.allocateDirect(size);
                udfav.read("filetag", bb);
                bb.flip();
                String metadata = Charset.defaultCharset().decode(bb).toString();
                metadataArray = metadata.split(",");
            }
        } catch (IOException e) {
        }
        return metadataArray;
    }

    public static void deleteMetadata(String filePath) {
        try {
            Path path = Paths.get(filePath);
            UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path,
                    UserDefinedFileAttributeView.class);
            udfav.delete("filetag");
        } catch (IOException e) {
        }
    }

}
