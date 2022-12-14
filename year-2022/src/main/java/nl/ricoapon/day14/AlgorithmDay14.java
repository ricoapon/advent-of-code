package nl.ricoapon.day14;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay14 implements Algorithm {
    private Coordinate2D convertCoordinateString(String s) {
        return new Coordinate2D(Integer.valueOf(s.split(",")[0]), Integer.valueOf(s.split(",")[1]));
    }

    private Set<Coordinate2D> convertLine(String line) {
        List<Coordinate2D> corners = Arrays.stream(line.split(" -> "))
                .map(this::convertCoordinateString)
                .toList();

        // Now traverse the corners and actually make a line.
        Set<Coordinate2D> coordinates = new HashSet<>();
        Coordinate2D previous = corners.get(0);
        for (int i = 1; i < corners.size(); i++) {
            Coordinate2D corner = corners.get(i);
            if (previous.y() == corner.y()) {
                int delta = 1;
                if (previous.x() > corner.x()) {
                    delta = -1;
                }
                for (int j = 0; j <= Math.abs(previous.x() - corner.x()); j++) {
                    coordinates.add(new Coordinate2D(previous.x() + j * delta, corner.y()));
                }
            } else if (previous.x() == corner.x()) {
                int delta = 1;
                if (previous.y() > corner.y()) {
                    delta = -1;
                }
                for (int j = 0; j < Math.abs(previous.y() - corner.y()); j++) {
                    coordinates.add(new Coordinate2D(corner.x(), previous.y() + j * delta));
                }

            } else {
                throw new RuntimeException("This should never happen");
            }

            previous = corner;
        }
        // The last part may not be forgotten!
        coordinates.add(previous);

        return coordinates;
    }

    @Override
    public String part1(String input) {
        Cave cave = new Cave();

        Arrays.stream(input.split("\r?\n"))
                .flatMap(s -> convertLine(s).stream())
                .forEach(cave::addRock);

        cave.printSituation();
        System.out.println("\n");

        int counter = 0;
        while (cave.dropSand()) {
            counter++;
        }
        cave.printSituation();
        System.out.println("\n");


        // 1075 and 1076 were too low.
        return "" + counter;
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
