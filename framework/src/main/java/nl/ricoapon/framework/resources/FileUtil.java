package nl.ricoapon.framework.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtil {
    /**
     * @param path The path to the resource.
     * @return The content of the resource. All occurrences of '\r' are removed and new lines at the end as well.
     */
    public static String readContentFromResource(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(FileUtil.class.getResource(path)).toURI())));
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
