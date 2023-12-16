package nl.ricoapon.year2021.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PolymerTemplate {
    /**
     * To avoid having to write {@code Pair&gt;String, String>} every time I create a separate class with this filled in.
     * Note that the utility class {@link nl.ricoapon.Pair} is a record and cannot be extended.
     */
    record Pair(String left, String right) {
    }

    private final String startingValue;
    private final String endingValue;

    private final Map<Pair, Long> pairs;
    private final Map<Pair, List<Pair>> pairInsertionRules;

    public PolymerTemplate(List<Pair> pairList, Map<Pair, List<Pair>> pairInsertionRules) {
        // Using Collectors.toMap() doesn't guarantee mutability. It is actually a mutable HashMap, but to be safe we create our own.
        pairs = new HashMap<>();
        pairList.forEach(pair -> pairs.put(pair, 1L));

        startingValue = pairList.get(0).left;
        endingValue = pairList.get(pairList.size() - 1).right;

        this.pairInsertionRules = pairInsertionRules;
    }

    public void insertPairs() {
        // Create a copy of all the existing pairs, so we can loop through the existing pairs and still mutate the pairs map.
        Map<Pair, Long> pairsBeforeInsertion = new HashMap<>(pairs);

        for (Pair pair : pairsBeforeInsertion.keySet()) {
            // Pairs that do not split will just stay as is.
            if (!pairInsertionRules.containsKey(pair)) {
                continue;
            }

            // Insertion is the same as removing the current pair and adding the newly created pairs.
            // Note: this split occurs as often as the pair occurred in the map before starting the split.
            long pairOccurrenceBeforeInsertion = pairsBeforeInsertion.get(pair);
            pairs.put(pair, pairs.get(pair) - pairOccurrenceBeforeInsertion);
            pairInsertionRules.get(pair).forEach(
                    newPair -> pairs.put(newPair, pairs.getOrDefault(newPair, 0L) + pairOccurrenceBeforeInsertion)
            );
        }

        // We don't want pairs that do not occur trigger again. So we remove any pairs that have occurrence number zero.
        List<Pair> pairsToBeRemoved = pairs.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .toList();

        for (Pair pair : pairsToBeRemoved) {
            pairs.remove(pair);
        }
    }

    public Map<String, Long> createOccurrenceMap() {
        Set<String> possibleStrings = pairs.keySet().stream()
                .flatMap(pair -> Stream.of(pair.left, pair.right))
                .collect(Collectors.toSet());

        return  possibleStrings.stream()
                .collect(Collectors.toMap(s -> s, this::countOccurrenceSingleString));
    }

    private long countOccurrenceSingleString(String s) {
        long totalOccurrences = pairs.entrySet().stream()
                .mapToLong(entry -> {
                    long occurrence = 0;
                    if (entry.getKey().left.equals(s)) {
                        occurrence += entry.getValue();
                    }
                    if (entry.getKey().right.equals((s))) {
                        occurrence += entry.getValue();
                    }
                    return occurrence;
                })
                .sum();

        long occurrencesAsLeftPair = pairs.entrySet().stream()
                .filter(entry -> entry.getKey().left.equals(s))
                .mapToLong(Map.Entry::getValue)
                .sum();
        long occurrencesAsRightPair = pairs.entrySet().stream()
                .filter(entry -> entry.getKey().right.equals(s))
                .mapToLong(Map.Entry::getValue)
                .sum();

        long bothEnds = (startingValue.equals(s) && endingValue.equals(s)) ? 1 : 0;

        return totalOccurrences - Math.min(occurrencesAsLeftPair, occurrencesAsRightPair) + bothEnds;
    }
}
