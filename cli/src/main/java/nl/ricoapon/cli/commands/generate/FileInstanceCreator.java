package nl.ricoapon.cli.commands.generate;

import java.io.File;

/**
 * Class for generating all the paths to the files to generate relative to the home directory.
 */
public class FileInstanceCreator {
    private final int day;
    private final File homeDirectory;
    private final String startingPath;
    private final String resourceStartingPath;

    public FileInstanceCreator(File homeDirectory, int year, int day) {
        this.homeDirectory = homeDirectory;
        this.day = day;
        startingPath = "year-" + year + "/src/";
        resourceStartingPath = startingPath + "main/resources/day" + day + "/";
    }

    public File algorithmDay() {
        return fileOf(startingPath + "main/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + ".java");
    }

    public File algorithmDayTest() {
        return fileOf(startingPath + "test/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + "Test.java");
    }

    public File expected() {
        return fileOf(resourceStartingPath + "expected.txt");
    }

    public File input() {
        return fileOf(resourceStartingPath + "input.txt");
    }

    public File part1example1() {
        return fileOf(resourceStartingPath + "part1_example1.txt");
    }

    public File part2example1() {
        return fileOf(resourceStartingPath + "part2_example1.txt");
    }

    private File fileOf(String path) {
        return new File(homeDirectory, path);
    }
}
