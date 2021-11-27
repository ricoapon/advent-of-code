package nl.ricoapon.framework;

/**
 * Interface that should be used for classes that contain the solution algorithm to a problem of a <b>single</b> day.
 */
public interface SolutionAlgorithm {
    /**
     * @param input The input of the problem.
     * @return The solution to part 1 of the problem.
     */
    String part1(String input);

    /**
     * @param input The input of the problem.
     * @return The solution to part 2 of the problem.
     */
    String part2(String input);
}
