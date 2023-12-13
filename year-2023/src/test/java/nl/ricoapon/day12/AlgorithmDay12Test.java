package nl.ricoapon.day12;

import nl.ricoapon.day12.ConditionRow.Condition;
import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class AlgorithmDay12Test {
    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(12);

    @Test
    void satisfiesCondition() {
        // Given
        List<Condition> conditions = List.of(Condition.DAMAGED, Condition.OPERATIONAL, Condition.DAMAGED,
                Condition.OPERATIONAL, Condition.DAMAGED, Condition.DAMAGED, Condition.DAMAGED);
        List<Integer> groups = List.of(1, 1, 3);

        // When and then
        assertTrue(new AlgorithmDay12().satisfiesCondition(conditions, groups));
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
