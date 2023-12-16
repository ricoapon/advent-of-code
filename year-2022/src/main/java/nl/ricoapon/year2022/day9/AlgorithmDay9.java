package nl.ricoapon.year2022.day9;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay9 implements Algorithm {
    private HeadTailPlain processInput(String input, int nrOfKnots) {
        HeadTailPlain headTailPlain = new HeadTailPlain(nrOfKnots);
        for (String line : input.split("\r?\n")) {
            HeadTailPlain.Direction direction = switch(line.charAt(0)) {
                case 'U' -> HeadTailPlain.Direction.UP;
                case 'D' -> HeadTailPlain.Direction.DOWN;
                case 'R' -> HeadTailPlain.Direction.RIGHT;
                case 'L' -> HeadTailPlain.Direction.LEFT;
                default -> throw new RuntimeException("This should never happen");
            };

            int times = Integer.parseInt(line.substring(2));

            headTailPlain.moveHead(direction, times);
        }

        return headTailPlain;
    }

    @Override
    public String part1(String input) {
        HeadTailPlain headTailPlain = processInput(input, 2);
        return "" + headTailPlain.getTailHistory().size();
    }

    @Override
    public String part2(String input) {
        HeadTailPlain headTailPlain = processInput(input, 10);
        return "" + headTailPlain.getTailHistory().size();
    }
}
