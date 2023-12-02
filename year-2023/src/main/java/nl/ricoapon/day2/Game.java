package nl.ricoapon.day2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Game(int id, List<Revealed> revealedList) {
    private final static Pattern GAME = Pattern.compile("Game (\\d+)");

    public static Game of(String line) {
        Matcher gameMatcher = GAME.matcher(line.split(":")[0]);
        if (!gameMatcher.matches()) {
            throw new RuntimeException("This should not happen");
        }

        int id = Integer.valueOf(gameMatcher.group(1));
        List<Revealed> revealedList = Arrays.stream(line.split(":")[1].split(";"))
                .map(Revealed::of)
                .toList();

        return new Game(id, revealedList);
    }
}
