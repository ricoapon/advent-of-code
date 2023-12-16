package nl.ricoapon.year2023.day14;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay14 implements Algorithm {
    @Override
    public String part1(String input) {
        Platform platform = Platform.of(input);
        Platform tiltedPlatform = platform.tilt(Platform.Direction.NORTH);
        long result = scorePlatform(tiltedPlatform);
        return String.valueOf(result);
    }

    private long scorePlatform(Platform platform) {
        long score = 0;
        for (int i = 0; i < platform.grid().getSizeX(); i++) {
            for (int j = 0; j < platform.grid().getSizeY(); j++) {
                Platform.Cell cell = platform.grid().getCell(new Coordinate2D(i, j));
                if (Platform.Cell.ROUND_ROCK.equals(cell)) {
                    score += (platform.grid().getSizeX() - i);
                }
            }
        }
        return score;
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
