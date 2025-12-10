package nl.ricoapon.year2025.day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay10 implements Algorithm {
    private record State(List<Integer> indicators) {
        public static State of(String line) {
            String relevantPart = line.substring(1, line.indexOf("]"));
            return new State(Stream.of(relevantPart.split("")).map(s -> s.equals("#") ? 1 : 0).toList());
        }

        public static State ofPart2(String line) {
            String relevantPart = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
            return new State(Stream.of(relevantPart.split(",")).map(Integer::parseInt).toList());
        }

        public State apply(List<Integer> toggleIndices) {
            List<Integer> newIndicators = new ArrayList<>(indicators);
            for (int toggleIndex : toggleIndices) {
                newIndicators.set(toggleIndex, Math.abs(newIndicators.get(toggleIndex) - 1));
            }
            return new State(newIndicators);
        }

        public State applyPart2(List<Integer> toggleIndices) {
            List<Integer> newIndicators = new ArrayList<>(indicators);
            for (int toggleIndex : toggleIndices) {
                newIndicators.set(toggleIndex, newIndicators.get(toggleIndex) + 1);
            }
            return new State(newIndicators);
        }

        // Is only called by the target state.
        public boolean canOtherStateReachSelf(State other) {
            Iterator<Integer> self = indicators.iterator();
            Iterator<Integer> otherIt = other.indicators.iterator();
            while (self.hasNext()) {
                if (self.next() < otherIt.next()) {
                    return false;
                }
            }
            return true;
        }
    }

    private List<List<Integer>> togglesOf(String line) {
        String relevantPart = line.substring(line.indexOf("]") + 2, line.indexOf(" {"));
        return Stream.of(relevantPart.split(" ")).map(this::toggleOf).toList();
    }

    private List<Integer> toggleOf(String toggleAsString) {
        String relevantPart = toggleAsString.substring(1, toggleAsString.length() - 1);
        return Stream.of(relevantPart.split(",")).map(Integer::parseInt).toList();
    }

    private Set<State> getReachableStates(State state, List<List<Integer>> toggles, boolean isPart1, State target) {
        if (isPart1) {
            return toggles.stream().map(state::apply).collect(Collectors.toSet());
        }

        // For part 2, we can reach an infinite amount of states. However, we want our
        // algorithm to end. Note that every integer can only increase, which means that
        // if we go over the target, we can never go back. So we consider this state to
        // be unreachable. This ensures we are living in a finite set of states.
        return toggles.stream().map(state::applyPart2).filter(target::canOtherStateReachSelf)
                .collect(Collectors.toSet());
    }

    // This is just Dijkstra algorithm, but where we don't know the graph
    // beforehand. Each adjacent node is calculated on the spot and we always use
    // weight 1 for each edge.
    private int determineShortestPath(State target, List<List<Integer>> toggles, boolean isPart1) {
        State source = new State(Collections.nCopies(target.indicators.size(), 0));
        Map<State, Integer> distance = new HashMap<>();
        distance.put(source, 0);

        PriorityQueue<State> queue = new PriorityQueue<>(
                Comparator.comparingInt(state -> distance.getOrDefault(state, Integer.MAX_VALUE)));
        queue.add(source);

        Set<State> seen = new HashSet<>();
        seen.add(source);

        while (!queue.isEmpty()) {
            State top = queue.poll();

            for (State adjacent : getReachableStates(top, toggles, isPart1, target)) {
                int currentDistance = 1 + distance.getOrDefault(top, Integer.MAX_VALUE);

                if (currentDistance < distance.getOrDefault(adjacent, Integer.MAX_VALUE)) {
                    distance.put(adjacent, currentDistance);
                }

                if (!seen.contains(adjacent)) {
                    queue.add(adjacent);
                    seen.add(adjacent);
                }
            }
        }

        return distance.get(target);
    }

    @Override
    public Object part1(String input) {
        int total = 0;
        for (String line : input.split("\\r?\\n")) {
            State targetState = State.of(line);
            List<List<Integer>> toggles = togglesOf(line);
            total += determineShortestPath(targetState, toggles, true);
        }

        return total;
    }

    @Override
    public Object part2(String input) {
        // This doesn't work, too many possibilities.
        // Now I see it is just math: consider linear equations in N variables.
        // We can convert each problem into a solvable Ax=y where A and y are given.
        // Consider: (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}. This is:
        //
        // +-------------------+   +----+   +---+
        // | 0  0  0  0  1  1  |   | x1 |   | 3 |
        // | 0  1  0  0  0  1  |   | x2 |   | 5 |
        // | 0  0  1  1  1  0  | * | x3 | = | 4 |
        // | 1  1  0  1  0  0  |   | x4 |   | 7 |
        // +-------------------+   | x5 |   +---+
        //                         | x6 |
        //                         +----+
        //
        // Now we can just solve this with Gaussian elimination.
        // We just need to make sure that the end result is only positive integers.
        return "x";
    }
}
