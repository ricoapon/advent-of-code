package nl.ricoapon.year2024.day2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay2 implements Algorithm {

    private List<Integer> parseLine(String line) {
        return Stream.of(line.split(" ")).map(Integer::parseInt).toList();
    }

    private List<List<Integer>> parseInput(String input) {
        return Stream.of(input.split("\\r?\\n")).map(this::parseLine).toList();
    }

    private boolean isSafeLinePart1(List<Integer> line) {
        int sign = Math.round(Math.signum(line.get(0) - line.get(1)));
        for (int i = 0; i < line.size() - 1; i++) {
            var diff = line.get(i) - line.get(i+1);

            if (diff * sign < 0) {
                return false;
            }

            if (Math.abs(diff) > 3 || Math.abs(diff) == 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isSafeLinePart2(List<Integer> line) {
        // We don't need to do anything smart yet for this exercise. So we just brute force it.
        // We could do it smarter by only removing an element if that specific element is causing the problem.
        // But then we would also need to take into account that removing the first or second element can make
        // the line suddenly change ascending/descending. A bit tricky, so we keep it simple.
        for (int i = 0; i < line.size(); i++) {
            List<Integer> lineWithIRemoved = new ArrayList<>(line);
            // Remove by index, not the object!
            lineWithIRemoved.remove(i);

            if (isSafeLinePart1(lineWithIRemoved)) {
                return true;
            }
        }

        return isSafeLinePart1(line);
    }

    @Override
    public Object part1(String input) {
        return parseInput(input).stream()
                .filter(this::isSafeLinePart1)
                .count();
    }

    @Override
    public Object part2(String input) {
        return parseInput(input).stream()
                .filter(this::isSafeLinePart2)
                .count();
    }
}
