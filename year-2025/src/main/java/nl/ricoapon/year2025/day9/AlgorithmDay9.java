package nl.ricoapon.year2025.day9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay9 implements Algorithm {
    private Coordinate2D of(String line) {
        List<Integer> integers = Stream.of(line.split(",")).map(Integer::valueOf).toList();
        return new Coordinate2D(integers.get(0), integers.get(1));
    }

    public long rectangleSize(Coordinate2D a, Coordinate2D b) {
        return (Math.abs(a.x() - b.x()) + 1L) * (Math.abs(a.y() - b.y()) + 1L);
    }

    @Override
    public Object part1(String input) {
        List<Coordinate2D> coordinates = Stream.of(input.split("\\r?\\n")).map(this::of).toList();
        long biggestSize = 0;
        for (Coordinate2D a : coordinates) {
            for (Coordinate2D b : coordinates) {
                var newRectangleSize = rectangleSize(a, b);
                if (newRectangleSize > biggestSize) {
                    biggestSize = newRectangleSize;
                }
            }
        }

        return biggestSize;
    }

    protected enum Axis {
        HORIZONTAL,
        VERTICAL;
    }

    public record Line(Coordinate2D a, Coordinate2D b, Axis axis) {
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

        public boolean contains(Coordinate2D c) {
            return switch (axis) {
                case VERTICAL -> c.x() == a.x() && a.y() <= c.y() && c.y() <= b.y();
                case HORIZONTAL -> c.y() == a.y() && a.x() <= c.x() && c.x() <= b.x();
            };
        }

        // Can be single point or longer range. Both return true.
        public boolean overlapsWith(Line other) {
            if (axis == other.axis) {
                if (sameCoordinate() != other.sameCoordinate()) {
                    return false;
                }

                // The lines are co-linear. Oo overlap one of the endpoints must be in the line.
                return contains(other.a) || contains(other.b);
            }

            // There can only be one intersection, if it exists.
            var x = axis == Axis.VERTICAL ? sameCoordinate() : other.sameCoordinate();
            var y = axis == Axis.HORIZONTAL ? sameCoordinate() : other.sameCoordinate();
            var c = new Coordinate2D(x, y);

            return contains(c) && other.contains(c);
        }

        public boolean overlapsWithAny(List<Line> others) {
            return others.stream().anyMatch(this::overlapsWith);
        }

        public int minCoordinate() {
            if (axis == Axis.HORIZONTAL) {
                return a.x();
            }
            return a.y();
        }

        public int maxCoordinate() {
            if (axis == Axis.HORIZONTAL) {
                return b.x();
            }
            return b.y();
        }

        public int sameCoordinate() {
            if (axis == Axis.HORIZONTAL) {
                return a.y();
            }
            return a.x();
        }
    }

    @Override
    public Object part2(String input) {
        // Core idea of the algorithm: decrease the size of each rectangle. This means
        // it must be fully inside, not touching any edges. So we can easily check if
        // we have an intersection. If so, the rectangle is not fully inside.
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
                var newRectangleSize = rectangleSize(a, b);

                // Move all points inwards.
                List<Line> rectangleLines = smallerRectangleLines(a, b);

                if (newRectangleSize > biggestSize
                        && rectangleLines.stream().noneMatch(l -> l.overlapsWithAny(lines))) {
                    biggestSize = newRectangleSize;
                }
            }
        }

        return biggestSize;
    }

    private List<Line> smallerRectangleLines(Coordinate2D a, Coordinate2D b) {
        // We don't know exactly which coordinate is which. So we find them based on
        // criteria.
        var coordinates = List.of(a, new Coordinate2D(a.x(), b.y()), new Coordinate2D(b.x(), a.y()), b);

        // Lowest x, lowest y.
        var leftBottom = coordinates.stream().min(Comparator.comparing(Coordinate2D::x).thenComparing(Coordinate2D::y))
                .orElseThrow()
                .sum(new Coordinate2D(1, 1));
        // Lowest x, highest y.
        var leftTop = coordinates.stream()
                .min(Comparator.comparing(Coordinate2D::x).thenComparing(Coordinate2D::y, Comparator.reverseOrder()))
                .orElseThrow()
                .sum(new Coordinate2D(1, -1));
        // Highest x, highest y.
        var rightTop = coordinates.stream()
                .min(Comparator.comparing(Coordinate2D::x, Comparator.reverseOrder()).thenComparing(Coordinate2D::y,
                        Comparator.reverseOrder()))
                .orElseThrow()
                .sum(new Coordinate2D(-1, -1));
        // Highest x, lowest y.
        var rightBottom = coordinates.stream()
                .min(Comparator.comparing(Coordinate2D::x, Comparator.reverseOrder()).thenComparing(Coordinate2D::y))
                .orElseThrow()
                .sum(new Coordinate2D(-1, 1));

        return List.of(
            new Line(leftBottom, leftTop),
            new Line(leftBottom, rightBottom),
            new Line(rightBottom, rightTop),
            new Line(leftTop, rightTop)
        );
    }
}
