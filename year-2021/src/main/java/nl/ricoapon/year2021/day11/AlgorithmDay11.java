package nl.ricoapon.year2021.day11;

import nl.ricoapon.Grid;
import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.List;

public class AlgorithmDay11 implements Algorithm {
    private static class Octopus {
        private int energyLevel;
        private boolean hasFlashedThisTurn = false;

        public Octopus(int energyLevel) {
            this.energyLevel = energyLevel;
        }

        public void resetEndOfTurn() {
            if (hasFlashedThisTurn) {
                energyLevel = 0;
                hasFlashedThisTurn = false;
            }
        }
    }

    private Grid<Octopus> createGrid(String input) {
        List<List<Octopus>> grid = Arrays.stream(input.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(i -> new Octopus(Integer.parseInt(i))).toList())
                .toList();
        return new Grid<>(grid);
    }

    int nrOfFlashes = 0;

    @Override
    public String part1(String input) {
        Grid<Octopus> grid = createGrid(input);


        for (int step = 0; step < 100; step++) {
            grid.stream().forEach(octopus -> increaseAndPossiblyFlash(grid, octopus));
            grid.stream().forEach(Octopus::resetEndOfTurn);
        }

        return String.valueOf(nrOfFlashes);
    }

    private void increaseAndPossiblyFlash(Grid<Octopus> grid, Octopus octopus) {
        octopus.energyLevel++;
        if (octopus.energyLevel <= 9 || octopus.hasFlashedThisTurn) {
            return;
        }

        nrOfFlashes++;
        octopus.hasFlashedThisTurn = true;

        grid.determineAllAdjacentCells(octopus)
                .forEach(o -> increaseAndPossiblyFlash(grid, o));
    }

    @Override
    public String part2(String input) {
        Grid<Octopus> grid = createGrid(input);

        int step = 0;
        boolean finished = false;

        while (!finished) {
            grid.stream().forEach(octopus -> increaseAndPossiblyFlash(grid, octopus));
            finished = grid.stream().allMatch(octopus -> octopus.hasFlashedThisTurn);
            grid.stream().forEach(Octopus::resetEndOfTurn);
            step++;
        }

        return String.valueOf(step);
    }
}
