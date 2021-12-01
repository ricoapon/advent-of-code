package nl.ricoapon.day1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay1 implements Algorithm {
    @Override
    public String part1(String input) {
        return calculateNumberOfIncreases(parseInput(input));
    }

    private String calculateNumberOfIncreases(List<Integer> list) {
        int increasedCounter = 0;
        Integer previousOne = null;
        for (Integer i : list) {
            if (previousOne != null && previousOne < i) {
                // We increased!
                increasedCounter++;
            }

            previousOne = i;
        }
        return String.valueOf(increasedCounter);
    }

    @Override
    public String part2(String input) {
        List<Integer> threeMeasurements = new ArrayList<>();
        List<Integer> inputList = parseInput(input);
        for (int i = 0; i <= inputList.size() - 1; i++) {
            List<Integer> threeIndexes = Arrays.asList(i, i - 1, i - 2);
            for (Integer index : threeIndexes) {
                if (index < 0) {
                    continue;
                }

                // Make sure calling get will return a number.
                if (threeMeasurements.size() == index) {
                    threeMeasurements.add(0);
                }

                threeMeasurements.set(index, threeMeasurements.get(index) + inputList.get(i));
            }
        }

        return calculateNumberOfIncreases(threeMeasurements);
    }

    private List<Integer> parseInput(String input) {
        return Arrays.stream(input.split("\n")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
