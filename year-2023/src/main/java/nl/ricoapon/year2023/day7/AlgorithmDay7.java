package nl.ricoapon.year2023.day7;

import java.util.Comparator;
import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay7 implements Algorithm {
    @Override
    public String part1(String input) {
        List<Hand> hands = Hand.of(input).stream().sorted(Comparator.reverseOrder()).toList();
        long winnings = 0;
        for (int i = 0; i < hands.size(); i++) {
            winnings += hands.get(i).bid() * (i + 1L);
        }

        return String.valueOf(winnings);
    }

    @Override
    public String part2(String input) {
        List<Hand2> hands = Hand2.of(input).stream().sorted(Comparator.reverseOrder()).toList();
        long winnings = 0;
        for (int i = 0; i < hands.size(); i++) {
            winnings += hands.get(i).bid() * (i + 1L);
        }

        return String.valueOf(winnings);
    }
}
