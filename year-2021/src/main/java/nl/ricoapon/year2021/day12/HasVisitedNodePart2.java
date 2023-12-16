package nl.ricoapon.year2021.day12;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HasVisitedNodePart2 implements HasVisitedNode {
    @Override
    public boolean hasVisited(String node, List<String> path) {
        Map<String, Long> smallCavesOccurrencesMap = path.stream()
                .filter(n -> n.toLowerCase().equals(n))
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        if (!smallCavesOccurrencesMap.containsKey(node)) {
            return false;
        }

        // Start and end are special nodes. We can only visit those nodes once.
        if ("start".equals(node) || "end".equals(node)) {
            return true;
        }

        // The small cave already occurs. Return true (= visited) if some small cave has already been visited twice.
        // If not, we can return false (= not visited) since we can visit that small cave again.
        return smallCavesOccurrencesMap.values().stream().anyMatch(l -> l == 2);
    }
}
