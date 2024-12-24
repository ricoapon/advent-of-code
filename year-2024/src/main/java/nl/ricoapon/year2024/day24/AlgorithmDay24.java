package nl.ricoapon.year2024.day24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay24 implements Algorithm {
    enum Operation {
        AND((a, b) -> a & b),
        OR((a, b) -> a | b),
        XOR((a, b) -> a ^ b);

        private BiFunction<Boolean, Boolean, Boolean> executeMethod;

        Operation(BiFunction<Boolean, Boolean, Boolean> executeMethod) {
            this.executeMethod = executeMethod;
        }

        public boolean execute(boolean a, boolean b) {
            return executeMethod.apply(a, b);
        }
    }

    private record CircuitOperation(String input1, String input2, Operation operation, String output) {

        private final static Pattern REGEX = Pattern.compile("(.{3}) (AND|OR|XOR) (.{3}) -> (.{3})");
        public static CircuitOperation of(String line) {
            var matcher = REGEX.matcher(line);
            if (!matcher.matches()) {
                throw new RuntimeException("Line doesn't match CircuitOperation: " + line);
            }

            return new CircuitOperation(
                    matcher.group(1),
                    matcher.group(3),
                    Operation.valueOf(matcher.group(2)),
                    matcher.group(4));
        }
    }

    private static class Circuit {
        private final Map<String, Boolean> values;
        private final List<CircuitOperation> circuitOperations;

        public static Circuit of(String input) {
            var items = input.split("\\r?\\n\\r?\\n");
            Map<String, Boolean> values = Stream.of(items[0].split("\\r?\\n"))
                    .map(s -> new Pair<>(s.substring(0, 3), "1".equals(s.substring(5))))
                    .collect(Collectors.toMap(Pair::getL, Pair::getR));
            List<CircuitOperation> circuitOperations = Stream.of(items[1].split("\\r?\\n"))
                    .map(CircuitOperation::of)
                    .toList();

            return new Circuit(values, circuitOperations);
        }

        public Circuit(Map<String, Boolean> values, List<CircuitOperation> circuitOperations) {
            // Input needs to be mutable.
            this.values = new HashMap<>(values);
            this.circuitOperations = new ArrayList<>(circuitOperations);
        }

        public void calculateAllCircuitOperations() {
            while (!circuitOperations.isEmpty()) {
                // Create a copy we can loop over, so we can remove items from the original
                // list.
                List<CircuitOperation> copy = circuitOperations.stream().toList();
                for (CircuitOperation circuitOperation : copy) {
                    if (!values.containsKey(circuitOperation.input1) || !values.containsKey(circuitOperation.input2)) {
                        continue;
                    }

                    var outputValue = circuitOperation.operation.execute(
                            values.get(circuitOperation.input1),
                            values.get(circuitOperation.input2));

                    values.put(circuitOperation.output, outputValue);
                    circuitOperations.remove(circuitOperation);
                }
            }
        }

        public Long determineFinalValue() {
            // Get all "z" values.
            List<Boolean> valuesInOrder = values.entrySet().stream()
                    .filter(e -> e.getKey().startsWith("z"))
                    // We can sort alphabetically, which works just fine.
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .toList();
            
            // Convert this from binary.
            long total = 0;
            for (int i = 0; i < valuesInOrder.size(); i++) {
                if (valuesInOrder.get(i)) {
                    total += Math.pow(2, i);
                }
            }
            return total;
        }
    }

    @Override
    public Object part1(String input) {
        Circuit circuit = Circuit.of(input);
        circuit.calculateAllCircuitOperations();
        return circuit.determineFinalValue();
    }

    @Override
    public Object part2(String input) {
        return "x";
    }
}
