package nl.ricoapon.year2023.day6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Race(long time, long distance) {
    public static List<Race> of(String input) {
        String timesAsString = input.split("\\r?\\n")[0].substring("Time:".length());
        String distancesAsString = input.split("\\r?\\n")[1].substring("Distance:".length());

        List<Long> times = determineNumbers(timesAsString);
        List<Long> distances = determineNumbers(distancesAsString);

        List<Race> races = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), distances.get(i)));
        }
        return races;
    }

    private static List<Long> determineNumbers(String line) {
        return Arrays.stream(line.split("\\s+"))
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .toList();
    }
}
