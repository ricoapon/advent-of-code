package nl.ricoapon.year2022.day14;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import nl.ricoapon.Coordinate2D;

public class Cave {
    private final boolean useInfiniteFloor;
    private final Set<Coordinate2D> solidRocks = new HashSet<>();
    private final Set<Coordinate2D> solidSand = new HashSet<>();
    // If sand reaches this Y-level, then there are no more rocks beneath it.
    private int abyssY = 0;

    public Cave(boolean useInfiniteFloor) {
        this.useInfiniteFloor = useInfiniteFloor;
    }

    public void addRock(Coordinate2D coordinate) {
        solidRocks.add(coordinate);
        // For part 2 we have to add 2 to the maximal Y. For part 1 it doesn't matter.
        if (coordinate.y() > abyssY - 2) {
            abyssY = coordinate.y() + 2;
        }
    }

    /**
     * @return {@code true} if the sand became solid and {@code false} if the sand
     *         fell into the abyss or we reached (500,0).
     */
    public boolean dropSand() {
        Coordinate2D currentCoordinate = new Coordinate2D(500, 0);
        while (true) {
            List<Coordinate2D> nextCoordinates = nextCoordinates(currentCoordinate);
            if (nextCoordinates.isEmpty()) {
                if (useInfiniteFloor && currentCoordinate.x() == 500 && currentCoordinate.y() == 0) {
                    return false;
                }

                // We reached a point where we only have solids.
                solidSand.add(currentCoordinate);
                return true;
            }
            currentCoordinate = nextCoordinates.get(0);

            if (!useInfiniteFloor && currentCoordinate.y() > abyssY) {
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
                .filter(c -> {
                    if (!useInfiniteFloor) {
                        return true;
                    }
                    return c.y() < abyssY;
                })
                .toList();
    }

    public void printSituation() {
        int minXSand = solidSand.stream().mapToInt(c -> c.x()).min().orElseThrow();
        int minXRock = solidRocks.stream().mapToInt(c -> c.x()).min().orElseThrow();
        int minX = Math.min(minXSand, minXRock);
        int maxXSand = solidSand.stream().mapToInt(c -> c.x()).max().orElseThrow();
        int maxXRock = solidRocks.stream().mapToInt(c -> c.x()).max().orElseThrow();
        int maxX = Math.max(maxXSand, maxXRock);

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
        if (useInfiniteFloor) {
            for (int x = minX; x <= maxX; x++) {
                System.out.print("#");
            }
        }
    }
}
