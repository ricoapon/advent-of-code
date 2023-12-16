package nl.ricoapon.year2023.day12;

import java.util.Arrays;
import java.util.List;

public record ConditionRow(List<Condition> row, List<Integer> groups) {
    public enum Condition {
        DAMAGED("#"),
        OPERATIONAL("."),
        UNKNOWN("?");

        private final String symbol;

        private Condition(String symbol) {
            this.symbol = symbol;
        }

        public static Condition of(String s) {
            for (Condition c : Condition.values()) {
                if (c.symbol.equals(s)) {
                    return c;
                }
            }

            throw new RuntimeException();
        }
    }

    public static List<ConditionRow> of(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .map(ConditionRow::ofRow)
                .toList();
    }

    private static ConditionRow ofRow(String row) {
        List<Integer> groups = Arrays.stream(row.split(" ")[1].split(",")).map(Integer::valueOf).toList();
        List<Condition> conditions = Arrays.stream(row.split(" ")[0].split(""))
                .map(Condition::of).toList();
        return new ConditionRow(conditions, groups);
    }

    public int actualNrOfDamaged() {
        return groups.stream().mapToInt(x -> x).sum();
    }

    public int count(Condition condition) {
        return (int) row.stream().filter(c -> condition.equals(c)).count();
    }
}
