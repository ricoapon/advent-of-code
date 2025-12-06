package nl.ricoapon.year2025.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay6 implements Algorithm {

    private record AllCalculations(List<List<Long>> integersWrongOrder, List<String> operations) {
        public static AllCalculations of(String input) {
            List<String> lines = Stream.of(input.split("\\r?\\n")).collect(Collectors.toCollection(ArrayList::new));
            String lineWithOperations = lines.removeLast();
            List<List<Long>> integersWrongOrder = lines.stream()
                    .map(s -> Stream.of(s.trim().split("\\s+")).map(Long::parseLong).toList())
                    .toList();
            List<String> operations = Stream.of(lineWithOperations.split("\\s+")).toList();
            return new AllCalculations(integersWrongOrder, operations);
        }

        public int totalNrOfcalculations() {
            return operations.size();
        }

        public Calculation getCalculation(int i) {
            List<Long> integersRightOrder = new ArrayList<>();
            for (int j = 0; j < integersWrongOrder.size(); j++) {
                integersRightOrder.add(integersWrongOrder.get(j).get(i));
            }
            return new Calculation(integersRightOrder, operations.get(i));
        }
    }

    private record Calculation(List<Long> integers, String operation) {
        public long execute() {
            BinaryOperator<Long> operator;
            if (operation.equals("+")) {
                operator = (a, b) -> a + b;
            } else if (operation.equals("*")) {
                operator = (a, b) -> a * b;
            } else {
                throw new RuntimeException("Found non-existing operation: " + operation);
            }
            return integers.stream().reduce(operator).orElseThrow();
        }
    }

    private record AllCalculationsPart2(List<String> integerLines, List<Integer> columnSizes, List<String> operations) {
        public static AllCalculationsPart2 of(String input) {
            List<String> lines = Stream.of(input.split("\\r?\\n")).collect(Collectors.toCollection(ArrayList::new));
            String lineWithOperations = lines.removeLast();
            List<String> operations = Stream.of(lineWithOperations.split("\\s+")).toList();

            // We know the length of a column by the number of spaces between operator.
            List<Integer> columnSizes = new ArrayList<>();
            int lastColumnSize = 0;
            for (char c : lineWithOperations.toCharArray()) {
                if (c != ' ') {
                    columnSizes.add(lastColumnSize);
                    lastColumnSize = 0;
                } else {
                    lastColumnSize++;
                }
            }
            // We added 0 at the start, which we can remove.
            columnSizes.removeFirst();
            // We assume that there is an additional space to split the rows, which is not
            // present at the final column. So we add one more for this.
            columnSizes.add(lastColumnSize + 1);

            return new AllCalculationsPart2(lines, columnSizes, operations);
        }

        public int totalNrOfcalculations() {
            return operations.size();
        }

        public Calculation getCalculation(int column) {
            int columnStart = columnSizes.stream().limit(column).mapToInt(x -> x + 1).sum();
            List<Long> integers = new ArrayList<>();
            for (int j = 0; j < columnSizes.get(column); j++) {
                StringBuilder s = new StringBuilder();
                for (String line : integerLines) {
                    s.append(line.toCharArray()[columnStart + j]);
                }
                integers.add(Long.valueOf(s.toString().trim()));
            }
            return new Calculation(integers, operations.get(column));
        }
    }

    @Override
    public Object part1(String input) {
        AllCalculations allCalculations = AllCalculations.of(input);
        long total = 0;
        for (int i = 0; i < allCalculations.totalNrOfcalculations(); i++) {
            total += allCalculations.getCalculation(i).execute();
        }
        return total;
    }

    @Override
    public Object part2(String input) {
        AllCalculationsPart2 allCalculations = AllCalculationsPart2.of(input);
        long total = 0;
        for (int i = 0; i < allCalculations.totalNrOfcalculations(); i++) {
            total += allCalculations.getCalculation(i).execute();
        }
        return total;
    }
}
