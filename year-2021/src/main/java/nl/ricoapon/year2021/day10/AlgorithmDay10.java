package nl.ricoapon.year2021.day10;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay10 implements Algorithm {
    private final static Map<Character, Integer> pointMapPart1 = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);
    private final static Map<Character, BigInteger> pointMapPart2 = Map.of(')', BigInteger.ONE, ']', BigInteger.TWO, '}', BigInteger.valueOf(3), '>', BigInteger.valueOf(4));

    @Override
    public Object part1(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).toList();
        ChunkLineValidator chunkLineValidator = new ChunkLineValidator();

        int result = lines.stream()
                .map(chunkLineValidator::determineState)
                .filter(state -> state.getL() == ChunkLineValidator.State.CORRUPTED)
                .mapToInt(state -> pointMapPart1.get(state.getR()))
                .sum();

        return String.valueOf(result);
    }

    @Override
    public Object part2(String input) {
        List<String> lines = Arrays.stream(input.split("\n")).toList();
        ChunkLineValidator chunkLineValidator = new ChunkLineValidator();

        List<BigInteger> results = lines.stream()
                .map(chunkLineValidator::determineStatePart2)
                .filter(state -> state.getL() == ChunkLineValidator.State.INCOMPLETE)
                .map(state -> calculateScore(state.getR()))
                .sorted()
                .toList();

        return String.valueOf(results.get(results.size() / 2));
    }

    private BigInteger calculateScore(String completionString) {
        BigInteger score = BigInteger.ZERO;
        for (Character c : completionString.toCharArray()) {
            score = score.multiply(BigInteger.valueOf(5));
            score = score.add(pointMapPart2.get(c));
        }
        return score;
    }
}
