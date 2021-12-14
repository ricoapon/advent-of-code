package nl.ricoapon.day14;

import nl.ricoapon.framework.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmDay14 implements Algorithm {
    private List<PolymerTemplate.Pair> createPairsFromInitialTemplate(String input) {
        List<PolymerTemplate.Pair> list = new ArrayList<>();

        for (int i = 0; i < input.length() - 1; i++) {
            String left = input.substring(i, i + 1);
            String right = input.substring(i + 1, i + 2);
            list.add(new PolymerTemplate.Pair(left, right));
        }

        return list;
    }

    private final static Pattern PAIR_INSERTION_PATTERN = Pattern.compile("(\\w)(\\w) -> (\\w)");

    private Map<PolymerTemplate.Pair, List<PolymerTemplate.Pair>> createPairInsertionRules(String input) {
        List<String> pairInsertionRules = Arrays.stream(input.split("\n")).toList();
        Map<PolymerTemplate.Pair, List<PolymerTemplate.Pair>> map = new HashMap<>();

        for (String pairInsertionRule : pairInsertionRules) {
            Matcher matcher = PAIR_INSERTION_PATTERN.matcher(pairInsertionRule);
            if (!matcher.matches()) {
                throw new RuntimeException("Pair insertion pattern doesn't match for input: '" + pairInsertionRule + "'");
            }
            String left = matcher.group(1);
            String right = matcher.group(2);
            String insertion = matcher.group(3);

            map.put(new PolymerTemplate.Pair(left, right),
                    List.of(new PolymerTemplate.Pair(left, insertion), new PolymerTemplate.Pair(insertion, right)));
        }

        return map;
    }

    private PolymerTemplate createPolymerTemplate(String input) {
        String[] inputAsParts = input.split("\n\n");
        return new PolymerTemplate(
                createPairsFromInitialTemplate(inputAsParts[0]),
                createPairInsertionRules(inputAsParts[1])
        );
    }

    private long doAlgorithm(String input, int nrOfSteps) {
        PolymerTemplate polymerTemplate = createPolymerTemplate(input);

        for (int step = 0; step < nrOfSteps; step++) {
            polymerTemplate.insertPairs();
        }

        Map<String, Long> occurrenceMap = polymerTemplate.createOccurrenceMap();

        long minValue = occurrenceMap.values().stream().mapToLong(l -> l).min().orElseThrow();
        long maxValue = occurrenceMap.values().stream().mapToLong(l -> l).max().orElseThrow();

        return maxValue - minValue;
    }


    @Override
    public String part1(String input) {
        return String.valueOf(doAlgorithm(input, 10));
    }

    @Override
    public String part2(String input) {
        return String.valueOf(doAlgorithm(input, 40));
    }
}
