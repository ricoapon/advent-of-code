package nl.ricoapon.day3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.ricoapon.Coordinate2D;

public record Schematic(Map<Coordinate2D, SchematicNumber> numbers, Map<Coordinate2D, String> symbols, int maxX,
        int maxY) {
    public static Schematic of(String input) {
        Map<Coordinate2D, SchematicNumber> numbers = new HashMap<>();
        Map<Coordinate2D, String> symbols = new HashMap<>();
        int maxX = 0;
        int maxY = 0;

        SchematicNumber currentNumber = null;
        Coordinate2D currentCoordinate = new Coordinate2D(0, 0);
        for (String line : input.split("\\r?\\n")) {
            for (String c : line.split("")) {
                if (c.matches("\\d")) {
                    if (currentNumber == null) {
                        currentNumber = new SchematicNumber(0, new ArrayList<>());
                    }
                    currentNumber.coordinates().add(currentCoordinate);
                    currentNumber = new SchematicNumber(currentNumber.value() * 10 + Integer.valueOf(c),
                            currentNumber.coordinates());
                } else if (".".equals(c)) {
                    if (currentNumber != null) {
                        for (Coordinate2D coordinate : currentNumber.coordinates()) {
                            numbers.put(coordinate, currentNumber);
                        }
                        currentNumber = null;
                    }
                } else {
                    if (currentNumber != null) {
                        for (Coordinate2D coordinate : currentNumber.coordinates()) {
                            numbers.put(coordinate, currentNumber);
                        }
                        currentNumber = null;
                    }
                    symbols.put(currentCoordinate, c);
                }

                currentCoordinate = new Coordinate2D(currentCoordinate.x() + 1, currentCoordinate.y());
            }
            maxX = currentCoordinate.x() - 1;
            currentCoordinate = new Coordinate2D(0, currentCoordinate.y() + 1);
        }
        maxY = currentCoordinate.y() - 1;

        return new Schematic(numbers, symbols, maxX, maxY);
    }
}
