package nl.ricoapon.year2023.day8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Documents(List<Function<Pair, String>> instructions, Map<String, Pair> nodes) {
    private final static Pattern NODE = Pattern.compile("(...) = \\((...), (...)\\)");

    public record Pair(String left, String right) {
    }

    public static Documents of(String input) {
        List<Function<Pair, String>> instructions = Arrays.stream(input.split("\\r?\\n\\r?\\n")[0].split(""))
                .map(Documents::convert).toList();
        
        Map<String, Pair> nodes = new HashMap<>();
        for (String line : input.split("\\r?\\n\\r?\\n")[1].split("\\r?\\n")) {
            Matcher m = NODE.matcher(line);
            if (!m.matches()) {
                throw new RuntimeException("This should not happen");
            }

            nodes.put(m.group(1), new Pair(m.group(2), m.group(3)));
        }

        return new Documents(instructions, nodes);
    }

    private static Function<Pair, String> convert(String s) {
        if ("R".equals(s)) {
            return Pair::right;
        } else if ("L".equals(s)) {
            return Pair::left;
        }
        throw new RuntimeException("Could not parse " + s);
    }
}
