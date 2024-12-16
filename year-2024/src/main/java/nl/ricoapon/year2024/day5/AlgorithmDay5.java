package nl.ricoapon.year2024.day5;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay5 implements Algorithm {

    private record Rule(int before, int after) {
        public static Rule of(String line) {
            return new Rule(
                    Integer.parseInt(line.substring(0, line.indexOf("|"))),
                    Integer.parseInt(line.substring(line.indexOf("|") + 1)));
        }

        public boolean correctFor(List<Integer> line) {
            var indexBefore = line.indexOf(before);
            var indexAfter = line.indexOf(after);

            // If one of the integers is missing, the rule doesn't need to be checked.
            if (indexBefore < 0 || indexAfter < 0) {
                return true;
            }

            return indexBefore < indexAfter;
        }
    }

    private List<Rule> parseRules(String input) {
        input = input.split("\\r?\\n\\r?\\n")[0];
        return Stream.of(input.split("\\r?\\n")).map(Rule::of).toList();
    }

    private List<Integer> parsePageLine(String line) {
        return Stream.of(line.split(",")).map(Integer::parseInt).toList();
    }

    private List<List<Integer>> parsePages(String input) {
        input = input.split("\\r?\\n\\r?\\n")[1];
        return Stream.of(input.split("\\r?\\n")).map(this::parsePageLine).toList();
    }

    private Integer determineMiddleDigit(List<Integer> line) {
        return line.get((line.size() - 1) / 2);
    }

    @Override
    public Object part1(String input) {
        List<Rule> rules = parseRules(input);
        return parsePages(input).stream()
                .filter(l -> rules.stream().allMatch(r -> r.correctFor(l)))
                .mapToInt(this::determineMiddleDigit)
                .sum();
    }

    private List<Integer> makeCorrect(List<Integer> line, List<Rule> allRules) {
        // We "correct" a line by swapping the two integers that fail for a certain
        // rule. We don't know if this makes the
        // line correct, so we keep trying. Apparently, this works.

        List<Integer> newLine = new ArrayList<>(line);

        while (true) {
            Optional<Rule> rule = allRules.stream().filter(r -> !r.correctFor(newLine)).findAny();
            if (rule.isEmpty()) {
                break;
            }
            // Swap the two integers not matching the rule.
            var firstIndex = newLine.indexOf(rule.get().before);
            var secondIndex = newLine.indexOf(rule.get().after);
            var tempFirst = newLine.get(firstIndex);
            newLine.set(firstIndex, newLine.get(secondIndex));
            newLine.set(secondIndex, tempFirst);
        }

        // All rules match, so we are done.
        return newLine;
    }

    @Override
    public Object part2(String input) {
        List<Rule> rules = parseRules(input);
        List<List<Integer>> incorrectLines = parsePages(input).stream()
                .filter(l -> !rules.stream().allMatch(r -> r.correctFor(l))).toList();

        return incorrectLines.stream().map(l -> makeCorrect(l, rules)).mapToInt(this::determineMiddleDigit).sum();
    }
}
