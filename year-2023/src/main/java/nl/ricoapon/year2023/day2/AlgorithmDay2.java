package nl.ricoapon.year2023.day2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay2 implements Algorithm {
    @Override
    public Object part1(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .map(Game::of)
                .filter(this::isGamePossible)
                .mapToInt(Game::id)
                .sum();
    }

    private boolean isGamePossible(Game game) {
        for (Revealed revealed : game.revealedList()) {
            if (revealed.blue() > 14 || revealed.green() > 13 || revealed.red() > 12) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object part2(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .map(Game::of)
                .mapToInt(this::determinePower)
                .sum();
    }

    private int determinePower(Game game) {
        // For some reason I cannot inline this list variable?
        List<Function<Revealed, Integer>> list = List.of(Revealed::blue, Revealed::green, Revealed::red);
        return list.stream()
            .mapToInt(r -> determineMinimumNeededCubes(game, r))
            .reduce(1, (i1, i2) -> i1 * i2);
    }

    private int determineMinimumNeededCubes(Game game, Function<Revealed, Integer> getColorCount) {
        return game.revealedList().stream()
                .mapToInt(getColorCount::apply)
                .max().orElseThrow();
    }
}
