package nl.ricoapon.day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Hand2(List<Integer> cards, Type type, long bid) implements Comparable<Hand2> {

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

    public static List<Hand2> of(String input) {
        List<Hand2> hands = new ArrayList<>();
        for (String line : Arrays.stream(input.split("\\r?\\n")).toList()) {
            String hand = line.split(" ")[0];
            List<Integer> cards = Arrays.stream(hand.split(""))
                    .map(s -> {
                        s = s.replace("A", "14");
                        s = s.replace("K", "13");
                        s = s.replace("Q", "12");
                        s = s.replace("J", "0");
                        s = s.replace("T", "10");
                        return Integer.valueOf(s);
                    })
                    .toList();
            Type type = determineTypeWithJoker(cards);
            long bid = Long.valueOf(line.split(" ")[1]);

            hands.add(new Hand2(cards, type, bid));
        }

        return hands;
    }

    private static Type determineTypeWithJoker(List<Integer> cards) {
        if (!cards.contains(0)) {
            return determineType(cards);
        }

        // Jokers only need to be checked if they are equal to another used value. We
        // don't have
        // streets or any other kinds of combinations. Also, it is never possible to
        // have a combination
        // where multiple jokers take different values. More of the same card is always
        // better.
        Set<Integer> possibleValues = cards.stream()
                .filter(i -> i != 0)
                .collect(Collectors.toSet());

        List<Type> possibleTypes = possibleValues.stream()
                .map(i -> determineType(cardsWithJokersReplaced(cards, i)))
                .sorted()
                .toList();
        
        if (possibleTypes.isEmpty()) {
            // This means we have a hand of all jokers.
            return Type.FIVE_OF_A_KIND;
        }

        return possibleTypes.get(0);
    }

    private static List<Integer> cardsWithJokersReplaced(List<Integer> cards, Integer jokerValue) {
        return cards.stream()
                .map(i -> i == 0 ? jokerValue : i)
                .toList();
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
    public int compareTo(Hand2 o) {
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
