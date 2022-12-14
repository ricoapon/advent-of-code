package nl.ricoapon.day14;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import nl.ricoapon.Coordinate2D;

public class Cave {
    private final Set<Coordinate2D> solidRocks = new HashSet<>();
    private final Set<Coordinate2D> solidSand = new HashSet<>();
    // If sand reaches this Y-level, then there are no more rocks beneath it.
    private int abyssY = 0;
    private int minX = Integer.MAX_VALUE;
    private int maxX = 0;

    public void addRock(Coordinate2D coordinate) {
        solidRocks.add(coordinate);
        if (coordinate.y() > abyssY) {
            abyssY = coordinate.y();
        }
        if (coordinate.x() < minX) {
            minX = coordinate.x();
        }
        if (coordinate.x() > maxX) {
            maxX = coordinate.x();
        }
    }

    /**
     * @return {@code true} if the sand became solid and {@code false} if the sand
     *         fell into the abyss.
     */
    public boolean dropSand() {
        Coordinate2D currentCoordinate = new Coordinate2D(500, 0);
        while (true) {
            List<Coordinate2D> nextCoordinates = nextCoordinates(currentCoordinate);
            if (nextCoordinates.isEmpty()) {
                // We reached a point where we only have solids.
                solidSand.add(currentCoordinate);
                return true;
            }
            currentCoordinate = nextCoordinates.get(0);

            if (currentCoordinate.y() > abyssY) {
                return false;
            }
        }
    }

    private List<Coordinate2D> nextCoordinates(Coordinate2D coordinate) {
        return Stream.of(
                new Coordinate2D(coordinate.x(), coordinate.y() + 1),
                new Coordinate2D(coordinate.x() - 1, coordinate.y() + 1),
                new Coordinate2D(coordinate.x() + 1, coordinate.y() + 1))
                .filter(c -> !solidRocks.contains(c))
                .filter(c -> !solidSand.contains(c))
                .toList();
    }

    public void printSituation() {
        for (int y = 0; y < abyssY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (solidRocks.contains(new Coordinate2D(x, y))) {
                    System.out.print("#");
                } else if (solidSand.contains(new Coordinate2D(x, y))) {
                    System.out.print("o");
                } else if (x == 500 && y == 0) {
                    System.out.print("+");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }
}
