package nl.ricoapon.day9;

import nl.ricoapon.framework.Algorithm;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmDay9 implements Algorithm {
    @Override
    public String part1(String input) {
        Board board = Board.of(input);
        int sum = board.stream()
                .filter(cell -> isLowPoint(cell, board.determineAdjacentCells(cell)))
                .mapToInt(cell -> cell.height() + 1)
                .sum();
        return String.valueOf(sum);
    }

    private boolean isLowPoint(Cell cell, List<Cell> adjacentCells) {
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell.height() <= cell.height()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String part2(String input) {
        Board board = Board.of(input);
        List<Cell> lowPoints = board.stream()
                .filter(cell -> isLowPoint(cell, board.determineAdjacentCells(cell)))
                .toList();

        List<Integer> basinSizes = lowPoints.stream().map(lowPoint -> calculateSizeBasin(board, lowPoint))
                .sorted(Comparator.reverseOrder()).toList();

        return String.valueOf(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }

    private int calculateSizeBasin(Board board, Cell lowPoint) {
        Set<Cell> basin = new HashSet<>();
        recursivelyCalculateBasin(basin, board, lowPoint);
        return basin.size();
    }

    private void recursivelyCalculateBasin(Set<Cell> result, Board board, Cell startingPoint) {
        if (result.contains(startingPoint)) {
            return;
        }

        result.add(startingPoint);

        List<Cell> adjacentCells = board.determineAdjacentCells(startingPoint);
        for (Cell adjacentCell : adjacentCells) {
            if (adjacentCell.height() == 9) {
                continue;
            }

            if (result.contains(adjacentCell)) {
                continue;
            }

            recursivelyCalculateBasin(result, board, adjacentCell);
        }
    }
}
