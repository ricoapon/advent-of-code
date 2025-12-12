package nl.ricoapon.year2025.day11;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
import org.junit.jupiter.api.Test;

class AlgorithmDay11Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2025, 11);

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
