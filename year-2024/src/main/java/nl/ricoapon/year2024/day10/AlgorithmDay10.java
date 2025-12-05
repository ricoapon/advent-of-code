package nl.ricoapon.year2024.day10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import nl.ricoapon.Grid;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay10 implements Algorithm {
    private static class Cell {
        public final int height;

        Cell(int height) {
            this.height = height;
        }
    }

    @Override
    public Object part1(String input) {
        Grid<Cell> grid = Grid.ofString(input, (s) -> new Cell(Integer.valueOf(s)));
        return grid.stream().filter(c -> c.height == 0)
                .mapToInt(c -> determineNrOfHikingTrails(c, grid, true))
                .peek(System.out::println)
                .sum();
    }

    private int determineNrOfHikingTrails(Cell trailhead, Grid<Cell> grid,
            boolean isPart1) {
        // We use breadth-first-search, that seemed like the easiest way.
        // In part 1, we need to count the unique number of ends that we reach. If we
        // use a set, then we make sure that each end is counted once (even if multiple
        // paths reach it).
        // In part 2, we count the total number of paths. So we store each end that we
        // reach. Each different path will end up with a different end.
        Collection<Cell> reachedEnds;
        if (isPart1) {
            reachedEnds = new HashSet<>();
        } else {
            reachedEnds = new ArrayList<>();
        }
        Queue<Cell> queue = new LinkedList<>();
        queue.add(trailhead);
        while (!queue.isEmpty()) {
            var node = queue.poll();
            if (node.height == 9) {
                reachedEnds.add(node);
            } else {
                queue.addAll(getAdjacentCellsOneHeightHigher(node, grid));
            }
        }

        return reachedEnds.size();
    }

    private List<Cell> getAdjacentCellsOneHeightHigher(Cell node, Grid<Cell> grid) {
        return grid.determineHorizontalAndVerticalAdjacentCells(node).stream()
                .filter(c -> c.height == node.height + 1)
                .toList();
    }

    @Override
    public Object part2(String input) {
        Grid<Cell> grid = Grid.ofString(input, (s) -> new Cell(Integer.valueOf(s)));
        return grid.stream().filter(c -> c.height == 0)
                .mapToInt(c -> determineNrOfHikingTrails(c, grid, false))
                .peek(System.out::println)
                .sum();
    }
}
