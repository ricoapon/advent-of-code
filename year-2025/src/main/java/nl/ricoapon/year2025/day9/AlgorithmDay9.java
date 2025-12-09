package nl.ricoapon.year2025.day9;

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

    // Only horizontal lines can go left or right and only vertical lines can go up
    // or down.
    protected enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN;
    }

    protected record Line(Coordinate2D a, Coordinate2D b, Axis axis, Direction direction) {
        public Line(Coordinate2D a, Coordinate2D b) {
            this(a, b, determineAxis(a, b), determineDirection(a, b));
        }

        private static Axis determineAxis(Coordinate2D a, Coordinate2D b) {
            // We know that a line can never be a diagonal!
            if (a.x() == b.x()) {
                return Axis.VERTICAL;
            }
            return Axis.HORIZONTAL;
        }

        private static Direction determineDirection(Coordinate2D a, Coordinate2D b) {
            if (determineAxis(a, b) == Axis.VERTICAL) {
                if (a.y() < b.y()) {
                    return Direction.UP;
                }
                return Direction.DOWN;
            }
            if (a.x() < b.x()) {
                return Direction.RIGHT;
            }
            return Direction.LEFT;
        }

        public int minX() {
            return Math.min(a.x(), b.x());
        }

        public int maxX() {
            return Math.max(a.x(), b.x());
        }

        public int minY() {
            return Math.min(a.y(), b.y());
        }

        public int maxY() {
            return Math.max(a.y(), b.y());
        }

        public boolean canWalkOutToLine(Line other) {
            return switch (direction) {
                case UP -> other.a.y() > a.y();
                case DOWN -> other.a.y() < a.y();
                case RIGHT -> other.a.x() < a.x();
                case LEFT -> other.a.x() > a.x();
            };
        }

        public boolean contains(Coordinate2D c) {
            return switch (direction) {
                case UP -> c.x() == a.x() && a.y() <= c.y() && c.y() <= b.y();
                case DOWN -> c.x() == a.x() && a.y() >= c.y() && c.y() >= b.y();
                case RIGHT -> c.y() == a.y() && a.x() >= c.x() && c.x() >= b.x();
                case LEFT -> c.y() == a.y() && a.x() <= c.x() && c.x() <= b.x();
            };
        }
    }

    @Override
    public Object part2(String input) {
        return "x";
    }
}
