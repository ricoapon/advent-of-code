package nl.ricoapon.year2022.day11;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monkey {
    public final int id;
    public final Queue<Long> items;
    public final UnaryOperator<Long> operation;
    public final int testDivisibleBy;
    public final int monkeyIdIfTestIsTrue;
    public final int monkeyIdIfTestIsFalse;

    public Monkey(int id, Queue<Long> items, UnaryOperator<Long> operation, int testDivisibleBy, int monkeyIdIfTestIsTrue, int monkeyIdIfTestIsFalse) {
        this.id = id;
        this.items = items;
        this.operation = operation;
        this.testDivisibleBy = testDivisibleBy;
        this.monkeyIdIfTestIsTrue = monkeyIdIfTestIsTrue;
        this.monkeyIdIfTestIsFalse = monkeyIdIfTestIsFalse;
    }

    private final static Pattern MONKEY = Pattern.compile(
            "Monkey (\\d+):\\s+" +
                    "Starting items: (.*)\\s+" +
                    "Operation: new = (.*)\\s+" +
                    "Test: divisible by (\\d+)\\s+" +
                    "If true: throw to monkey (\\d+)\\s+" +
                    "If false: throw to monkey (\\d+)\\s*");

    public static Monkey of(String input) {
        Matcher matcher = MONKEY.matcher(input);
        if (!matcher.matches()) {
            throw new RuntimeException("This should never happen");
        }

        int id = Integer.parseInt(matcher.group(1));

        Queue<Long> items = new LinkedList<>();
        for (String itemAsString : matcher.group(2).split(",")) {
            items.add(Long.parseLong(itemAsString.trim()));
        }

        UnaryOperator<Long> operation;
        String[] operationArray = matcher.group(3).split(" ");
        // We know that operation[0] is always "old", so we don't need to check this.
        // We also know that we can only have + and * as operators.
        boolean isPlus = operationArray[1].equals("+");
        if (operationArray[2].equals("old")) {
            if (isPlus) {
                operation = (i) -> i + i;
            } else {
                operation = (i) -> i * i;
            }
        } else {
            long value = Long.parseLong(operationArray[2]);
            if (isPlus) {
                operation = (i) -> i + value;
            } else {
                operation = (i) -> i * value;
            }
        }

        int divisibleBy = Integer.parseInt(matcher.group(4));
        int monkeyIdIfTestIsTrue = Integer.parseInt(matcher.group(5));
        int monkeyIdIfTestIsFalse = Integer.parseInt(matcher.group(6));

        return new Monkey(id, items, operation, divisibleBy, monkeyIdIfTestIsTrue, monkeyIdIfTestIsFalse);
    }
}
