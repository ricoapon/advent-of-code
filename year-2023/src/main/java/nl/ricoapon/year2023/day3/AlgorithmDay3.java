package nl.ricoapon.year2023.day3;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay3 implements Algorithm {
    @Override
    public Object part1(String input) {
        Schematic schematic = Schematic.of(input);
        return schematic.symbols().keySet().stream()
                // Map each symbol coordinate to adjacent coordinates.
                .map(c -> c.getAdjacent(schematic.maxX(), schematic.maxY()))
                // Collect all numbers that are part of the coordinate. Note that we have to collect to a set
                // because we could have two adjacent coordinates that are part of the same number.
                .map(l -> l.stream().map(c -> schematic.numbers().get(c)).filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                // Sum all the values of the numbers inside the sets.
                .flatMapToInt(s -> s.stream().mapToInt(SchematicNumber::value))
                .sum();
    }

    @Override
    public Object part2(String input) {
        return Schematic.of(input).symbols().entrySet().stream()
                // Only look at gear symbols.
                .filter(e -> e.getValue().equals("*"))
                .map(Map.Entry::getKey)
                // Map each symbol coordinate to adjacent coordinates.
                .map(c -> c.getAdjacent(Schematic.of(input).maxX(), Schematic.of(input).maxY()))
                // Collect all numbers that are part of the coordinate. Note that we have to collect to a set
                // because we could have two adjacent coordinates that are part of the same number.
                .map(l -> l.stream().map(c -> Schematic.of(input).numbers().get(c)).filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                // A gear symbol is only valid if it is adjacent to exactly two numbers.
                .filter(s -> s.size() == 2)
                // Multiply the numbers inside each set.
                .mapToInt(s -> s.stream().mapToInt(SchematicNumber::value).reduce(1, (i1, i2) -> i1 * i2))
                // Sum the multiplied numbers to get the answer.
                .sum();
    }
}
