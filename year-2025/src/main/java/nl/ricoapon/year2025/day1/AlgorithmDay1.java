package nl.ricoapon.year2025.day1;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay1 implements Algorithm {
    @Override
    public Object part1(String input) {
        int vaultValue = 50;
        int nrOfTimesVaultValueEqualsZero = 0;
        for (String rotation : input.split("\r?\n")) {
            if (rotation.isBlank()) {
                continue;
            }

            vaultValue = vaultValue + convertRotationToValue(rotation);
            vaultValue = mod(vaultValue, 100);

            if (vaultValue == 0) {
                nrOfTimesVaultValueEqualsZero++;
            }
        }

        return nrOfTimesVaultValueEqualsZero;
    }

    private int convertRotationToValue(String rotation) {
        int x = Integer.valueOf(rotation.substring(1));
        if (rotation.startsWith("R")) {
            return x;
        } else if (rotation.startsWith("L")) {
            return -x;
        } else {
            throw new RuntimeException("This should never happen");
        }
    }

    private int mod(int x, int n) {
        // Sadly, modulo returns negative values. This trick ensures it is always between 0 and n-1.
        return ((x % n) + n) % n;
    }

    // Note that this method ALSO counts if the final value lands on 0.
    private int modCountHowManyTimesPassesZero(int x, int n, int previousX, int rotationValue) {
        if (0 < x && x < n) {
            return 0;
        }

        int nrOfTimesPastZero = 0;
        while (x < 0) {
            x += n;
            nrOfTimesPastZero++;
        }
        while (x >= n) {
            x -= n;
            nrOfTimesPastZero++;
        }

        // If we land on 0, we might or might not have counted it. This depends on
        // the rotation. If we start on 95 and rotate R5, then we counted it already.
        // If we start on 5 and rotate L5, we did not count it already.
        if (x == 0 && rotationValue < 0) {
            nrOfTimesPastZero++;
        }

        // If we started on 0 and rotate L5, the while-loop will count it but it shouldn't.
        // We need to substract one in these cases.
        if (previousX == 0 && rotationValue < 0) {
            nrOfTimesPastZero--;
        }

        return nrOfTimesPastZero;
    }

    @Override
    public Object part2(String input) {
        int vaultValue = 50;
        int nrOfTimesVaultValuePassesZero = 0;
        for (String rotation : input.split("\r?\n")) {
            if (rotation.isBlank()) {
                continue;
            }
            
            int previousVaultValue = vaultValue;
            vaultValue = vaultValue + convertRotationToValue(rotation);
            nrOfTimesVaultValuePassesZero += modCountHowManyTimesPassesZero(vaultValue, 100, previousVaultValue, convertRotationToValue(rotation));
            vaultValue = mod(vaultValue, 100);
        }

        return nrOfTimesVaultValuePassesZero;
    }
}
