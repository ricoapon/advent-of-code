package nl.ricoapon.year2023.day19;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class AlgorithmDay19Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2023, 19);

    @Test
    void count() {
        AlgorithmDay19 algorithmDay19 = new AlgorithmDay19();
        assertEquals(256000000000000L, algorithmDay19.determinePossibleCombinations(new ArrayList<>()));
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
