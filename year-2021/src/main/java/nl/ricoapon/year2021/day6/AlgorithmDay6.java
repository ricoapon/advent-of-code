package nl.ricoapon.year2021.day6;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {
    @Override
    public Object part1(String input) {
        return calculateNrOfFishAfterXDays(input, 80);
    }

    @Override
    public Object part2(String input) {
        return calculateNrOfFishAfterXDays(input, 256);
    }

    private String calculateNrOfFishAfterXDays(String input, int days) {
        // Keep track of how many we have of each day.
        Map<Integer, Long> state = Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int day = 1; day <= days; day++) {
            Map<Integer, Long> newState = new HashMap<>();
            for (int i = 0; i <= 7; i++) {
                newState.put(i, state.getOrDefault(i + 1, 0L));
            }

            newState.put(6, newState.getOrDefault(6, 0L) + state.getOrDefault(0, 0L));
            newState.put(8, state.getOrDefault(0, 0L));

            state = newState;
        }

        return String.valueOf(state.values().stream().mapToLong(x -> x).sum());

    }
}
