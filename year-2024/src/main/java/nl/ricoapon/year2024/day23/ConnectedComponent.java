package nl.ricoapon.year2024.day23;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.graphs.SimpleGraph;

// The class is not actually immutable, but we should consider it so.
public class ConnectedComponent {
    private final SimpleGraph<String> g;
    private final List<String> nodes;

    public ConnectedComponent(SimpleGraph<String> g, List<String> nodes) {
        this.g = g;
        this.nodes = nodes;
    }

    public ConnectedComponent add(String node) {
        // We do not check here if the node is adjacent to all nodes, since that should
        // have been checked already.
        List<String> newNodes = new ArrayList<>(nodes);
        newNodes.add(node);
        return new ConnectedComponent(g, newNodes);
    }

    public boolean isAdjacentTo(String node) {
        // If the node is inside the component already, we see it as non-adjacent.
        // This is easier for the algorithm.
        if (nodes.contains(node)) {
            return false;
        }

        Set<String> adjacentNodes = g.getAdjacentNodes(node);
        return nodes.stream().allMatch(adjacentNodes::contains);
    }

    public int size() {
        return nodes.size();
    }

    public boolean containsNodeStartingWithT() {
        return nodes.stream().anyMatch(s -> s.startsWith("t"));
    }

    public Stream<String> stream() {
        return nodes.stream();
    }

    // We consider two objects equal if the nodes are equal as a set, not as a list.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedComponent that = (ConnectedComponent) o;

        return Objects.equals(g, that.g) && Objects.equals(toSet(nodes), toSet(that.nodes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, toSet(nodes));
    }

    private static Set<String> toSet(List<String> nodes) {
        return nodes.stream().collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
