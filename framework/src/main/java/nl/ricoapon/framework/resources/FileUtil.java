package nl.ricoapon.framework.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtil {
    /**
     * @param path The path to the resource.
     * @return The content of the resource.
     */
    public static String readContentFromResource(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(FileUtil.class.getResource(path)).toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Could not read content of '" + path + "'");
        }
    }

}
