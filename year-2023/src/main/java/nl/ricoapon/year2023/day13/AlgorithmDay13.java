package nl.ricoapon.year2023.day13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay13 implements Algorithm {
    @Override
    public Object part1(String input) {
        List<Pattern> patterns = Pattern.of(input);
        int total = 0;
        for (Pattern p : patterns) {
            int rowScore = determineReflectionScore(p.rows());
            if (rowScore > 0) {
                total += 100 * rowScore;
                continue;
            }

            int columnScore = determineReflectionScore(p.columns());
            if (columnScore < 0) {
                throw new RuntimeException();
            }
            total += columnScore;
        }

        return total;
    }

    private int determineReflectionScore(List<Pattern.Row> rows) {
        // Create a list of all the indexes of each row.
        // No clue how to do this with streams! Mainly because we have duplicates, so we
        // cannot get the index based on the object.
        Map<Pattern.Row, List<Integer>> rowIndexes = new HashMap<>();
        for (int i = 0; i < rows.size(); i++) {
            Pattern.Row row = rows.get(i);
            if (!rowIndexes.containsKey(row)) {
                rowIndexes.put(row, new ArrayList<>());
            }
            rowIndexes.get(row).add(i);
        }

        // We ony need to check symmetries if the indexes are sequential.
        List<Integer> symmetryIndexes = rowIndexes.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .flatMap(e -> e.getValue().stream().filter(i -> e.getValue().contains(i + 1)))
                .toList();

        for (int symmetry : symmetryIndexes) {
            if (isSymmetryLine(rows, symmetry)) {
                // I assume there is only one symmetry line, so we can return any result.
                return symmetry + 1;
            }
        }

        // Cannot throw exception, since it could also be the other reflection score
        // (row/column) that has a value.
        return -1;
    }

    private boolean isSymmetryLine(List<Pattern.Row> rows, int symmetry) {
        for (int i = 0; i < rows.size(); i++) {
            int mirrorIndex = 2 * symmetry + 1 - i;
            if (mirrorIndex >= rows.size() || mirrorIndex < 0) {
                continue;
            }

            if (!rows.get(i).equals(rows.get(mirrorIndex))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object part2(String input) {
        List<Pattern> patterns = Pattern.of(input);
        int total = 0;
        for (Pattern p : patterns) {
            int rowScore = determineReflectionScorePart2(p.rows());
            if (rowScore > 0) {
                total += 100 * rowScore;
                continue;
            }

            int columnScore = determineReflectionScorePart2(p.columns());
            if (columnScore < 0) {
                throw new RuntimeException();
            }
            total += columnScore;
        }

        return total;
    }

    private int determineReflectionScorePart2(List<Pattern.Row> rows) {
        // Just loop through all possible reflection lines.
        for (int i = 0; i < rows.size() - 1; i++) {
            int rowScore = compareScore(rows, i);
            if (rowScore == 1) {
                return i + 1;
            }
        }
        return -1;
    }

    private int compareScore(List<Pattern.Row> rows, int symmetry) {
        // Score counts how many differences we have in total. If it is 0, we found an actual symmetry. If it is 1, we found a smudge symmetry.
        // Higher than 1 is not a symmetry.
        int score = 0;
        for (int i = 0; i <= symmetry; i++) {
            int mirrorIndex = 2 * symmetry + 1 - i;
            if (mirrorIndex >= rows.size() || mirrorIndex < 0) {
                continue;
            }

            score += rows.get(i).compare(rows.get(mirrorIndex));
            if (score >= 2) {
                return -1;
            }
        }
        return score;
    }
}
