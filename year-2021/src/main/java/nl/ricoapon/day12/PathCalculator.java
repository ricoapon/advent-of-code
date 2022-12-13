package nl.ricoapon.day12;

import nl.ricoapon.graphs.SimpleGraph;
import nl.ricoapon.graphs.SimplePath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathCalculator {
    private final SimpleGraph<String> graph;
    private final HasVisitedNode hasVisitedNode;

    public PathCalculator(SimpleGraph<String> graph, HasVisitedNode hasVisitedNode) {
        this.graph = graph;
        this.hasVisitedNode = hasVisitedNode;
    }

    public Set<SimplePath<String>> calculateAllPaths(String startNode, String endNode) {
        Set<SimplePath<String>> allPaths = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        currentPath.add(startNode);
        dfs(startNode, endNode, currentPath, allPaths);
        return allPaths;
    }

    private void dfs(String startNode, String endNode, List<String> currentPath, Set<SimplePath<String>> allPaths) {
        if (startNode.equals(endNode)) {
            // We are at the end, so we found a path.
            allPaths.add(new SimplePath<String>(currentPath));
            return;
        }

        for (String node : graph.getAdjacentNodes(startNode)) {
            // Skip small caves we already visited.
            if (hasVisitedNode.hasVisited(node, currentPath)) {
                continue;
            }

            currentPath.add(node);
            dfs(node, endNode, currentPath, allPaths);
            // Nodes can occur twice in the path. Make sure we delete the last one.
            int lastIndex = currentPath.lastIndexOf(node);
            currentPath.remove(lastIndex);
        }
    }
}
