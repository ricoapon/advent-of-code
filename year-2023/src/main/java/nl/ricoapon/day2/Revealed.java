package nl.ricoapon.day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Revealed(int blue, int red, int green) {
    private final static Pattern COLOR = Pattern.compile(" (\\d+) (blue|red|green)");

    public static Revealed of(String subset) {
        int blue = 0;
        int green = 0;
        int red = 0;

        for (String colorAsString : subset.split(",")) {
            Matcher colorMatcher = COLOR.matcher(colorAsString);
            if (!colorMatcher.matches()) {
                throw new RuntimeException("This should not happen");
            }

            if ("red".equals(colorMatcher.group(2))) {
                red = Integer.valueOf(colorMatcher.group(1));
            } else if ("blue".equals(colorMatcher.group(2))) {
                blue = Integer.valueOf(colorMatcher.group(1));
            } else if ("green".equals(colorMatcher.group(2))) {
                green = Integer.valueOf(colorMatcher.group(1));
            }
        }

        return new Revealed(blue, red, green);
    }
}
