package nl.ricoapon.day12;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.DijkstraAlgorithm;
import nl.ricoapon.graphs.WeightedDirectedGraph;

import java.util.List;

public class AlgorithmDay12 implements Algorithm {
    record Node(int value, boolean isStart, boolean isEnd){}

    private WeightedDirectedGraph<Pair<Coordinate2D, Node>> createGraph(String input) {
        GridWithCoordinates<Node> grid = GridWithCoordinates.ofCharacters(input, (c) -> {
            if (c == 'S') {
                return new Node(0, true, false);
            } else if (c == 'E') {
                return new Node(25, false, true);
            }
            return new Node(c - 97, false, false);
        });

        WeightedDirectedGraph<Pair<Coordinate2D, Node>> graph = new WeightedDirectedGraph<>();
        grid.stream().forEach(n -> {
            graph.addNode(n);
            grid.determineHorizontalAndVerticalAdjacentCoordinates(n.l()).forEach(coordinate -> {
                Pair<Coordinate2D, Node> adjacent = new Pair<>(coordinate, grid.getCell(coordinate));
                graph.addNode(adjacent);
                if (n.r().value() + 1 >= adjacent.r().value()) {
                    graph.addEdge(n, adjacent, 1);
                }
                if (n.r().value() <= adjacent.r().value() + 1) {
                    graph.addEdge(adjacent, n, 1);
                }
            });
        });
        return graph;
    }

    @Override
    public String part1(String input) {
        WeightedDirectedGraph<Pair<Coordinate2D, Node>> graph = createGraph(input);

        // Find start and end node.
        Pair<Coordinate2D, Node> start = graph.getAllNodes().stream().filter(p -> p.r().isStart).findFirst().orElseThrow();
        Pair<Coordinate2D, Node> end = graph.getAllNodes().stream().filter(p -> p.r().isEnd).findFirst().orElseThrow();

        // Use dijkstra.
        DijkstraAlgorithm<Pair<Coordinate2D, Node>> dijkstraAlgorithm = new DijkstraAlgorithm<>();
        int shortestPathLength = dijkstraAlgorithm.determineShortestPath(graph, start, end);

        return "" + shortestPathLength;
    }


    @Override
    public String part2(String input) {
        WeightedDirectedGraph<Pair<Coordinate2D, Node>> graph = createGraph(input);

        // Find start nodes and end node.
        List<Pair<Coordinate2D, Node>> startNodes = graph.getAllNodes().stream().filter(p -> p.r().value == 0).toList();
        Pair<Coordinate2D, Node> end = graph.getAllNodes().stream().filter(p -> p.r().isEnd).findFirst().orElseThrow();

        DijkstraAlgorithm<Pair<Coordinate2D, Node>> dijkstraAlgorithm = new DijkstraAlgorithm<>();
        int shortestPathLength = Integer.MAX_VALUE;
        for (Pair<Coordinate2D, Node> start : startNodes) {
            int path = dijkstraAlgorithm.determineShortestPath(graph, start, end);
            if (shortestPathLength > path) {
                shortestPathLength = path;
            }
        }

        return "" + shortestPathLength;
    }
}

