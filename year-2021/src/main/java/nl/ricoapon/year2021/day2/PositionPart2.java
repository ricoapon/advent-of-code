package nl.ricoapon.year2021.day2;

public class PositionPart2 {
    private int depth = 0;
    private int horizontalPosition = 0;
    private int aim = 0;

    public void apply(Command command) {
        if (command.getDirection() == Command.Direction.UP) {
            aim -= command.getX();
        } else if (command.getDirection() == Command.Direction.DOWN) {
            aim += command.getX();
        } else {
            horizontalPosition += command.getX();
            depth += aim * command.getX();
        }
    }

    public int getDepth() {
        return depth;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }
}
