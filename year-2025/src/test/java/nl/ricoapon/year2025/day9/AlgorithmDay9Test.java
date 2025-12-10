package nl.ricoapon.year2025.day9;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
import nl.ricoapon.year2025.day9.AlgorithmDay9.Line;

class AlgorithmDay9Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2025, 9);

    @Test
    void smallTest() {
        Line squareLine = new Line(new Coordinate2D(9, 3), new Coordinate2D(9, 7));
        Line edgeLine = new Line(new Coordinate2D(9, 7), new Coordinate2D(11, 7));

        assertFalse(squareLine.hasSinglePointInsideLineIntersectionWith(edgeLine));
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
