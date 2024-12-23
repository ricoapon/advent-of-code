package nl.ricoapon.year2024.day23;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class AlgorithmDay23Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2024, 23);

    @Test
    void ensureConnectedComponentIsEqualWhenListEqualsAsSet() {
        ConnectedComponent c1 = new ConnectedComponent(null, List.of("b", "a"));
        ConnectedComponent c2 = new ConnectedComponent(null, List.of("a", "b"));

        assertEquals(c1, c2);
    }

    @Test
    void part1_example() {
        algorithmDayTestRunnerUtil.testAllExamples(1);
    }

    @Test
    void part1() {
        algorithmDayTestRunnerUtil.testInput(1);
    }

    @Test
    void part2_example() {
        algorithmDayTestRunnerUtil.testAllExamples(2);
    }

    @Test
    void part2() {
        algorithmDayTestRunnerUtil.testInput(2);
    }
}
