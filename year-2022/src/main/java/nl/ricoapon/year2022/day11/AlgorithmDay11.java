package nl.ricoapon.year2022.day11;

import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;

public class AlgorithmDay11 implements Algorithm {
    @Override
    public String part1(String input) {
        MonkeyGame monkeyGame = new MonkeyGame(Arrays.stream(input.split("\r?\n\r?\n")).map(Monkey::of).toList());

        for (int i = 0; i < 20; i++) {
            monkeyGame.playRound(true);
        }

        return "" + monkeyGame.getMonkeyBusiness();
    }

    @Override
    public String part2(String input) {
        MonkeyGame monkeyGame = new MonkeyGame(Arrays.stream(input.split("\r?\n\r?\n")).map(Monkey::of).toList());

        for (int i = 0; i < 10000; i++) {
            monkeyGame.playRound(false);
        }

        return "" + monkeyGame.getMonkeyBusiness();
    }
}
