package nl.ricoapon.graphs;

import java.util.List;

public record SimplePath<N>(List<N> nodes) {
    public SimplePath(List<N> nodes) {
        this.nodes = List.copyOf(nodes);
    }
}
