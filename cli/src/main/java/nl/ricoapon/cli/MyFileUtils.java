package nl.ricoapon.cli;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper methods of commons-io {@link FileUtils} where all exceptions are rethrown as {@link RuntimeException}s.
 */
public class MyFileUtils {

    /**
     * @return The home directory of the entire repository.
     */
    public static File determineHomeDirectory() {
        return new File("./");
    }

    /**
     * Touches a file. If any directories in between do not exist, these are created as well.
     * @param file The file to create.
     */
    public static void touchFile(File file) {
        try {
            FileUtils.touch(file);
        } catch (IOException e) {
            throw new RuntimeException("File creation failed", e);
        }
    }

    /**
     * Overwrites the content of the file with the given content.
     * @param file    The file to write to.
     * @param content The content to write.
     * @throws RuntimeException If anything went wrong when writing into the file.
     */
    public static void overwriteContentOfFile(File file, String content) throws RuntimeException {
        try {
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file", e);
        }
    }
}
