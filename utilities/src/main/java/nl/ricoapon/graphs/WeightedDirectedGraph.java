package nl.ricoapon.graphs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeightedDirectedGraph<N> {
    private final Map<N, Map<N, Integer>> weights = new HashMap<>();

    public void addNode(N node){
        if (!weights.containsKey(node)) {
            weights.put(node, new HashMap<>());
        }
    }

    public void addEdge(N source, N target, int weight) {
        weights.get(source).put(target, weight);
    }

    public Set<N> getAdjacentNodes(N node) {
        return weights.get(node).keySet();
    }

    public int getWeight(N source, N target) {
        return weights.get(source).get(target);
    }

    public Set<N> getAllNodes() {
        return weights.keySet();
    }
}
