package nl.ricoapon.year2023.day10;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Directed graph.
 * @param <N> The type of nodes.
 */
public class DirectedGraph<N> {
    private final Map<N, Set<N>> edges = new HashMap<>();

    public void addNode(N node){
        edges.put(node, new HashSet<>());
    }

    public void addEdge(N source, N target) {
        if (!containsNode(source) || !containsNode(target)) {
            return;
        }
        
        edges.get(source).add(target);
    }

    public boolean containsNode(N node) {
        return edges.containsKey(node);
    }

    public Set<N> getAdjacentNodes(N node) {
        return edges.get(node);
    }

    public Set<N> getAllNodes() {
        return edges.keySet();
    }
}
