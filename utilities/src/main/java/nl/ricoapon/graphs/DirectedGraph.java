package nl.ricoapon.graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectedGraph<N> {
    private final Map<N, Set<N>> edges = new HashMap<>();

    public void addNode(N node){
        if (!edges.containsKey(node)) {
            edges.put(node, new HashSet<>());
        }
    }

    public void addEdge(N source, N target) {
        edges.get(source).add(target);
    }

    public Set<N> getAdjacentNodes(N node) {
        return edges.get(node);
    }

    public Set<N> getAllNodes() {
        return edges.keySet();
    }
}
