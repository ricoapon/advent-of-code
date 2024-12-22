package nl.ricoapon.year2024.day19;

import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmDay19 implements Algorithm {

    private long calculateCombinations(String towel, List<String> patterns, Map<String, Long> cache) {
        if (cache.containsKey(towel)) {
            return cache.get(towel);
        }

        // If we have nothing left, we have filled the entire towel with correct patterns. So we have 1.
        if (towel.isEmpty()) {
            return 1;
        }

        var result = patterns.stream().filter(towel::startsWith)
                .mapToLong(p -> calculateCombinations(towel.substring(p.length()), patterns, cache))
                .sum();
        cache.put(towel, result);
        return result;
    }

    private List<String> determinePatternsFromInput(String input) {
        return Arrays.asList(input.split("\\r?\\n\\r?\\n")[0].split(", "));
    }

    private List<String> determineTowelsToMatchFromInput(String input) {
        return Arrays.asList(input.split("\\r?\\n\\r?\\n")[1].split("\\r?\\n"));
    }

    @Override
    public Object part1(String input) {
        List<String> patterns = determinePatternsFromInput(input);
        List<String> towelsToMatch = determineTowelsToMatchFromInput(input);
        Map<String, Long> cache = new HashMap<>();

        return towelsToMatch.stream().filter(p -> calculateCombinations(p, patterns, cache) > 0).count();
    }

    @Override
    public Object part2(String input) {
        List<String> patterns = determinePatternsFromInput(input);
        List<String> towelsToMatch = determineTowelsToMatchFromInput(input);
        Map<String, Long> cache = new HashMap<>();

        return towelsToMatch.stream().mapToLong(p -> calculateCombinations(p, patterns, cache)).sum();
    }
}
