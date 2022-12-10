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
        startingPath = "year-" + year + "/";
        resourceStartingPath = startingPath + "src/main/resources/day" + day + "/";
    }

    public File parentPom() {
        return fileOf(startingPath + "../pom.xml");
    }

    public File yearPom() {
        return fileOf(startingPath + "pom.xml");
    }

    public File algorithmDay() {
        return fileOf(startingPath + "src/main/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + ".java");
    }

    public File algorithmDayTest() {
        return fileOf(startingPath + "src/test/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + "Test.java");
    }

    public File expected() {
        return fileOf(resourceStartingPath + "expected.yml");
    }

    public File input() {
        return fileOf(resourceStartingPath + "input.txt");
    }

    public File example() {
        return fileOf(resourceStartingPath + "example.txt");
    }

    private File fileOf(String path) {
        return new File(homeDirectory, path);
    }
}
