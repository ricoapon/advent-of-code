package nl.ricoapon.year2023.day6;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {
    @Override
    public Object part1(String input) {
        return Race.of(input).stream().map(this::countNrOfWaysToWin).reduce(1L, (l1, l2) -> l1 * l2);
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
    public Object part2(String input) {
        return countNrOfWaysToWin(Race.of(input.replace(" ", "")).get(0));
    }
}
