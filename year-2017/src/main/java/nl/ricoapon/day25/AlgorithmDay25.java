package nl.ricoapon.day25;

import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmDay25 implements Algorithm {
    private final static Pattern STARTING_INPUT = Pattern.compile("Begin in state (.)\\.\nPerform a diagnostic checksum after (\\d+) steps.");

    @Override
    public String part1(String input) {
        List<String> inputParts = Arrays.stream(input.split("\n\n")).toList();
        Matcher matcher = STARTING_INPUT.matcher(inputParts.get(0));
        if (!matcher.matches()) {
            throw new RuntimeException("Input seems incorrect.");
        }

        String startingStateExecutorIdentifier = matcher.group(1);
        int nrOfStepsToExecute = Integer.parseInt(matcher.group(2));

        Map<String, StateExecutor> stateExecutorInstances = new HashMap<>();
        for (String stateExecutorAsString : inputParts.subList(1, inputParts.size())) {
            StateExecutor stateExecutor = StateExecutor.of(stateExecutorAsString);
            stateExecutorInstances.put(stateExecutor.getStateIdentifier(), stateExecutor);
        }

        Machine machine = new Machine();
        StateExecutor stateExecutor = stateExecutorInstances.get(startingStateExecutorIdentifier);

        for (int i = 0; i < nrOfStepsToExecute; i++) {
            stateExecutor = stateExecutorInstances.get(stateExecutor.execute(machine));
        }

        return String.valueOf(machine.getDiagnosticCheckSum());
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
