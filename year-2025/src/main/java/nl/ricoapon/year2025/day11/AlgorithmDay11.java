package nl.ricoapon.year2025.day11;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
        var graph = graphOf(input);
        // This is never added as node, so to prevent NPE we add it ourselves.
        graph.addNode("out");

        // We know there is no cycle in the graph. So there is either a path from dac to
        // fft or fft to dac, not both. This means that we can just find the subpaths.
        // This method is slightly slower, but a simple one-liner. It works, because
        // one of the values will return 0.
        return (findNrOfPaths("svr", "fft", graph)
                * findNrOfPaths("fft", "dac", graph)
                * findNrOfPaths("dac", "out", graph))
                + (findNrOfPaths("svr", "dac", graph)
                        * findNrOfPaths("dac", "fft", graph)
                        * findNrOfPaths("fft", "out", graph));
    }

    private long findNrOfPaths(String start, String end, DirectedGraph<String> graph) {
        return traverse(start, end, new HashSet<>(), new HashMap<>(), graph);
    }

    private long traverse(String node, String target, Set<String> visited, Map<String, Long> scores,
            DirectedGraph<String> graph) {
        if (node.equals(target)) {
            return 1;
        }
        if (visited.contains(node)) {
            return 0;
        }
        if (scores.containsKey(node)) {
            return scores.get(node);
        }

        visited.add(node);
        long total = 0;
        for (String adjacentNode : graph.getAdjacentNodes(node)) {
            total += traverse(adjacentNode, target, visited, scores, graph);
        }
        visited.remove(node);
        scores.put(node, total);
        return total;
    }
}
