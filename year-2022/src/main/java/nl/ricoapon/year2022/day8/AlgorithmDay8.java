package nl.ricoapon.year2022.day8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import nl.ricoapon.Grid;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay8 implements Algorithm {
    private Grid<Integer> createGrid(String input) {
        List<List<Integer>> gridList = Arrays.stream(input.split("\r?\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(i -> Integer.valueOf(i)).toList())
                .toList();
        return new Grid<>(gridList);
    }

    private static class Coordinate {
        public final int x;
        public final int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Coordinate other = (Coordinate) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Coordinate [x=" + x + ", y=" + y + "]";
        }
    }

    private Set<Coordinate> findVisibleTreesWalkingFromEdge(Grid<Integer> trees, List<Coordinate> edge,
            UnaryOperator<Coordinate> nextCoordinate) {
        Set<Coordinate> visibleTrees = new HashSet<>();

        for (Coordinate coordinate : edge) {
            // The edge is always visible.
            visibleTrees.add(coordinate);

            // Walk from the edge towards the other edge.
            Integer previousHighestTree = trees.getCell(coordinate.x, coordinate.y);
            Coordinate currentTreeCoordinate = nextCoordinate.apply(coordinate);
            while (true) {
                Integer currentTree = trees.getCell(currentTreeCoordinate.x, currentTreeCoordinate.y);
                if (previousHighestTree < currentTree) {
                    // The current tree is visible.
                    visibleTrees.add(currentTreeCoordinate);
                    previousHighestTree = currentTree;
                }

                currentTreeCoordinate = nextCoordinate.apply(currentTreeCoordinate);

                // Note that we always have to walk the entire line! A tree can be visible,
                // while the others are not.
                // For example 112 has the first 1 and the last 2 visible, but the middle 1 is
                // not.

                // Ensure we stop when reaching the end.
                if (currentTreeCoordinate.x < 0 || currentTreeCoordinate.x >= trees.getSizeX() ||
                        currentTreeCoordinate.y < 0 || currentTreeCoordinate.y >= trees.getSizeY()) {
                    break;
                }
            }
        }

        return visibleTrees;
    }

    @Override
    public String part1(String input) {
        Grid<Integer> trees = createGrid(input);
        Set<Coordinate> visibleTrees = new HashSet<>();

        Set<Coordinate> visibleUpToDown = findVisibleTreesWalkingFromEdge(trees,
                IntStream.range(0, trees.getSizeX()).mapToObj(x -> new Coordinate(x, 0)).toList(),
                (c) -> new Coordinate(c.x, c.y + 1));
        Set<Coordinate> visibleDownToUp = findVisibleTreesWalkingFromEdge(trees,
                IntStream.range(0, trees.getSizeX()).mapToObj(x -> new Coordinate(x, trees.getSizeY() - 1)).toList(),
                (c) -> new Coordinate(c.x, c.y - 1));
        Set<Coordinate> visibleLeftToRight = findVisibleTreesWalkingFromEdge(trees,
                IntStream.range(0, trees.getSizeX()).mapToObj(y -> new Coordinate(0, y)).toList(),
                (c) -> new Coordinate(c.x + 1, c.y));
        Set<Coordinate> visibleRightToLeft = findVisibleTreesWalkingFromEdge(trees,
                IntStream.range(0, trees.getSizeX()).mapToObj(y -> new Coordinate(trees.getSizeX() - 1, y)).toList(),
                (c) -> new Coordinate(c.x - 1, c.y));

        visibleTrees.addAll(visibleUpToDown);
        visibleTrees.addAll(visibleDownToUp);
        visibleTrees.addAll(visibleLeftToRight);
        visibleTrees.addAll(visibleRightToLeft);

        return "" + visibleTrees.size();
    }

    private int calculateScenicScoreFromTree(Grid<Integer> trees, Coordinate treeHouse) {
        List<UnaryOperator<Coordinate>> directions = List.of(
                (c) -> new Coordinate(c.x, c.y + 1),
                (c) -> new Coordinate(c.x, c.y - 1),
                (c) -> new Coordinate(c.x + 1, c.y),
                (c) -> new Coordinate(c.x - 1, c.y));

        List<Integer> visibleTreesPerDirection = new ArrayList<>();
        int treeHouseValue = trees.getCell(treeHouse.x, treeHouse.y);

        for (UnaryOperator<Coordinate> direction : directions) {
            Coordinate next = direction.apply(treeHouse);

            // If the tree is an edge, the first step will already be out of bounds.
            // This means one of the values is 0, and thus the result of the entire function
            // is 0.
            if (next.x < 0 || next.x >= trees.getSizeX() ||
                    next.y < 0 || next.y >= trees.getSizeY()) {
                return 0;
            }

            int visibleTrees = 0;

            // Do stuff.
            while (true) {
                int currentTree = trees.getCell(next.x, next.y);
                visibleTrees++;
                if (currentTree >= treeHouseValue) {
                    break;
                }

                next = direction.apply(next);

                // Ensure we stop when reaching the end.
                if (next.x < 0 || next.x >= trees.getSizeX() ||
                        next.y < 0 || next.y >= trees.getSizeY()) {
                    break;
                }
            }

            visibleTreesPerDirection.add(visibleTrees);
        }

        int result = visibleTreesPerDirection.stream().reduce(1, (i1, i2) -> i1 * i2);
        return result;
    }

    @Override
    public String part2(String input) {
        Grid<Integer> trees = createGrid(input);
        int maxScenicScore = 0;
        for (int x = 0; x < trees.getSizeX(); x++) {
            for (int y = 0; y < trees.getSizeY(); y++) {
                int scenicScore = calculateScenicScoreFromTree(trees, new Coordinate(x, y));
                if (maxScenicScore < scenicScore) {
                    maxScenicScore = scenicScore;
                }
            }
        }

        return "" + maxScenicScore;
    }
}
