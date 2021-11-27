package nl.ricoapon.framework.testrunner;

import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.framework.resources.TestData;
import nl.ricoapon.framework.resources.TestDataProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Utility methods to make it easy to run tests on {@link Algorithm} classes.
 */
@SuppressWarnings("ClassCanBeRecord")
public class AlgorithmDayTestRunnerUtil {
    private final int day;

    public AlgorithmDayTestRunnerUtil(int day) {
        this.day = day;
    }

    public void testAllExamples(int part) {
        TestDataProvider.determineTestDataForDay(day).stream()
                .filter(TestData::isExample)
                .filter(t -> t.part() == part)
                .forEach(this::runTestUsingTestData);
    }

    public void testSingleExample(int part, int exampleNumber) {
        TestDataProvider.determineTestDataForDay(day).stream()
                .filter(TestData::isExample)
                .filter(t -> t.part() == part)
                .filter(t -> t.exampleNumber() == exampleNumber)
                .forEach(this::runTestUsingTestData);
    }

    public void testInput(int part) {
        TestDataProvider.determineTestDataForDay(day).stream()
                .filter(t -> !t.isExample())
                .filter(t -> t.part() == part)
                .forEach(this::runTestUsingTestData);
    }

    private void runTestUsingTestData(TestData testData) {
        Optional<Algorithm> algorithm = instantiateAlgorithm(testData.day());
        if (algorithm.isEmpty()) {
            fail("Cannot instantiate algorithm");
            return;
        }

        Function<String, String> partToInvoke = testData.part() == 1 ? algorithm.get()::part1 : algorithm.get()::part2;
        assertEquals(testData.expectedOutput(), partToInvoke.apply(testData.input()));
    }

    private static Optional<Algorithm> instantiateAlgorithm(int day) {
        try {
            return Optional.of((Algorithm) Class.forName("nl.ricoapon.day" + day + ".AlgorithmDay" + day).getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
