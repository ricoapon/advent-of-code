package nl.ricoapon.year2022.day10;

import java.util.ArrayList;
import java.util.List;

public class CommunicationDevice {
    private final List<Integer> cyclesToCheck = List.of(20, 60, 100, 140, 180, 220);

    private int cycle = 0;
    private int X = 1;
    private final List<Integer> cycleValues = new ArrayList<>();

    public void noop() {
        cycle++;
        checkSignalStrength();
    }

    public void add(int v) {
        cycle++;
        checkSignalStrength();
        cycle++;
        checkSignalStrength();
        X += v;
    }

    private void checkSignalStrength() {
        if (!cyclesToCheck.contains(cycle)) {
            return;
        }

        cycleValues.add(cycle * X);
    }

    public int getCycleValuesSum() {
        // The last cycle
        return cycleValues.stream().mapToInt(x -> x).sum();
    }
}
