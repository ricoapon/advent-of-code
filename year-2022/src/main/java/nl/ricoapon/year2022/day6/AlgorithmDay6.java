package nl.ricoapon.year2022.day6;

import java.util.HashSet;
import java.util.Set;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {
    @Override
    public Object part1(String input) {
        return calculateDistinctMarker(input, 4);
    }

    private String calculateDistinctMarker(String input, int nrOfDistinctCharacters) {
        char[] inputArray = input.toCharArray();
        for (int i = 0; i < inputArray.length - nrOfDistinctCharacters; i++) {
            Set<Character> seenCharacters = new HashSet<>();
            for (int j = 0; j < nrOfDistinctCharacters; j++) {
                seenCharacters.add(inputArray[i + j]);
            }
            if (seenCharacters.size() == nrOfDistinctCharacters) {
                return "" + (i + nrOfDistinctCharacters);
            }
        }
        throw new RuntimeException("This should never happen");
    }

    @Override
    public Object part2(String input) {
        return calculateDistinctMarker(input, 14);
    }
}
