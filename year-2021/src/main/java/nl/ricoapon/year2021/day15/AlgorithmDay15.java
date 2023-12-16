package nl.ricoapon.year2021.day15;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.DijkstraAlgorithm;
import nl.ricoapon.graphs.WeightedDirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmDay15 implements Algorithm {
    private WeightedDirectedGraph<Coordinate2D> createGraph(List<List<Integer>> weights) {
        GridWithCoordinates<Integer> grid = new GridWithCoordinates<>(weights);

        WeightedDirectedGraph<Coordinate2D> graph = new WeightedDirectedGraph<>();
        grid.stream().forEach(pair -> {
            Coordinate2D coordinate = pair.l();
            Integer weight = pair.r();

            graph.addNode(coordinate);
            grid.determineHorizontalAndVerticalAdjacentCoordinates(coordinate).forEach(adjacentCoordinate -> {
                // To avoid a second loop, we add the nodes in case they haven't been added yet.
                graph.addNode(adjacentCoordinate);
                graph.addEdge(adjacentCoordinate, coordinate, weight);
            });
        });

        return graph;
    }

    private int calculateShortestDistance(List<List<Integer>> weights) {
        WeightedDirectedGraph<Coordinate2D> graph = createGraph(weights);
        DijkstraAlgorithm<Coordinate2D> dijkstraAlgorithm = new DijkstraAlgorithm<>();

        Coordinate2D startingCoordinate = new Coordinate2D(0, 0);
        Coordinate2D destinationCoordinate = new Coordinate2D(weights.size() - 1, weights.get(0).size() - 1);

        return dijkstraAlgorithm.determineShortestPath(graph, startingCoordinate, destinationCoordinate);
    }

    @Override
    public String part1(String input) {
        List<List<Integer>> weights = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(Integer::parseInt).toList())
                .toList();


        return String.valueOf(calculateShortestDistance(weights));
    }

    @Override
    public String part2(String input) {
        List<List<Integer>> weights = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(Integer::parseInt).toList())
                .toList();

        // Expand weights to the right.
        List<List<Integer>> quintupledWeights = new ArrayList<>();
        for (List<Integer> weightRow : weights) {
            quintupledWeights.add(quintupledRow(weightRow));
        }

        // Expand weights to the bottom.
        List<List<Integer>> finalWeights = new ArrayList<>();
        for (int increment = 0; increment < 5; increment++) {
            for (List<Integer> quintupledWeightRow : quintupledWeights) {
                finalWeights.add(incrementRow(quintupledWeightRow, increment));
            }
        }

        return String.valueOf(calculateShortestDistance(finalWeights));
    }

    private int calculateNewNumber(int number, int increment) {
        number += increment;
        if (number > 9) {
            number -= 9;
        }
        return  number;
    }

    private List<Integer> quintupledRow(List<Integer> row) {
        List<Integer> quintupledRow = new ArrayList<>();
        for (int increment = 0; increment < 5; increment++) {
            for (Integer integer : row) {
                quintupledRow.add(calculateNewNumber(integer, increment));
            }
        }
        return quintupledRow;
    }

    private List<Integer> incrementRow(List<Integer> row, int increment) {
        return row.stream().map(i -> calculateNewNumber(i, increment)).toList();
    }
}
