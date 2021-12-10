package nl.ricoapon.framework.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstraction of a resource file that can be used as input for algorithms.
 */
public record InputFile(String path, boolean canBeUsedForPart1, boolean canBeUsedForPart2, boolean isExample) {
    private final static String INPUT_FILENAME_REGEX_PATTERN = "/day([1-9]|1[0-9]|2[0-5])/input\\.txt";
    private final static String EXAMPLE_FILENAME_REGEX_PATTERN = "/day([1-9]|1[0-9]|2[0-5])/part([12])_example(\\d+)\\.txt";

    public static InputFile of(String path) {
        Matcher matcher = Pattern.compile(INPUT_FILENAME_REGEX_PATTERN).matcher(path);
        if (matcher.matches()) {
            return new InputFile(path, true, true, false);
        }

        matcher = Pattern.compile(EXAMPLE_FILENAME_REGEX_PATTERN).matcher(path);
        if (matcher.matches()) {
            int part = Integer.parseInt(matcher.group(2));
            return new InputFile(path, (part == 1), (part == 2), true);
        }

        throw new IllegalArgumentException("Cannot create an InputFile that doesn't satisfy input or example filename patterns. Path argument was: '" + path + "'");
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
