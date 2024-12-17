package nl.ricoapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record Coordinate2D(int x, int y) {
    public Collection<Coordinate2D> getHorizontalAndVerticalAdjacent(int maxX, int maxY) {
        List<Coordinate2D> adjacent = new ArrayList<>();
        if (x > 0) {
            adjacent.add(new Coordinate2D(x - 1, y));
        }
        if (y > 0) {
            adjacent.add(new Coordinate2D(x, y - 1));
        }
        if (x < maxX) {
            adjacent.add(new Coordinate2D(x + 1, y));
        }
        if (y < maxY) {
            adjacent.add(new Coordinate2D(x, y + 1));
        }
        return adjacent;
    }

    public Collection<Coordinate2D> getDiagonalAdjacent(int maxX, int maxY) {
        List<Coordinate2D> adjacent = new ArrayList<>();
        // Top left
        if (x > 0 && y > 0) {
            adjacent.add(new Coordinate2D(x - 1, y - 1));
        }
        // Top right.
        if (x > 0 && y < maxY) {
            adjacent.add(new Coordinate2D(x - 1, y + 1));
        }
        // Bottom left.
        if (x < maxX && y > 0) {
            adjacent.add(new Coordinate2D(x + 1, y - 1));
        }
        // Bottom right.
        if (x < maxX && y < maxY) {
            adjacent.add(new Coordinate2D(x + 1, y + 1));
        }
        return adjacent;
    }

    public Collection<Coordinate2D> getAdjacent(int maxX, int maxY) {
        List<Coordinate2D> adjacent = new ArrayList<>();
        adjacent.addAll(getHorizontalAndVerticalAdjacent(maxX, maxY));
        adjacent.addAll(getDiagonalAdjacent(maxX, maxY));
        return adjacent;
    }

    public Coordinate2D sum(Coordinate2D other) {
        return new Coordinate2D(x + other.x, y + other.y);
    }

    public Coordinate2D minus(Coordinate2D other) {
        return new Coordinate2D(x - other.x, y - other.y);
    }

    public Coordinate2D multiply(int i) {
        return new Coordinate2D(x * i, y * i);
    }
}
