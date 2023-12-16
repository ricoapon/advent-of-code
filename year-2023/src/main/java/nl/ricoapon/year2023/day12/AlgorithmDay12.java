package nl.ricoapon.year2023.day12;

import java.util.ArrayList;
import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay12 implements Algorithm {
    @Override
    public String part1(String input) {
        List<ConditionRow> conditionRows = ConditionRow.of(input);
        int result = conditionRows.stream().mapToInt(this::nrOfArrangements).sum();
        return String.valueOf(result);
    }

    private int nrOfArrangements(ConditionRow conditionRow) {
        int nrOfDamagedToPlace = conditionRow.actualNrOfDamaged() - conditionRow.count(ConditionRow.Condition.DAMAGED);
        int nrOfUnknowns = conditionRow.count(ConditionRow.Condition.UNKNOWN);

        if (nrOfDamagedToPlace == nrOfUnknowns) {
            return 1;
        }

        // Loop through all combinations placing nrOfDamagedToPlace inside nrOfUnknowns.
        return 0;
    }

    boolean satisfiesCondition(List<ConditionRow.Condition> conditions, List<Integer> groups) {
        ConditionRow.Condition previous = null;
        List<Integer> actualGroups = new ArrayList<>();
        for (ConditionRow.Condition c : conditions) {
            if (ConditionRow.Condition.DAMAGED.equals(c)) {
                if (previous == null || !ConditionRow.Condition.DAMAGED.equals(previous)) {
                    actualGroups.add(1);
                } else {
                    actualGroups.set(actualGroups.size() - 1, actualGroups.get(actualGroups.size() - 1) + 1);
                }
            }
            previous = c;
        }

        return actualGroups.equals(groups);
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
