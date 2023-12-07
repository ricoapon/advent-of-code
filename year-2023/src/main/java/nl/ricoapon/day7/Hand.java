package nl.ricoapon.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Hand(List<Integer> cards, Type type, long bid) implements Comparable<Hand> {

    public static enum Type {
        // Sorted from high to low.
        FIVE_OF_A_KIND, // 1
        FOUR_OF_A_KIND, // 2
        FULL_HOUSE, // 2
        THREE_OF_A_KIND, // 3
        TWO_PAIR, // 3
        ONE_PAIR, // 4
        HIGH_CARD // 5
    }

    public static List<Hand> of(String input) {
        List<Hand> hands = new ArrayList<>();
        for (String line : Arrays.stream(input.split("\\r?\\n")).toList()) {
            String hand = line.split(" ")[0];
            List<Integer> cards = Arrays.stream(hand.split(""))
                    .map(s -> {
                        s = s.replace("A", "14");
                        s = s.replace("K", "13");
                        s = s.replace("Q", "12");
                        s = s.replace("J", "11");
                        s = s.replace("T", "10");
                        return Integer.valueOf(s);
                    })
                    .toList();
            Type type = determineType(cards);
            long bid = Long.valueOf(line.split(" ")[1]);

            hands.add(new Hand(cards, type, bid));
        }

        return hands;
    }

    private static Type determineType(List<Integer> cards) {
        Map<Integer, Long> frequencyMap = cards.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        if (frequencyMap.size() == 1) {
            return Type.FIVE_OF_A_KIND;
        } else if (frequencyMap.size() == 3) {
            if (frequencyMap.values().containsAll(List.of(2L, 1L))) {
                return Type.TWO_PAIR;
            }
            
            return Type.THREE_OF_A_KIND;
        } else if (frequencyMap.size() == 2) {
            if (frequencyMap.values().containsAll(List.of(2L, 3L))) {
                return Type.FULL_HOUSE;
            }
            return Type.FOUR_OF_A_KIND;
        } else if (frequencyMap.size() == 4) {
            return Type.ONE_PAIR;
        }

        return Type.HIGH_CARD;
    }

    @Override
    public int compareTo(Hand o) {
        if (type != o.type) {
            return type.compareTo(o.type);
        }

        for (int i = 0; i < 5; i++) {
            int a = cards.get(i);
            int b = o.cards.get(i);
            if (a != b) {
                return b - a;
            }
        }

        return 0;
    }
}
