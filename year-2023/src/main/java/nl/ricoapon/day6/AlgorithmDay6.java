package nl.ricoapon.day6;

import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {
    @Override
    public String part1(String input) {
        List<Race> races = Race.of(input);
        long result = races.stream().map(this::countNrOfWaysToWin).reduce(1L, (l1, l2) -> l1 * l2);

        return String.valueOf(result);
    }

    private long countNrOfWaysToWin(Race race) {
        // This was fast enough! Better way would be to use the ABC formula and solve the equation x(t-x) > d.
        // But this simple for-loop was fast enough for part 2 as well (surprisingly).
        long count = 0;
        for (int i = 1; i < race.time(); i++) {
            if (i * (race.time() - i) > race.distance()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String part2(String input) {
        Race race = Race.of(input.replace(" ", "")).get(0);
        long result = countNrOfWaysToWin(race);
        return String.valueOf(result);
    }
}
