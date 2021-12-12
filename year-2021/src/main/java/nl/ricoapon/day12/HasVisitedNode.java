package nl.ricoapon.day12;

import java.util.List;

@FunctionalInterface
public interface HasVisitedNode {
    boolean hasVisited(String node, List<String> path);
}
