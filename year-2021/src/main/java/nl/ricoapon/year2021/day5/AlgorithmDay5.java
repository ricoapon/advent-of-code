package nl.ricoapon.year2021.day5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay5 implements Algorithm {

    @Override
    public Object part1(String input) {
        List<Line> lines = Arrays.stream(input.split("\n")).map(Line::of).filter(Line::isHorizontalOrVertical).toList();

        Map<Point, Integer> pointOccurrenceMap = new HashMap<>();
        lines.stream()
                .flatMap(line -> line.calculatePointsOnLine().stream())
                .forEach(point -> pointOccurrenceMap.put(point, pointOccurrenceMap.getOrDefault(point, 0) + 1));

        long answer = pointOccurrenceMap.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .count();

        return String.valueOf(answer);
    }

    @Override
    public Object part2(String input) {
        List<Line> lines = Arrays.stream(input.split("\n")).map(Line::of).toList();

        Map<Point, Integer> pointOccurrenceMap = new HashMap<>();
        lines.stream()
                .flatMap(line -> line.calculatePointsOnLine().stream())
                .forEach(point -> pointOccurrenceMap.put(point, pointOccurrenceMap.getOrDefault(point, 0) + 1));

        long answer = pointOccurrenceMap.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .count();

        return String.valueOf(answer);
    }
}
