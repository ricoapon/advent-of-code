package nl.ricoapon.year2025.day9;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
import nl.ricoapon.year2025.day9.AlgorithmDay9.Line;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class AlgorithmDay9Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(2025, 9);

    @Test
    void lineContainsWorks() {
        var a = new Coordinate2D(1,3);
        var middle = new Coordinate2D(1, 4);
        var b = new Coordinate2D(1, 6);
        var line1 = new Line(a, b);
        var line2 = new Line(b, a);

        for (var c : List.of(a, middle, b)) {
            assertTrue(line1.contains(c));
            assertTrue(line2.contains(c));
        }
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
