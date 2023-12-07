package nl.ricoapon.day7;

import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class AlgorithmDay7Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(7);

    @Test
    void testEnum() {
        List<Hand.Type> types = Stream.of(Hand.Type.FOUR_OF_A_KIND, Hand.Type.TWO_PAIR, Hand.Type.FIVE_OF_A_KIND).sorted().toList();
        assertEquals(types, List.of(Hand.Type.FIVE_OF_A_KIND, Hand.Type.FOUR_OF_A_KIND, Hand.Type.TWO_PAIR));
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
