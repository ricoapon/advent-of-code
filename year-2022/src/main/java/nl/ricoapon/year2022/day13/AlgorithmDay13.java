package nl.ricoapon.year2022.day13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay13 implements Algorithm {
    interface Node {
        /**
         * @return Negative if this node is smaller than other, 0 if equal and positive
         *         if other node is smaller.
         */
        int compare(Node other);
    }

    record ValueNode(int value) implements Node {
        @Override
        public int compare(Node other) {
            if (other instanceof ValueNode v) {
                return this.value - v.value;
            } else if (other instanceof ListNode) {
                return ListNode.of(this).compare(other);
            }

            throw new RuntimeException("Cannot compare ValueNode to " + other.getClass().getSimpleName());
        }
    }

    record ListNode(List<Node> values) implements Node {
        public static ListNode of(ValueNode v) {
            return new ListNode(Collections.singletonList(v));
        }

        @Override
        public int compare(Node other) {
            if (other instanceof ValueNode v) {
                return this.compare(ListNode.of(v));
            } else if (!(other instanceof ListNode)) {
                throw new RuntimeException("Cannot compare ListNode to " + other.getClass().getSimpleName());
            }

            Iterator<Node> leftIterator = this.values.iterator();
            Iterator<Node> rightIterator = ((ListNode) other).values.iterator();

            while (true) {
                if (leftIterator.hasNext() && !rightIterator.hasNext()) {
                    return 1;
                } else if (!leftIterator.hasNext() && rightIterator.hasNext()) {
                    return -1;
                } else if (!leftIterator.hasNext() && !rightIterator.hasNext()) {
                    return 0;
                }

                Node leftNode = leftIterator.next();
                Node rightNode = rightIterator.next();

                int compareValue = leftNode.compare(rightNode);
                if (compareValue != 0) {
                    return compareValue;
                }
            }
        }
    }

    public static ListNode ofLine(String line) {
        Stack<ListNode> listNodes = new Stack<>();
        ListNode lastPopped = null;
        // Smart trick to split at the everything, but keep digits together.
        for (String c : line.split("((?=\\[)|(?<=\\[)|(?=\\])|(?<=\\])|(?=,)|(?<=,))")) {
            if ("[".equals(c)) {
                ListNode newChild = new ListNode(new ArrayList<>());

                // We don't have a parent for root.
                if (!listNodes.empty()) {
                    listNodes.peek().values.add(newChild);
                }

                listNodes.add(newChild);
            } else if ("]".equals(c)) {
                lastPopped = listNodes.pop();
            } else if (",".equals(c)) {
                // Do nothing.
            } else {
                // We must have an integer.
                listNodes.peek().values.add(new ValueNode(Integer.valueOf(c)));
            }
        }

        return lastPopped;
    }

    @Override
    public String part1(String input) {
        List<String> inputPairs = Arrays.stream(input.split("\r?\n\r?\n")).toList();
        int sum = 0;
        for (int i = 0; i < inputPairs.size(); i++) {
            String twoLines = inputPairs.get(i);
            ListNode left = ofLine(twoLines.split("\r?\n")[0]);
            ListNode right = ofLine(twoLines.split("\r?\n")[1]);

            int compareValue = left.compare(right);
            if (compareValue < 0) {
                // The first item must be 1, not 0.
                sum += (i + 1);
            }
        }
        return "" + sum;
    }

    @Override
    public String part2(String input) {
        List<ListNode> listNodes = Arrays.stream(input.split("\r?\n"))
                .filter(l -> l.length() > 0)
                .map(AlgorithmDay13::ofLine)
                .collect(Collectors.toCollection(ArrayList::new));
        ListNode element1 = ofLine("[[2]]");
        ListNode element2 = ofLine("[[6]]");
        listNodes.add(element1);
        listNodes.add(element2);

        listNodes.sort((l1, l2) -> l1.compare(l2));

        int element1Index = listNodes.indexOf(element1) + 1;
        int element2Index = listNodes.indexOf(element2) + 1;
        return "" + (element1Index * element2Index);
    }
}
