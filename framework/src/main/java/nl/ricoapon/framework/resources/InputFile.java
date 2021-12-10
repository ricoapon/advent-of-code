package nl.ricoapon.framework.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstraction of a resource file that can be used as input for algorithms.
 */
public record InputFile(String path) {
    private final static String INPUT_FILENAME_REGEX_PATTERN = "/day([1-9]|1[0-9]|2[0-5])/input\\.txt";
    private final static String EXAMPLE_FILENAME_REGEX_PATTERN = "/day([1-9]|1[0-9]|2[0-5])/part([12])_example(\\d+)\\.txt";

    public InputFile(String path) {
        this.path = path;

        if (!path.matches(INPUT_FILENAME_REGEX_PATTERN) && !isExample()) {
            throw new IllegalArgumentException("Cannot create an InputFile that doesn't satisfy input or example filename patterns. Path argument was: '" + path + "'");
        }
    }

    /**
     * @return Whether this file refers to an example ({@code true}) or the actual input file ({@code false}).
     */
    public boolean isExample() {
        return path.matches(EXAMPLE_FILENAME_REGEX_PATTERN);
    }

    /**
     * @return Which part this file is an input for. Only relevant when {@link #isExample()} is {@code true}.
     */
    public int part() {
        Matcher matcher = Pattern.compile(EXAMPLE_FILENAME_REGEX_PATTERN).matcher(path);
        if (!matcher.matches()) {
            throw new RuntimeException("part() was called for a non-example InputFile");
        }
        return Integer.parseInt(matcher.group(2));
    }

    /**
     * @return Whether this file exists ({@code true}) or not ({@code false}).
     */
    public boolean exists() {
        return InputFile.class.getResource(path) != null;
    }

    /**
     * @return The content of the file.
     */
    public String readContent() {
        return FileUtil.readContentFromResource(path);
    }
}
