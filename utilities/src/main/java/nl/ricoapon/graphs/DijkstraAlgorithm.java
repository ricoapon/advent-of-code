package nl.ricoapon.graphs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraAlgorithm<N> {
    public int determineShortestPath(WeightedDirectedGraph<N> graph, N source, N target) {
        Map<N, Integer> distance = new HashMap<>();
        graph.getAllNodes().forEach(node -> distance.put(node, Integer.MAX_VALUE));
        distance.put(source, 0);

        PriorityQueue<N> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(source);
        Set<N> seen = new HashSet<>();
        seen.add(source);

        while (!queue.isEmpty()) {
            N top = queue.poll();

            for (N adjacent : graph.getAdjacentNodes(top)) {
                int currentDistance = graph.getWeight(top, adjacent) + distance.get(top);

                if (currentDistance < distance.get(adjacent)) {
                    distance.put(adjacent, currentDistance);
                }

                if (!seen.contains(adjacent)) {
                    queue.add(adjacent);
                    seen.add(adjacent);
                }
            }
        }

        return distance.get(target);
    }
}
