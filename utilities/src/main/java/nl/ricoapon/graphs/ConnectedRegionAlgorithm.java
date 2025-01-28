package nl.ricoapon.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.GridWithCoordinates;
import nl.ricoapon.Pair;

public class ConnectedRegionAlgorithm {

    // Basically T = Pair<Coordinate2D, C>
    public <C> Set<Set<Pair<Coordinate2D, C>>> determineAllOrthogonallyConnectedRegions(GridWithCoordinates<C> grid, BiPredicate<Pair<Coordinate2D, C>, Pair<Coordinate2D, C>> isAdjacent) {
        Set<Pair<Coordinate2D, C>> allNodes = grid.stream().collect(Collectors.toSet());
        Function<Pair<Coordinate2D, C>, Set<Pair<Coordinate2D, C>>> getAdjacentNodes = (p) -> {
            return grid.determineHorizontalAndVerticalAdjacentCoordinates(p.getL()).stream()
            .map(c -> new Pair<>(c, grid.getCell(c)))
            .filter(adjacentP -> isAdjacent.test(p, adjacentP))
            .collect(Collectors.toSet());
        };

        return determineAllConnectedRegions(allNodes, getAdjacentNodes);
    }

    public <T> Set<Set<T>> determineAllConnectedRegions(Set<T> allNodes, Function<T, Set<T>> getAdjacentNodes) {
        Set<Set<T>> result = new HashSet<>();
        Set<T> undiscoveredNodes = new HashSet<>(allNodes);

        while (!undiscoveredNodes.isEmpty()) {
            Set<T> region = new HashSet<>();
            result.add(region);
            var node = undiscoveredNodes.iterator().next();
            undiscoveredNodes.remove(node);

            dfsAddConnectedNodes(undiscoveredNodes, node, region, getAdjacentNodes);
        }

        return result;
    }

    private <T> void dfsAddConnectedNodes(Set<T> undiscoveredNodes, T currentNode, Set<T> region,
            Function<T, Set<T>> getAdjacentNodes) {
        region.add(currentNode);
        for (var adjacentNode : getAdjacentNodes.apply(currentNode)) {
            if (undiscoveredNodes.contains(adjacentNode)) {
                undiscoveredNodes.remove(adjacentNode);
                dfsAddConnectedNodes(undiscoveredNodes, adjacentNode, region, getAdjacentNodes);
            }
        }
    }
}
