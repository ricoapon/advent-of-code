package nl.ricoapon.cli.actions.generate;

/**
 * Class for generating all the paths to the files to generate relative to the home directory.
 */
public class PathCreator {
    private final int day;
    private final String startingPath;
    private final String resourceStartingPath;

    public PathCreator(int year, int day) {
        this.day = day;
        startingPath = "year-" + year + "/src/";
        resourceStartingPath = startingPath + "main/resources/day" + day + "/";
    }

    public String algorithmDay() {
        return startingPath + "main/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + ".java";
    }

    public String algorithmDayTest() {
        return startingPath + "test/java/nl/ricoapon/day" + day + "/AlgorithmDay" + day + "Test.java";
    }

    public String expected() {
        return resourceStartingPath + "expected.txt";
    }

    public String input() {
        return resourceStartingPath + "input.txt";
    }

    public String part1example1() {
        return resourceStartingPath + "part1_example1.txt";
    }

    public String part2example1() {
        return resourceStartingPath + "part2_example1.txt";
    }
}
