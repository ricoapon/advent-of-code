package nl.ricoapon.year2021.day5;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line {
    private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");

    private final Point p1, p2;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public static Line of(String lineAsString) {
        Matcher matcher = LINE_PATTERN.matcher(lineAsString);
        if (!matcher.matches()) {
            throw new RuntimeException("Should not happen");
        }
        Point p1 = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        Point p2 = new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        return new Line(p1, p2);
    }

    public boolean isHorizontalOrVertical() {
        return p1.x() == p2.x() || p1.y() == p2.y();
    }

    public List<Point> calculatePointsOnLine() {
        if (!isHorizontalOrVertical()) {
            return calculateDiagonalPoints();
        }

        List<Point> pointsOnLine = new ArrayList<>();

        int deltaX = Math.abs(p2.x() - p1.x());
        int signDeltaX = Integer.signum(p2.x() - p1.x());
        int deltaY = Math.abs(p2.y() - p1.y());
        int signDeltaY = Integer.signum(p2.y() - p1.y());

        for (int changeX = 0; changeX <= deltaX; changeX++) {
            for (int changeY = 0; changeY <= deltaY; changeY++) {
                pointsOnLine.add(new Point(p1.x() + changeX * signDeltaX, p1.y() + changeY * signDeltaY));
            }
        }

        return pointsOnLine;
    }

    private List<Point> calculateDiagonalPoints() {
        List<Point> pointsOnLine = new ArrayList<>();

        // The exercise indicates we may assume the angle is always 45 degrees.
        int deltaXAndY = Math.abs(p2.x() - p1.x());
        int signDeltaX = Integer.signum(p2.x() - p1.x());
        int signDeltaY = Integer.signum(p2.y() - p1.y());

        for (int change = 0; change <= deltaXAndY; change++) {
            pointsOnLine.add(new Point(p1.x() + change * signDeltaX, p1.y() + change * signDeltaY));
        }

        return pointsOnLine;
    }
}
