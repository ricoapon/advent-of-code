package nl.ricoapon.year2021.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PossibleCalculator {
    /** Record for containing the possible of either x or y velocity. */
    private record PossibleVelocity(int velocity, StepRange stepRange) {
    }

    /** Indicates a range. Use {@link Integer#MIN_VALUE} or {@link Integer#MAX_VALUE} for infinity. */
    private record StepRange(int minStep, int maxStep) {
        public boolean intersects(StepRange other) {
            // Ranges overlap if either the min or max of this range is contained in the other OR
            // the other min or max is contained in the current range.
            return (other.minStep <= minStep && minStep <= other.maxStep) ||
                    (other.minStep <= maxStep && maxStep <= other.maxStep) ||
                    (minStep <= other.minStep && other.minStep <= maxStep) ||
                    (minStep <= other.maxStep && other.maxStep <= maxStep);
        }
    }

    public List<PossibleStartingVelocity> calculatePossibleXY(int targetMinX, int targetMaxX, int targetMinY, int targetMaxY) {
        List<PossibleVelocity> possibleXList = calculatePossibleX(targetMinX, targetMaxX);
        List<PossibleVelocity> possibleYList = calculatePossibleY(targetMinY, targetMaxY);

        List<PossibleStartingVelocity> possibleStartingVelocityList = new ArrayList<>();
        for (PossibleVelocity possibleX : possibleXList) {
            for (PossibleVelocity possibleY : possibleYList) {
                if (possibleX.stepRange.intersects(possibleY.stepRange)) {
                    possibleStartingVelocityList.add(new PossibleStartingVelocity(possibleX.velocity(), possibleY.velocity()));
                }
            }
        }
        return possibleStartingVelocityList;
    }

    private List<PossibleVelocity> calculatePossibleX(int targetMinX, int targetMaxX) {
        List<PossibleVelocity> possibleXList = new ArrayList<>();
        for (int xVelocity = 1; xVelocity <= targetMaxX; xVelocity++) {
            Optional<StepRange> stepRange = calculatePossibleStepsForX(targetMinX, targetMaxX, xVelocity);
            if (stepRange.isPresent()) {
                possibleXList.add(new PossibleVelocity(xVelocity, stepRange.get()));
            }
        }
        return possibleXList;
    }

    private List<PossibleVelocity> calculatePossibleY(int targetMinY, int targetMaxY) {
        int maxPosYValue = Math.max(Math.abs(targetMinY), Math.abs(targetMaxY));
        List<PossibleVelocity> possibleYList = new ArrayList<>();
        for (int yVelocity = -1 * maxPosYValue; yVelocity <= maxPosYValue; yVelocity++) {
            Optional<StepRange> stepRange = calculatePossibleStepsForY(targetMinY, targetMaxY, yVelocity);
            if (stepRange.isPresent()) {
                possibleYList.add(new PossibleVelocity(yVelocity, stepRange.get()));
            }
        }
        return possibleYList;
    }

    @SuppressWarnings("ConstantConditions")
    private Optional<StepRange> calculatePossibleStepsForX(int targetMinX, int targetMaxX, int velocity) {
        int xCoordinate = 0;
        int step = 0;
        boolean firstTimeInRange = true;
        int minStepRange = 0;
        while (xCoordinate <= targetMaxX && velocity != 0) {
            if (targetMinX <= xCoordinate && xCoordinate <= targetMaxX) {
                if (firstTimeInRange) {
                    firstTimeInRange = false;
                    minStepRange = step;
                }
            }

            step++;
            xCoordinate += velocity;
            if (velocity > 0) {
                velocity--;
            } else if (velocity < 0) {
                velocity++;
            }
        }

        if (firstTimeInRange) {
            // We never hit the target.
            return Optional.empty();
        }

        // The while loop stopped so either we are out of range or the velocity is zero.
        // However, to make sure that we didn't get to zero in the last step, also make sure we are still in the range.
        if (velocity == 0 && targetMinX <= xCoordinate && xCoordinate <= targetMaxX) {
            return Optional.of(new StepRange(minStepRange, Integer.MAX_VALUE));
        } else {
            return Optional.of(new StepRange(minStepRange, step - 1));
        }
    }

    private Optional<StepRange> calculatePossibleStepsForY(int targetMinY, int targetMaxY, int velocity) {
        int yCoordinate = 0;
        int step = 0;
        boolean firstTimeInRange = true;
        int minStepRange = 0;
        while (!(velocity < 0 && yCoordinate < targetMinY)) {
            if (targetMinY <= yCoordinate && yCoordinate <= targetMaxY) {
                if (firstTimeInRange) {
                    firstTimeInRange = false;
                    minStepRange = step;
                }
            }

            step++;
            yCoordinate += velocity;
            velocity--;
        }

        if (firstTimeInRange) {
            // We never hit the target.
            return Optional.empty();
        }

        return Optional.of(new StepRange(minStepRange, step - 1));
    }
}
