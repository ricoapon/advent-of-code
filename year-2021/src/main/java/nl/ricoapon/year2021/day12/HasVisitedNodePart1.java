package nl.ricoapon.year2021.day12;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HasVisitedNodePart1 implements HasVisitedNode {
    @Override
    public boolean hasVisited(String node, List<String> path) {
        Set<String> visitedSmallCaves = path.stream().filter(n -> n.toLowerCase().equals(n)).collect(Collectors.toSet());
        return visitedSmallCaves.contains(node);
    }
}
