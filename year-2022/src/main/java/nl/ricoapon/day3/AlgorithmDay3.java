package nl.ricoapon.day3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay3 implements Algorithm {
    @Override
    public String part1(String input) {
        int max = Arrays.stream(input.split("\r?\n")).map(this::findDoubleItem).map(this::determineScore).reduce(0, Integer::sum);
        return String.valueOf(max);
    }

    private Character findDoubleItem(String line) {
        Set<Character> firstHalf = createSetOfChars(line.substring(0, line.length() / 2));
        Set<Character> secondHalf = createSetOfChars(line.substring(line.length() / 2));
        firstHalf.retainAll(secondHalf);
        if (firstHalf.size() != 1) {
            throw new RuntimeException("This should not happen");
        }
        return firstHalf.iterator().next();
    }

    private Set<Character> createSetOfChars(String text) {
        return text.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    private int determineScore(Character character) {
        if (Character.isUpperCase(character)) {
            return character - 65 + 27;
        }
        return character - 97 + 1;
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
