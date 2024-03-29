package nl.ricoapon.framework.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * @param path The path to the resource.
     * @return The content of the resource. All occurrences of '\r' are removed and new lines at the end as well.
     */
    public static String readContentFromResource(String path) {
        try {
            URL resource = FileUtil.class.getResource(path);
            if (resource == null) {
                throw new RuntimeException("The file with given path does not exist: '" + path + "'");
            }
            String content = new String(Files.readAllBytes(Paths.get(resource.toURI())));
            content = content.replaceAll("\r", "");
            while (content.endsWith("\n")) {
                content = content.substring(0, content.length() - 1);
            }
            return content;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Could not read content of '" + path + "'");
        }
    }

}
