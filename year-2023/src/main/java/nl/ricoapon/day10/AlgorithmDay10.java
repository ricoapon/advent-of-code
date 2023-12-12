package nl.ricoapon.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay10 implements Algorithm {

    record Graph(DirectedGraph<Coordinate2D> graph, Coordinate2D startingPoint, int maxX, int maxY) {

        public static Graph of(String input) {
            DirectedGraph<Coordinate2D> graph = new DirectedGraph<>();
            GridWithCoordinates<String> grid = GridWithCoordinates.ofString(input, s -> s);

            // Add all the coordinates as nodes.
            for (Coordinate2D c : grid.stream().map(p -> p.l()).toList()) {
                graph.addNode(c);
            }

            // Now connect all the nodes.
            Coordinate2D startingNode = null;
            for (Pair<Coordinate2D, String> p : grid.stream().toList()) {
                Coordinate2D c = p.getL();
                String symbol = p.getR();
                if ("|".equals(symbol)) {
                    graph.addEdge(c, north(c));
                    graph.addEdge(c, south(c));
                } else if ("-".equals(symbol)) {
                    graph.addEdge(c, west(c));
                    graph.addEdge(c, east(c));
                } else if ("L".equals(symbol)) {
                    graph.addEdge(c, north(c));
                    graph.addEdge(c, east(c));
                } else if ("J".equals(symbol)) {
                    graph.addEdge(c, north(c));
                    graph.addEdge(c, west(c));
                } else if ("7".equals(symbol)) {
                    graph.addEdge(c, south(c));
                    graph.addEdge(c, west(c));
                } else if ("F".equals(symbol)) {
                    graph.addEdge(c, south(c));
                    graph.addEdge(c, east(c));
                } else if ("S".equals(symbol)) {
                    startingNode = c;
                }
            }

            // Now that all edges are configured, we know how S is connected by looking at
            // nodes that point to S.
            for (Coordinate2D adjacent : startingNode.getHorizontalAndVerticalAdjacent(grid.getSizeX(),
                    grid.getSizeY())) {
                if (graph.getAdjacentNodes(adjacent).contains(startingNode)) {
                    graph.addEdge(startingNode, adjacent);
                }
            }

            return new Graph(graph, startingNode, grid.getSizeX(), grid.getSizeY());
        }

        // The x value is the vertical axis, and y the horizontal (not nice, I know).
        private static Coordinate2D north(Coordinate2D c) {
            return new Coordinate2D(c.x() - 1, c.y());
        }

        private static Coordinate2D south(Coordinate2D c) {
            return new Coordinate2D(c.x() + 1, c.y());
        }

        private static Coordinate2D west(Coordinate2D c) {
            return new Coordinate2D(c.x(), c.y() - 1);
        }

        private static Coordinate2D east(Coordinate2D c) {
            return new Coordinate2D(c.x(), c.y() + 1);
        }
    }

    @Override
    public String part1(String input) {
        Graph graph = Graph.of(input);

        // We can find any path from S to S, because we know there is only a single loop
        // that does this.
        List<Coordinate2D> path = calculatePath(graph);

        // The path output will go from S to S, including S twice.
        int result = path.size() / 2;

        return String.valueOf(result);
    }

    private List<Coordinate2D> calculatePath(Graph graph) {
        List<Coordinate2D> path = new ArrayList<>();
        path.add(graph.startingPoint());

        Coordinate2D current = graph.startingPoint();
        while (true) {
            Optional<Coordinate2D> next = graph.graph().getAdjacentNodes(current).stream()
                    .filter(c -> !path.contains(c)).findAny();

            if (next.isEmpty()) {
                break;
            }

            path.add(next.get());
            current = next.get();
        }

        return path;
    }

    @Override
    public String part2(String input) {
        // Move from left to right. If you come across a path node, you are inside.
        // If you find another path node, you are outside. Mark any nodes half-inside.
        // Then repeat from top to bottom, same algorithm. If you marked nodes
        // half-inside twice, it is inside.

        return "x";
    }
}
