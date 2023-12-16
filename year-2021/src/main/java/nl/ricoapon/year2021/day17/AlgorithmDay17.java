package nl.ricoapon.year2021.day17;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmDay17 implements Algorithm {
    private final static Pattern INPUT_PATTERN = Pattern.compile("target area: x=(\\d+)..(\\d+), y=(-\\d+)..(-\\d+).*");

    private List<PossibleStartingVelocity> calculatePossibleStartingVelocityList(String input) {
        Matcher matcher = INPUT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new RuntimeException("Input is incorrect.");
        }

        int targetMinX = Integer.parseInt(matcher.group(1));
        int targetMaxX = Integer.parseInt(matcher.group(2));
        int targetMinY = Integer.parseInt(matcher.group(3));
        int targetMaxY = Integer.parseInt(matcher.group(4));

        PossibleCalculator possibleCalculator = new PossibleCalculator();
        List<PossibleStartingVelocity> list = possibleCalculator.calculatePossibleXY(targetMinX, targetMaxX, targetMinY, targetMaxY).stream().sorted(Comparator.comparingInt(PossibleStartingVelocity::xVelocity)).toList();

        List<PossibleStartingVelocity> badList = list.stream().filter(p -> !canHitTarget(targetMinX, targetMaxX, targetMinY, targetMaxY, p)).toList();
        if (!badList.isEmpty()) {
            throw new RuntimeException(badList.toString());
        }

        return possibleCalculator.calculatePossibleXY(targetMinX, targetMaxX, targetMinY, targetMaxY).stream().sorted(Comparator.comparingInt(PossibleStartingVelocity::xVelocity)).toList();
    }

    @Override
    public String part1(String input) {
        List<PossibleStartingVelocity> possibleStartingVelocityList = calculatePossibleStartingVelocityList(input);
        int maxHeight = possibleStartingVelocityList.stream().mapToInt(this::calculateMaxHeight).max().orElseThrow();

        return String.valueOf(maxHeight);
    }

    private int calculateMaxHeight(PossibleStartingVelocity possibleStartingVelocity) {
        int yVelocity = possibleStartingVelocity.yVelocity();
        if (yVelocity > 0) {
            // The max height is always yVelocity + (yVelocity - 1) + ... Using math this comes down to the following formula:
            return yVelocity * (yVelocity + 1) / 2;
        } else {
            // We start at coordinate 0. With a negative y-velocity, we can never get heigher than that.
            return 0;
        }
    }

    @Override
    public String part2(String input) {
        return String.valueOf(calculatePossibleStartingVelocityList(input).size());
    }

    @SuppressWarnings("ConstantConditions")
    private boolean canHitTarget(int targetMinX, int targetMaxX, int targetMinY, int targetMaxY, PossibleStartingVelocity possibleStartingVelocity) {
        int xVelocity = possibleStartingVelocity.xVelocity();
        int yVelocity = possibleStartingVelocity.yVelocity();
        Coordinate2D coordinate = new Coordinate2D(0, 0);
        while ((coordinate.x() <= targetMaxX) && !(yVelocity < 0 && coordinate.y() < targetMinY)) {

            if (targetMinX <= coordinate.x() && coordinate.x() <= targetMaxX) {
                if (targetMinY <= coordinate.y() && coordinate.y() <= targetMaxY) {
                    return true;
                }
            }


            coordinate = new Coordinate2D(coordinate.x() + xVelocity, coordinate.y() + yVelocity);
            if (xVelocity > 0) {
                xVelocity--;
            } else if (xVelocity < 0) {
                xVelocity++;
            }
            yVelocity--;
        }

        return false;
    }
}
