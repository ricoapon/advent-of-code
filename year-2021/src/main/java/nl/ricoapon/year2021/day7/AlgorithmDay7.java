package nl.ricoapon.year2021.day7;

import java.util.Arrays;
import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay7 implements Algorithm {
    @Override
    public Object part1(String input) {
        List<Integer> sortedPositions = Arrays.stream(input.split(",")).map(Integer::parseInt).sorted().toList();

        // Subtract one since lists start at 0.
        int medianPosition = sortedPositions.size() / 2 - 1;
        int median;
        if (sortedPositions.size() % 2 == 0) {
            // We can assume that the input list is sufficiently nice that we don't need to think about rounding.
            median = (sortedPositions.get(medianPosition) + sortedPositions.get(medianPosition + 1)) / 2;
        } else {
            median = sortedPositions.get(medianPosition);
        }

        int finalMedian = median;
        int cost = sortedPositions.stream().mapToInt(x -> Math.abs(x - finalMedian)).sum();
        return String.valueOf(cost);
    }

    @Override
    public Object part2(String input) {
        List<Integer> positions = Arrays.stream(input.split(",")).map(Integer::parseInt).toList();

        double average = positions.stream().mapToDouble(x -> x).average().orElseThrow();

        // The average will be the correct result, but this is not a round integer.
        // Try both roundings and see which one is the best. We cannot say this for sure beforehand.
        int finalAverageRoundedDown = (int) Math.floor(average);
        int finalAverageRoundedUp = (int) Math.ceil(average);
        int costForRoundedDown = positions.stream().mapToInt(x -> {
            int a = Math.abs(x - finalAverageRoundedDown);
            return a * (a + 1) / 2;
        }).sum();
        int costForRoundedUp = positions.stream().mapToInt(x -> {
            int a = Math.abs(x - finalAverageRoundedUp);
            return a * (a + 1) / 2;
        }).sum();
        return String.valueOf(Math.min(costForRoundedDown, costForRoundedUp));
    }
}
