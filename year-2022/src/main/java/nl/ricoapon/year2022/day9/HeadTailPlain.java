package nl.ricoapon.year2022.day9;

import nl.ricoapon.Coordinate2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class HeadTailPlain {
    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private final List<Coordinate2D> knots;
    private final Set<Coordinate2D> tailHistory;

    /**
     * @param nrOfKnots The number of knots, including the head. So the simple case H,T has value 2.
     */
    public HeadTailPlain(int nrOfKnots) {
        knots = new ArrayList<>();
        for (int i = 0; i < nrOfKnots; i++) {
            knots.add(new Coordinate2D(0, 0));
        }
        tailHistory = new HashSet<>(Collections.singleton(new Coordinate2D(0, 0)));
    }

    public void moveHead(Direction direction, int times) {
        Coordinate2D head = knots.get(0);
        for (int i = 0; i < times; i++) {
            head = switch (direction) {
                case UP -> new Coordinate2D(head.x(), head.y() + 1);
                case DOWN -> new Coordinate2D(head.x(), head.y() - 1);
                case RIGHT -> new Coordinate2D(head.x() + 1, head.y());
                case LEFT -> new Coordinate2D(head.x() - 1, head.y());
            };
            knots.set(0, head);

            moveTails();
        }
    }

    private void moveTails() {
        for (int i = 0; i < knots.size() - 1; i++) {
            Coordinate2D head = knots.get(i);
            Coordinate2D tail = knots.get(i + 1);
            knots.set(i + 1, determineNewTailCoordinate(head, tail));
        }
        tailHistory.add(knots.get(knots.size() - 1));
    }

    private Coordinate2D determineNewTailCoordinate(Coordinate2D head, Coordinate2D tail) {
        if (distanceBetweenCoordinates(head, tail) <= Math.sqrt(2)) {
            return tail;
        }

        // We will always end up horizontally or vertically next to H. So we only need to determine which.
        // We can determine this by seeing which difference is the highest.
        int diffY = head.y() - tail.y();
        int diffX = head.x() - tail.x();

        if (abs(diffY) > abs(diffX) && diffY > 0) {
            // UP
            return new Coordinate2D(head.x(), head.y() - 1);
        } else if (abs(diffY) > abs(diffX) && diffY < 0) {
            // DOWN
            return new Coordinate2D(head.x(), head.y() + 1);
        } else if (abs(diffX) > abs(diffY) && diffX > 0) {
            // RIGHT
            return new Coordinate2D(head.x() - 1, head.y());
        } else if (abs(diffX) > abs(diffY) && diffX < 0) {
            // RIGHT
            return new Coordinate2D(head.x() + 1, head.y());
        } else if (abs(diffX) == abs(diffY)) {
            // We have to move diagonally. Now again we have 4 directions we can move into.
            if (diffX > 0 && diffY > 0) {
                // UP RIGHT
                return new Coordinate2D(head.x() - 1, head.y() - 1);
            } else if (diffX > 0 && diffY < 0) {
                // DOWN RIGHT
                return new Coordinate2D(head.x() - 1, head.y() + 1);
            } else if (diffX < 0 && diffY > 0) {
                // UP LEFT
                return new Coordinate2D(head.x() + 1, head.y() - 1);
            } else if (diffX < 0 && diffY < 0) {
                // DOWN LEFT
                return new Coordinate2D(head.x() + 1, head.y() + 1);
            }
        }

        throw new RuntimeException("This should never happen");
    }

    private double distanceBetweenCoordinates(Coordinate2D a, Coordinate2D b) {
        return Math.sqrt(pow(a.x() - b.x(), 2) + pow(a.y() - b.y(), 2));
    }

    public Set<Coordinate2D> getTailHistory() {
        return tailHistory;
    }
}
