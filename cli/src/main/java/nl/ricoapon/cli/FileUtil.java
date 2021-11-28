package nl.ricoapon.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtil {

    /**
     * @return The home directory of the entire repository.
     */
    public static File determineHomeDirectory() {
        return new File("../");
    }

    /**
     * @param path The path to the resource.
     * @return The content of the resource. All occurences of '\r' are removed and new lines at the end as well.
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

    /**
     * Create a new file. If any directories in between do not exist, these are created as well.
     * @param file The file to create.
     * @throws RuntimeException If the file already exists or if anything went wrong while creating the file.
     */
    public static void createFile(File file) throws RuntimeException {
        try {
            // Make sure to only create directories of the parent file, otherwise the file itself will be created as a directory.
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();

            if (!file.createNewFile()) {
                throw new IOException("File " + file.getAbsolutePath() + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("File creation failed", e);
        }
    }

    /**
     * Adds the given content to a file.
     * @param file    The file to write to.
     * @param content The content to write.
     * @throws RuntimeException If anything went wrong when writing into the file.
     */
    public static void appendContentOfFile(File file, String content) throws RuntimeException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file", e);
        }
    }
}
