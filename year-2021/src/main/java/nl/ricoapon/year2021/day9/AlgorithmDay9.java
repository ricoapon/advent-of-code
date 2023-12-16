package nl.ricoapon.year2021.day9;

import nl.ricoapon.Grid;
import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmDay9 implements Algorithm {
    private static class Cell {
        private final Integer height;

        public Cell(Integer height) {
            this.height = height;
        }
    }

    private Grid<Cell> createGrid(String input) {
        List<List<Cell>> grid = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(i -> new Cell(Integer.parseInt(i))).toList())
                .toList();
        return new Grid<>(grid);
    }

    @Override
    public String part1(String input) {
        Grid<Cell> grid = createGrid(input);
        int sum = grid.stream()
                .filter(cell -> isLowPoint(cell, grid.determineHorizontalAndVerticalAdjacentCells(cell)))
                .mapToInt(cell -> cell.height + 1)
                .sum();
        return String.valueOf(sum);
    }

    private boolean isLowPoint(Cell cell, Set<Cell> adjacentCells) {
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell.height <= cell.height) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String part2(String input) {
        Grid<Cell> grid = createGrid(input);
        List<Cell> lowPoints = grid.stream()
                .filter(cell -> isLowPoint(cell, grid.determineHorizontalAndVerticalAdjacentCells(cell)))
                .toList();

        List<Integer> basinSizes = lowPoints.stream().map(lowPoint -> calculateSizeBasin(grid, lowPoint))
                .sorted(Comparator.reverseOrder()).toList();

        return String.valueOf(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }

    private int calculateSizeBasin(Grid<Cell> grid, Cell lowPoint) {
        Set<Cell> basin = new HashSet<>();
        recursivelyCalculateBasin(basin, grid, lowPoint);
        return basin.size();
    }

    private void recursivelyCalculateBasin(Set<Cell> result, Grid<Cell> grid, Cell startingPoint) {
        if (result.contains(startingPoint)) {
            return;
        }

        result.add(startingPoint);

        Set<Cell> adjacentCells = grid.determineHorizontalAndVerticalAdjacentCells(startingPoint);
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell.height == 9) {
                continue;
            }

            if (result.contains(adjacentCell)) {
                continue;
            }

            recursivelyCalculateBasin(result, grid, adjacentCell);
        }
    }
}
