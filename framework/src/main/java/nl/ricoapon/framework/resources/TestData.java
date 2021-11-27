package nl.ricoapon.framework.resources;

/**
 * Contains all data needed to run a test.
 */
public record TestData(int day, int part, String input, String expectedOutput, boolean isExample, Integer exampleNumber) {
}
