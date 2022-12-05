package nl.ricoapon.day5;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay5 implements Algorithm {
    private static class CrateStacks {
        private final List<Stack<String>> stacks;

        private CrateStacks(List<Stack<String>> stacks) {
            this.stacks = stacks;
        }
        
        public static CrateStacks of(List<String> lines) {
            int nrOfStacks = (lines.get(0).length() + 1) / 4;
            List<Stack<String>> stacks = new ArrayList();
            for (int i = 0; i < nrOfStacks; i++) {
                stacks.add(new Stack<>());
            }

            // Read the lines reversed, so we start at the bottom.
            List<String> mutableLines = new ArrayList<>(lines);
            Collections.reverse(mutableLines);
            // There is a single line with integers. Removing them is not needed, since they should never be touched.
            // To avoid unexpected results, I remove it anyway.
            mutableLines.remove(0);
            for (String line : mutableLines) {
                for (int i = nrOfStacks - 1; i >= 0; i--) {
                    String crate = line.substring(i * 4 + 1, i * 4 + 2);
                    if (!" ".equals(crate)) {
                        stacks.get(i).add(crate);
                    }
                }
            }

            return new CrateStacks(stacks);
        }

        public void applyPart1(MoveOrder moveOrder) {
            for (int i = 0; i < moveOrder.nrOfCratesToMove; i++) {
                String crate = stacks.get(moveOrder.from).pop();
                stacks.get(moveOrder.to).add(crate);
            }
        }

        public void applyPart2(MoveOrder moveOrder) {
            List<String> cratesToMove = new ArrayList<>();
            for (int i = 0; i < moveOrder.nrOfCratesToMove; i++) {
                cratesToMove.add(stacks.get(moveOrder.from).pop());
            }
            Collections.reverse(cratesToMove);
            for (String crate : cratesToMove) {
                stacks.get(moveOrder.to).add(crate);
            }
        }

        public String getFinalArrangement() {
            String result = "";
            for (int i = 0; i < stacks.size(); i++) {
                result += stacks.get(i).pop();
            }
            return result;
        }
    }

    private final static Pattern MOVE_ORDER = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    private static class MoveOrder {
        private final int nrOfCratesToMove;
        private final int from;
        private final int to;

        public MoveOrder(int nrOfCratesToMove, int from, int to) {
            this.nrOfCratesToMove = nrOfCratesToMove;
            this.from = from;
            this.to = to;
        }

        public static List<MoveOrder> of(List<String> lines) {
            return lines.stream().map(line -> {
                Matcher matcher = MOVE_ORDER.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException("This should not happen");
                }

                return new MoveOrder(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)) - 1);
            }).toList();
        }
    }

    @Override
    public String part1(String input) {
        List<String> crateLines = Arrays.stream(input.split("\r?\n\r?\n")[0].split("\r?\n")).toList();
        CrateStacks crateStacks = CrateStacks.of(crateLines);

        List<String> moveOrderLines = Arrays.stream(input.split("\r?\n\r?\n")[1].split("\r?\n")).toList();
        List<MoveOrder> moveOrders = MoveOrder.of(moveOrderLines);

        for (MoveOrder moveOrder : moveOrders) {
            crateStacks.applyPart1(moveOrder);
        }

        return crateStacks.getFinalArrangement();
    }

    @Override
    public String part2(String input) {
        List<String> crateLines = Arrays.stream(input.split("\r?\n\r?\n")[0].split("\r?\n")).toList();
        CrateStacks crateStacks = CrateStacks.of(crateLines);

        List<String> moveOrderLines = Arrays.stream(input.split("\r?\n\r?\n")[1].split("\r?\n")).toList();
        List<MoveOrder> moveOrders = MoveOrder.of(moveOrderLines);

        for (MoveOrder moveOrder : moveOrders) {
            crateStacks.applyPart2(moveOrder);
        }

        return crateStacks.getFinalArrangement();
    }
}
