package nl.ricoapon.graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Graph implementation where the graph satisfies the following constraints:
 * <ul>
 * <li>Unweighted</li>
 * <li>Undirected</li>
 * <li>No self-loops or multiple edges</li>
 * </ul>
 * 
 * @param <N> The type of nodes.
 */
public class SimpleGraph<N> {
    private final Map<N, Set<N>> edges = new HashMap<>();

    public void addNode(N node) {
        if (edges.containsKey(node)) {
            return;
        }
        edges.put(node, new HashSet<>());
    }

    public void addEdge(N source, N target) {
        if (!containsNode(source) || !containsNode(target)) {
            return;
        }

        edges.get(source).add(target);
        edges.get(target).add(source);
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
