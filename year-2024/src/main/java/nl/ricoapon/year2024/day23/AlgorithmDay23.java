package nl.ricoapon.year2024.day23;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.SimpleGraph;

public class AlgorithmDay23 implements Algorithm {

    record Edge(String a, String b) {
        public static Edge of(String line) {
            var items = line.split("-");
            return new Edge(items[0], items[1]);
        }
    }

    // The entire set of connected components should all have the same size.
    private Set<ConnectedComponent> extendByOne(SimpleGraph<String> g, Set<ConnectedComponent> connectedComponents) {
        Set<ConnectedComponent> result = new HashSet<>();

        for (String node : g.getAllNodes()) {
            for (ConnectedComponent c : connectedComponents) {
                if (c.isAdjacentTo(node)) {
                    result.add(c.add(node));
                }
            }
        }

        return result;
    }

    @Override
    public Object part1(String input) {
        List<Edge> edges = Stream.of(input.split("\\r?\\n")).map(Edge::of).toList();
        SimpleGraph<String> g = new SimpleGraph<>();

        for (Edge edge : edges) {
            g.addNode(edge.a);
            g.addNode(edge.b);
            g.addEdge(edge.a, edge.b);
        }

        Set<ConnectedComponent> connectedComponentsSize2 = edges.stream()
                .map(e -> new ConnectedComponent(g, List.of(e.a, e.b))).collect(Collectors.toSet());
        return extendByOne(g, connectedComponentsSize2).stream()
                .filter(ConnectedComponent::containsNodeStartingWithT)
                .count();
    }

    @Override
    public Object part2(String input) {
        List<Edge> edges = Stream.of(input.split("\\r?\\n")).map(Edge::of).toList();
        SimpleGraph<String> g = new SimpleGraph<>();

        for (Edge edge : edges) {
            g.addNode(edge.a);
            g.addNode(edge.b);
            g.addEdge(edge.a, edge.b);
        }

        Set<ConnectedComponent> connectedComponentsOfEqualSize = edges.stream()
                .map(e -> new ConnectedComponent(g, List.of(e.a, e.b))).collect(Collectors.toSet());

        while (true) {
            var newCC = extendByOne(g, connectedComponentsOfEqualSize);
            if (newCC.isEmpty()) {
                break;
            }
            connectedComponentsOfEqualSize = newCC;
        }

        if (connectedComponentsOfEqualSize.size() != 1) {
            throw new RuntimeException("Cannot have more than one answer: " + connectedComponentsOfEqualSize);
        }

        var element = connectedComponentsOfEqualSize.iterator().next();

        return element.stream().sorted().collect(Collectors.joining(","));
    }
}
