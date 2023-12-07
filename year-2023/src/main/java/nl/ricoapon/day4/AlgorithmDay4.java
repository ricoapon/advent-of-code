package nl.ricoapon.day4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay4 implements Algorithm {
    @Override
    public String part1(String input) {
        int result = Arrays.stream(input.split("\\r?\\n"))
                .map(ScratchCard::of)
                .mapToInt(this::determinePoints)
                .sum();
        return String.valueOf(result);
    }

    private int countMatchingNumbers(ScratchCard scratchCard) {
        return (int) scratchCard.numbers().stream()
                .filter(i -> scratchCard.winningNumbers().contains(i))
                .count();
    }

    private int determinePoints(ScratchCard scratchCard) {
        int nrOfMatchingNumbers = countMatchingNumbers(scratchCard);

        if (nrOfMatchingNumbers == 0) {
            return 0;
        }

        return (int) Math.pow(2, nrOfMatchingNumbers - 1);
    }

    @Override
    public String part2(String input) {
        List<ScratchCard> scratchCards = Arrays.stream(input.split("\\r?\\n"))
                .map(ScratchCard::of)
                .toList();

        Map<Integer, Integer> countOfScratchCards = new HashMap<>();
        for (int i = 1; i <= scratchCards.size(); i++) {
            countOfScratchCards.put(i, 1);
        }

        for (ScratchCard scratchCard : scratchCards) {
            int nrOfMatchingNumbers = countMatchingNumbers(scratchCard);
            int multiplier = countOfScratchCards.get(scratchCard.id());
            for (int i = 0; i < nrOfMatchingNumbers; i++) {
                int newId = scratchCard.id() + i + 1;
                countOfScratchCards.put(newId, countOfScratchCards.get(newId) + multiplier);
            }
        }

        int result = countOfScratchCards.values().stream().mapToInt(i -> i).sum();

        return String.valueOf(result);
    }
}