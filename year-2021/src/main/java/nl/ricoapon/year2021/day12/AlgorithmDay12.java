package nl.ricoapon.year2021.day12;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.SimpleGraph;
import nl.ricoapon.graphs.SimplePath;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlgorithmDay12 implements Algorithm {
    private SimpleGraph<String> createGraph(String input) {
        List<Pair<String, String>> edges = Arrays.stream(input.split("\n"))
                .map(s -> new Pair<>(s.substring(0, s.indexOf("-")), s.substring(s.indexOf("-") + 1)))
                .toList();
        Set<String> nodes = edges.stream()
                .flatMap(pair -> Stream.of(pair.l(), pair.r()))
                .collect(Collectors.toSet());

        SimpleGraph<String> graph = new SimpleGraph<>();
        nodes.forEach(graph::addNode);
        edges.forEach(pair -> graph.addEdge(pair.l(), pair.r()));
        return graph;
    }

    @Override
    public String part1(String input) {
        SimpleGraph<String> graph = createGraph(input);
        PathCalculator pathCalculator = new PathCalculator(graph, new HasVisitedNodePart1());
        Set<SimplePath<String>> allPaths = pathCalculator.calculateAllPaths("start", "end");
        return String.valueOf(allPaths.size());
    }

    @Override
    public String part2(String input) {
        SimpleGraph<String> graph = createGraph(input);
        PathCalculator pathCalculator = new PathCalculator(graph, new HasVisitedNodePart2());
        Set<SimplePath<String>> allPaths = pathCalculator.calculateAllPaths("start", "end");
        return String.valueOf(allPaths.size());
    }
}
