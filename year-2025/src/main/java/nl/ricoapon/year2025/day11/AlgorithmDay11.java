package nl.ricoapon.year2025.day11;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.DirectedGraph;

public class AlgorithmDay11 implements Algorithm {
    private DirectedGraph<String> graphOf(String input) {
        DirectedGraph<String> graph = new DirectedGraph<>();
        for (String line : input.split("\\r?\\n")) {
            String node = line.split(":")[0];
            graph.addNode(node);
            Stream.of(line.split(":")[1].trim().split(" ")).forEach(
                    target -> graph.addEdge(node, target));
        }
        return graph;
    }

    @Override
    public Object part1(String input) {
        var graph = graphOf(input);

        // BFS could in theory go on infinitely if there is a cycle, but that would also
        // mean the answer is infinite, which is probably not the case.
        Queue<String> queue = new LinkedList<>();
        queue.add("you");

        int nrOfPathsToOut = 0;
        while (!queue.isEmpty()) {
            String node = queue.poll();
            if ("out".equals(node)) {
                nrOfPathsToOut += 1;
            } else {
                queue.addAll(graph.getAdjacentNodes(node));
            }
        }

        return nrOfPathsToOut;
    }

    @Override
    public Object part2(String input) {
        return "x";
    }
}
