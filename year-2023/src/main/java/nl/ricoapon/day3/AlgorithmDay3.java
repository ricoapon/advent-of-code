package nl.ricoapon.day3;

import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay3 implements Algorithm {
    @Override
    public String part1(String input) {
        Schematic schematic = Schematic.of(input);
        int result = schematic.symbols().keySet().stream()
                // Map each symbol coordinate to adjacent coordinates.
                .map(c -> c.getAdjacent(schematic.maxX(), schematic.maxY()))
                // Collect all numbers that are part of the coordinate. Note that we have to collect to a set
                // because we could have two adjacent coordinates that are part of the same number.
                .map(l -> l.stream().map(c -> schematic.numbers().get(c)).filter(n -> n != null)
                        .collect(Collectors.toSet()))
                // Sum all the values of the numbers inside the sets.
                .flatMapToInt(s -> s.stream().mapToInt(SchematicNumber::value))
                .sum();

        return String.valueOf(result);
    }

    @Override
    public String part2(String input) {
        Schematic schematic = Schematic.of(input);

        int result = schematic.symbols().entrySet().stream()
                // Only look at gear symbols.
                .filter(e -> e.getValue().equals("*"))
                .map(e -> e.getKey())
                // Map each symbol coordinate to adjacent coordinates.
                .map(c -> c.getAdjacent(schematic.maxX(), schematic.maxY()))
                // Collect all numbers that are part of the coordinate. Note that we have to collect to a set
                // because we could have two adjacent coordinates that are part of the same number.
                .map(l -> l.stream().map(c -> schematic.numbers().get(c)).filter(n -> n != null)
                        .collect(Collectors.toSet()))
                // A gear symbol is only valid if it is adjacent to exactly two numbers.
                .filter(s -> s.size() == 2)
                // Multiply the numbers inside each set.
                .mapToInt(s -> s.stream().mapToInt(SchematicNumber::value).reduce(1, (i1, i2) -> i1 * i2))
                // Sum the multiplied numbers to get the answer.
                .sum();

        return String.valueOf(result);
    }
}
