package nl.ricoapon.year2021.day2;

import java.util.Arrays;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay2 implements Algorithm {

    @Override
    public Object part1(String input) {
        Position position = new Position();
        Arrays.stream(input.split("\n"))
                .map(Command::of)
                .forEach(position::apply);
        return String.valueOf(position.getHorizontalPosition() * position.getDepth());
    }

    @Override
    public Object part2(String input) {
        PositionPart2 position = new PositionPart2();
        Arrays.stream(input.split("\n"))
                .map(Command::of)
                .forEach(position::apply);
        return String.valueOf(position.getHorizontalPosition() * position.getDepth());
    }
}
