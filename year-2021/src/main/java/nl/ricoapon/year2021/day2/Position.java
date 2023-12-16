package nl.ricoapon.year2021.day2;

public class Position {
    private int depth = 0;
    private int horizontalPosition = 0;

    public void apply(Command command) {
        if (command.getDirection() == Command.Direction.UP) {
            depth -= command.getX();
        } else if (command.getDirection() == Command.Direction.DOWN) {
            depth += command.getX();
        } else {
            horizontalPosition += command.getX();
        }
    }

    public int getDepth() {
        return depth;
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }
}
