package classPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class search {

    public static List<digitalFiles> searchName(String path, String keyword, List<digitalFiles> searchList) {
        File filepath = new File(path);
        File filesList[] = filepath.listFiles();
        for (File f : filesList) {
            String metadata[] = classPackage.metadata.getMetadata(f.getAbsolutePath());
            String data = Arrays.toString(metadata).replace("[", "").replace("]", "");
            if (metadata == null) {
                data = "-";
            }

            if (f.getName().toLowerCase().contains(keyword.toLowerCase())) {
                digitalFiles df = new digitalFiles();
                df.setName(f.getName());
                df.setMetadata(data);
                df.setPath(f.getAbsolutePath());
                df.setType(digitalFiles.getExtention(f));
                searchList.add(df);
            } else {
                if (metadata != null) {
                    for (String s : metadata) {
                        if (s.toLowerCase().equals(keyword.toLowerCase())) {
                            digitalFiles df = new digitalFiles();
                            df.setName(f.getName());
                            df.setMetadata(data);
                            df.setPath(f.getAbsolutePath());
                            df.setType(digitalFiles.getExtention(f));
                            searchList.add(df);
                            break;
                        }
                    }
                }
            }
            if (f.isDirectory()) {
                searchName(f.getAbsolutePath(), keyword, searchList);
            }
        }
        searchList = new ArrayList<>(new LinkedHashSet<>(searchList));
        return searchList;
    }

}
