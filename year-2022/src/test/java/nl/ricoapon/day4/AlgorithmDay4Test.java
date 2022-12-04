package nl.ricoapon.day4;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
import org.junit.jupiter.api.Test;

class AlgorithmDay4Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(4);

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
