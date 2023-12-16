package nl.ricoapon.year2021.day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    public enum Direction {
        FORWARD, DOWN, UP

    }

    private final Direction direction;
    private final int x;

    private Command(Direction direction, int x) {
        this.direction = direction;
        this.x = x;
    }

    public static Command of(String line) {
        Matcher matcher = Pattern.compile(".* (\\d+)").matcher(line);
        if (!matcher.matches()) {
            throw new RuntimeException("Cannot find x in input line '" + line + "'");
        }
        int x = Integer.parseInt(matcher.group(1));
        if (line.startsWith("forward")) {
            return new Command(Direction.FORWARD, x);
        } else if (line.startsWith("down")) {
            return new Command(Direction.DOWN, x);
        } else {
            return new Command(Direction.UP, x);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }
}
