package nl.ricoapon.year2024.day11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay11 implements Algorithm {

    private List<Long> next(Long i) {
        if (i == 0L) {
            return List.of(1L);
        }
        if (i.toString().length() % 2 == 0) {
            var stringI = i.toString();
            var length = i.toString().length();
            return List.of(
                    Long.valueOf(stringI.substring(0, length / 2)),
                    Long.valueOf(stringI.substring(length / 2)));
        }
        return List.of(i * 2024);
    }

    private final Map<Pair<Long, Integer>, Long> cache = new HashMap<>();

    private Long countAfterBlinks(Long value, Integer timesToCount) {
        if (timesToCount == 0) {
            return 1L;
        }

        var key = new Pair<>(value, timesToCount);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        Long result = next(value).stream().mapToLong(v -> countAfterBlinks(v, timesToCount - 1)).sum();
        cache.put(key, result);

        return result;
    }

    private Long countAfterBlinks(List<Long> values, Integer timesToCount) {
        return values.stream().mapToLong(v -> countAfterBlinks(v, timesToCount)).sum();
    }

    @Override
    public Object part1(String input) {
        List<Long> values = Stream.of(input.split(" ")).map(Long::valueOf).toList();
        return countAfterBlinks(values, 25);
    }

    @Override
    public Object part2(String input) {
        List<Long> values = Stream.of(input.split(" ")).map(Long::valueOf).toList();
        return countAfterBlinks(values, 75);
    }
}
