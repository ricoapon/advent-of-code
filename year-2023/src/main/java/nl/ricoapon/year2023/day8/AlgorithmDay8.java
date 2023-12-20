package nl.ricoapon.year2023.day8;

import nl.ricoapon.year2023.day8.Documents.Pair;
import nl.ricoapon.framework.Algorithm;

import java.util.List;
import java.util.function.Function;

public class AlgorithmDay8 implements Algorithm {
    @Override
    public Object part1(String input) {
        Documents documents = Documents.of(input);

        return countSteps(documents, "AAA");
    }

    private int countSteps(Documents documents, String startingNode) {
        String currentNode = startingNode;
        int instructionsSize = documents.instructions().size();

        int count = 0;
        while (!currentNode.endsWith("Z")) {
            Function<Pair, String> instruction = documents.instructions().get(count % instructionsSize);
            Pair node = documents.nodes().get(currentNode);

            currentNode = instruction.apply(node);
            count++;
        }

        return count;
    }

    @Override
    public Object part2(String input) {
        Documents documents = Documents.of(input);

        // Count the number of steps for every node into a list.
        List<Long> nodesCount = documents.nodes().keySet().stream()
            .filter(s -> s.endsWith("A"))
            .map(s -> (long) this.countSteps(documents, s))
            .toList();
        
        // Now we need to find the LCM which is the answer.
        return nodesCount.stream().reduce(AlgorithmDay8::lcm).orElseThrow();
    }

    // Copy and pasted this from the internet.
    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }    
}
