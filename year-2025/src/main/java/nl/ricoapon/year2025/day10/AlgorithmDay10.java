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

import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

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
        // +-------------+ . +----+ . +---+
        // | 0 0 0 0 1 1 | . | x0 | . | 3 |
        // | 0 1 0 0 0 1 | . | x1 | . | 5 |
        // | 0 0 1 1 1 0 | * | x2 | = | 4 |
        // | 1 1 0 1 0 0 | . | x3 | . | 7 |
        // +-------------+ . | x4 | . +---+
        // ................. | x5 |
        // ..................+----+
        //
        // This is just a set of linear equations that we can solve.
        // I use a library, because that is a good experience.

        long total = 0;
        for (String line : input.split("\\r?\\n")) {
            double[] yValues = State.ofPart2(line).indicators.stream().mapToDouble(i -> (double) i).toArray();
            List<List<Integer>> toggles = togglesOf(line);
            double[][] A = convertToMatrix(toggles, yValues.length);
            int nVars = A[0].length;

            // Build the model
            ExpressionsBasedModel model = new ExpressionsBasedModel();

            // Add variables to the model/
            Variable[] vars = new Variable[nVars];
            for (int j = 0; j < nVars; j++) {
                vars[j] = model.addVariable("x" + j)
                        .integer(true) // mark as integer
                        .lower(0); // x_j ≥ 0 (non-negative integers)
            }

            // Add constraints A[i]·x == y[i]
            for (int i = 0; i < A.length; i++) {
                Expression expr = model.addExpression("eq" + i).level(yValues[i]);
                for (int j = 0; j < nVars; j++) {
                    expr.set(vars[j], A[i][j]);
                }
            }

            // printReadableExpressions(model);

            // Add an objective to minimize the sum of all the variables.
            Expression objective = model.addExpression("MinimizeSum");
            for (Variable v : vars) {
                objective.set(v, 1);
            }
            objective.weight(1);

            // Solve the model
            Optimisation.Result result = model.minimise();

            if (result.getState().isFeasible()) {
                // I had some issues with rounding. Apparently, the output could get "27.9999999" for example.
                // If you get the value, it will return 27. So we need to return the double and then round to
                // the nearest integer. I found this by verifying the solutions.
                if (!verifySolution(toggles, result, State.ofPart2(line))) {
                    throw new RuntimeException();
                }

                for (int j = 0; j < nVars; j++) {
                    total += Math.round(result.get(j).doubleValue());
                }
            } else {
                // The input is constructed by AoC to always have a solution.
                throw new RuntimeException("Could not find solution!");
            }
        }

        return total;
    }

    // Compose matrix A from the Ax=y equation.
    private double[][] convertToMatrix(List<List<Integer>> toggles, int sizeY) {
        double[][] coefficients = new double[sizeY][toggles.size()];

        // Each List<Integer> converts into a vertical entry in the matrix.
        for (int i = 0; i < toggles.size(); i++) {
            var toggle = toggles.get(i);
            for (int j = 0; j < sizeY; j++) {
                var value = toggle.contains(j) ? 1 : 0;
                coefficients[j][i] = value;
            }
        }

        return coefficients;
    }

    public void printReadableExpressions(ExpressionsBasedModel model) {
        List<Variable> vars = model.getVariables();

        for (Expression expr : model.getExpressions()) {
            StringBuilder sb = new StringBuilder();

            boolean first = true;
            for (Variable v : vars) {
                double coef = expr.get(v).doubleValue();
                if (Math.abs(coef) > 1e-12) {
                    if (!first)
                        sb.append(" + ");
                    sb.append(coef).append("*").append(v.getName());
                    first = false;
                }
            }

            if (expr.isEqualityConstraint()) {
                sb.append(" = ").append(expr.getLowerLimit());
            } else {
                if (expr.getLowerLimit() != null)
                    sb.append(" >= ").append(expr.getLowerLimit());
                if (expr.getUpperLimit() != null)
                    sb.append(" <= ").append(expr.getUpperLimit());
            }

            System.out.println(sb);
        }
    }

    private boolean verifySolution(List<List<Integer>> toggles, Optimisation.Result result, State targetState) {
        State state = new State(Collections.nCopies(targetState.indicators.size(), 0));
        for (int i = 0; i < toggles.size(); i++) {
            var toggle = toggles.get(i);
            
            // Apply the toggle X times, where X is the value of the result.
            for (int x = 0; x < Math.round(result.get(i).doubleValue()); x++) {
                state = state.applyPart2(toggle);
            }
        }

        return state.equals(targetState);
    }
}
