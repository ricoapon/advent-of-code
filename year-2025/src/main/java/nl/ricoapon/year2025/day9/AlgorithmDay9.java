package nl.ricoapon.year2025.day9;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay9 implements Algorithm {
    private Coordinate2D of(String line) {
        List<Integer> integers = Stream.of(line.split(",")).map(Integer::valueOf).toList();
        return new Coordinate2D(integers.get(0), integers.get(1));
    }

    public long squareSize(Coordinate2D a, Coordinate2D b) {
        return (Math.abs(a.x() - b.x()) + 1L) * (Math.abs(a.y() - b.y()) + 1L);
    }

    @Override
    public Object part1(String input) {
        List<Coordinate2D> coordinates = Stream.of(input.split("\\r?\\n")).map(this::of).toList();
        long biggestSize = 0;
        for (Coordinate2D a : coordinates) {
            for (Coordinate2D b : coordinates) {
                var newSquareSize = squareSize(a, b);
                if (newSquareSize > biggestSize) {
                    biggestSize = newSquareSize;
                }
            }
        }

        return biggestSize;
    }

    protected enum Axis {
        HORIZONTAL,
        VERTICAL;
    }

    protected record Line(Coordinate2D a, Coordinate2D b, Axis axis) {
        public Line(Coordinate2D a, Coordinate2D b) {
            this(smallest(a, b), largest(a, b), determineAxis(a, b));
        }

        private static Coordinate2D smallest(Coordinate2D a, Coordinate2D b) {
            // Since we know either x or y is equal, this will return either a or b.
            return new Coordinate2D(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
        }

        private static Coordinate2D largest(Coordinate2D a, Coordinate2D b) {
            // Since we know either x or y is equal, this will return either a or b.
            return new Coordinate2D(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
        }

        private static Axis determineAxis(Coordinate2D a, Coordinate2D b) {
            // We know that a line can never be a diagonal!
            if (a.x() == b.x()) {
                return Axis.VERTICAL;
            }
            return Axis.HORIZONTAL;
        }

        // We exclude the start and end points, because obviously they are on a line.
        public boolean hasSinglePointInsideLineIntersectionWith(Line other) {
            // We can only have a single point if have a vertical and horizontal line.
            if (axis == other.axis) {
                return false;
            }

            // If a point exists, it must be this one. If it is also on both lines, we
            // found that single point.
            var x = axis == Axis.VERTICAL ? a.x() : other.a.x();
            var y = axis == Axis.HORIZONTAL ? a.y() : other.a.y();
            var c = new Coordinate2D(x, y);

            if (c.equals(a) || c.equals(b) || c.equals(other.a) || c.equals(other.b)) {
                return false;
            }

            return contains(c) && other.contains(c);
        }

        public boolean contains(Coordinate2D c) {
            return switch (axis) {
                case VERTICAL -> c.x() == a.x() && a.y() <= c.y() && c.y() <= b.y();
                case HORIZONTAL -> c.y() == a.y() && a.x() <= c.x() && c.x() <= b.x();
            };
        }
    }

    @Override
    public Object part2(String input) {
        List<Coordinate2D> coordinates = Stream.of(input.split("\\r?\\n")).map(this::of).toList();
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            var a = coordinates.get(i);
            var b = (i + 1 < coordinates.size()) ? coordinates.get(i + 1) : coordinates.get(0);
            lines.add(new Line(a, b));
        }

        long biggestSize = 0;
        for (Coordinate2D a : coordinates) {
            for (Coordinate2D b : coordinates) {
                if (a.equals(b) || a.x() == b.x() || a.y() == b.y()) {
                    continue;
                }
                var newSquareSize = squareSize(a, b);
                if (newSquareSize > biggestSize && isSquareInsideBorder(lines, a, b)) {
                    biggestSize = newSquareSize;
                }
            }
        }

        return biggestSize;
    }

    private boolean isSquareInsideBorder(List<Line> lines, Coordinate2D a, Coordinate2D b) {
        // This uses a trick that no two lines inside the list of lines is colinear.
        // We can reduce the check to if there is a single point intersection.
        List<Line> squareLines = List.of(
                new Line(a, new Coordinate2D(a.x(), b.y())),
                new Line(a, new Coordinate2D(b.x(), a.y())),
                new Line(new Coordinate2D(a.x(), b.y()), b),
                new Line(new Coordinate2D(b.x(), a.y()), b));

        for (Line squareLine : squareLines) {
            if (lines.stream().filter(squareLine::hasSinglePointInsideLineIntersectionWith).count() > 0) {
                return false;
            }
        }

        // TODO: find out a way to exclude this case:
        // ..O.
        // .OX.
        // Where O are the corners. All four corners must be inside!

        return true;
    }
}
