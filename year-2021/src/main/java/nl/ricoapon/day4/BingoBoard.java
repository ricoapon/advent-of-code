package nl.ricoapon.day4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class BingoBoard {
    private static class Cell {
        private final int i;
        private boolean marked;

        public Cell(int i) {
            this.i = i;
        }
    }

    private final List<List<Cell>> board;

    private BingoBoard(List<List<Cell>> board) {
        this.board = board;
    }

    public static BingoBoard of(String input) {
        List<List<Cell>> board = new ArrayList<>();
        for (String rowAsString : input.split("\n")) {
            List<Cell> row = Arrays.stream(rowAsString.split(" +"))
                    // We cannot identify all cases using regex (starting with whitespace for example).
                    // Just filter these cases to make it easy.
                    .filter(s -> s.length() > 0)
                    .map(Integer::parseInt)
                    .map(Cell::new)
                    .toList();
            board.add(row);
        }
        return new BingoBoard(board);
    }

    private int getValue(int x, int y) {
        return board.get(x).get(y).i;
    }

    private boolean isMarked(int x, int y) {
        return board.get(x).get(y).marked;
    }

    private void mark(int x, int y) {
        board.get(x).get(y).marked = true;
    }

    public void markDrawnNumbers(int drawnNumber) {
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                if (getValue(x, y) == drawnNumber) {
                    mark(x, y);
                }
            }
        }
    }

    public boolean hasBingo() {
        // Rows are easy.
        for (List<Cell> row : board) {
            if (row.stream().filter(c -> c.marked).count() == 5) {
                return true;
            }
        }

        // Columns slightly harder.
        for (int y = 0; y < board.size(); y++) {
            int finalY = y;
            if (IntStream.range(0, board.size())
                    .mapToObj(x -> isMarked(x, finalY))
                    .filter(x -> x)
                    .count() == 5) {
                return true;
            }
        }

        return false;
    }

    public int score(int previouslyDrawnNumber) {
        return board.stream()
                .flatMap(Collection::stream)
                .filter(c -> !c.marked)
                .mapToInt(c -> c.i)
                .sum() * previouslyDrawnNumber;
    }
}
