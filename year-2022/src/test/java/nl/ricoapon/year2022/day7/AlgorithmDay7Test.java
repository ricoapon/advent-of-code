package nl.ricoapon.year2022.day7;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
import org.junit.jupiter.api.Test;

class AlgorithmDay7Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2022, 7);

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
