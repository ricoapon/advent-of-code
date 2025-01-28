package nl.ricoapon.year2024.day12;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;
import nl.ricoapon.graphs.ConnectedRegionAlgorithm;

public class AlgorithmDay12 implements Algorithm {

    private int determineFenceCost(Set<Pair<Coordinate2D, String>> region, GridWithCoordinates<String> grid) {
        Set<Coordinate2D> coordinateRegion = region.stream().map(Pair::getL).collect(Collectors.toSet());

        int perimeter = 0;
        for (var coordinate : coordinateRegion) {
            var adjacentCoordinates = grid.determineHorizontalAndVerticalAdjacentCoordinates(coordinate);
            adjacentCoordinates.retainAll(coordinateRegion);
            perimeter += 4 - adjacentCoordinates.size();
        }

        int area = coordinateRegion.size();

        return area * perimeter;
    }

    @Override
    public Object part1(String input) {
        final GridWithCoordinates<String> grid = GridWithCoordinates.ofString(input, s -> s);

        // Adjacent means the symbol in the cell is identical.
        BiPredicate<Pair<Coordinate2D, String>, Pair<Coordinate2D, String>> isAdjacent = (p1, p2) -> {
            return p1.getR().equals(p2.getR());
        };

        Set<Set<Pair<Coordinate2D, String>>> regions = new ConnectedRegionAlgorithm()
                .determineAllOrthogonallyConnectedRegions(grid, isAdjacent);

        return regions.stream().mapToInt(r -> determineFenceCost(r, grid)).sum();
    }

    private int determineFenceCostPart2(Set<Pair<Coordinate2D, String>> region, GridWithCoordinates<String> grid) {
        Set<Coordinate2D> coordinateRegion = region.stream().map(Pair::getL).collect(Collectors.toSet());

        int nrOfSides = 0;
        List<Function<Coordinate2D, Coordinate2D>> orientations = List.of(
                (c) -> new Coordinate2D(c.x() + 1, c.y()),
                (c) -> new Coordinate2D(c.x() - 1, c.y()),
                (c) -> new Coordinate2D(c.x(), c.y() + 1),
                (c) -> new Coordinate2D(c.x(), c.y() - 1));

        // Given an orientation, we can list all nodes that have a fence in that
        // orientation. Then we can find regions inside this sub-region. Each region now
        // corresponds to a merged set of fences, which is a full side.
        for (var orientation : orientations) {
            Set<Coordinate2D> subRegion = coordinateRegion.stream()
                    .filter(c -> !coordinateRegion.contains(orientation.apply(c)))
                    .collect(Collectors.toSet());

            Function<Coordinate2D, Set<Coordinate2D>> getAdjacentNodes = (c) -> {
                var adjacentNodes = grid.determineHorizontalAndVerticalAdjacentCoordinates(c);
                adjacentNodes.retainAll(subRegion);
                return adjacentNodes;
            };

            nrOfSides += new ConnectedRegionAlgorithm()
                    .determineAllConnectedRegions(subRegion, getAdjacentNodes).size();
        }

        int area = coordinateRegion.size();

        return nrOfSides * area;
    }

    @Override
    public Object part2(String input) {
        final GridWithCoordinates<String> grid = GridWithCoordinates.ofString(input, s -> s);

        // Adjacent means the symbol in the cell is identical.
        BiPredicate<Pair<Coordinate2D, String>, Pair<Coordinate2D, String>> isAdjacent = (p1, p2) -> {
            return p1.getR().equals(p2.getR());
        };

        Set<Set<Pair<Coordinate2D, String>>> regions = new ConnectedRegionAlgorithm()
                .determineAllOrthogonallyConnectedRegions(grid, isAdjacent);

        return regions.stream().mapToInt(r -> determineFenceCostPart2(r, grid)).sum();
    }
}
