package nl.ricoapon.year2023.day12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay12 implements Algorithm {
    private record State(String arrangement, List<Integer> validation,
            boolean previousCharWasDamagedSpring) {
    }

    private final Map<State, Long> memoization = new HashMap<>();

    private long recursivelyFindAllSolutionsWithMemoization(String arrangement, List<Integer> validation,
            boolean previousCharWasDamagedSpring) {
        State state = new State(arrangement, validation, previousCharWasDamagedSpring);
        if (memoization.containsKey(state)) {
            return memoization.get(state);
        }

        long result = recursivelyFindAllSolutions(arrangement, validation, previousCharWasDamagedSpring);
        memoization.put(state, result);
        return result;
    }

    private long recursivelyFindAllSolutions(String arrangement, List<Integer> validation,
            boolean previousCharWasDamagedSpring) {

        if (arrangement.length() == 0) {
            // It is only valid if we don't have any more groups left in the validation
            // list.
            if (validation.isEmpty() || (validation.size() == 1 && validation.getFirst() == 0)) {
                return 1;
            }
            return 0;
        }

        if (arrangement.startsWith(".")) {
            if (previousCharWasDamagedSpring && validation.getFirst() != 0) {
                return 0;
            }

            List<Integer> newValidation = validation;
            if (previousCharWasDamagedSpring && validation.getFirst() == 0) {
                newValidation = new ArrayList<>(validation);
                newValidation.remove(0);
            }

            return recursivelyFindAllSolutionsWithMemoization(arrangement.substring(1), newValidation, false);
        }

        if (arrangement.startsWith("#")) {
            if (validation.isEmpty()) {
                return 0;
            }

            int groupLength = validation.getFirst();
            if (groupLength <= 0) {
                return 0;
            }
            List<Integer> newValidation = new ArrayList<>(validation);
            newValidation.set(0, groupLength -= 1);

            return recursivelyFindAllSolutionsWithMemoization(arrangement.substring(1), newValidation, true);
        }

        // We found a wildcard. So we can have two options.
        return recursivelyFindAllSolutionsWithMemoization("#" + arrangement.substring(1), validation,
                previousCharWasDamagedSpring) +
                recursivelyFindAllSolutionsWithMemoization("." + arrangement.substring(1), validation,
                        previousCharWasDamagedSpring);
    }

    private long findNrOfSolutions(String line) {
        String arrangementWithWildcards = line.split(" ")[0];
        List<Integer> validation = Stream.of(line.split(" ")[1].split(",")).map(Integer::valueOf).toList();
        return recursivelyFindAllSolutionsWithMemoization(arrangementWithWildcards, validation, false);
    }

    private long findNrOfSolutionsPart2(String line) {
        String arrangementWithWildcards = line.split(" ")[0] + "?"
                + line.split(" ")[0] + "?"
                + line.split(" ")[0] + "?"
                + line.split(" ")[0] + "?"
                + line.split(" ")[0];
        List<Integer> validation = Collections
                .nCopies(5, Stream.of(line.split(" ")[1].split(",")).map(Integer::valueOf).toList()).stream()
                .flatMap(l -> l.stream()).toList();
        return recursivelyFindAllSolutionsWithMemoization(arrangementWithWildcards, validation, false);
    }

    @Override
    public Object part1(String input) {
        return Stream.of(input.split("\\r?\\n"))
                .mapToLong(this::findNrOfSolutions)
                .sum();
    }

    @Override
    public Object part2(String input) {
        return Stream.of(input.split("\\r?\\n"))
                .mapToLong(this::findNrOfSolutionsPart2)
                .sum();
    }
}
