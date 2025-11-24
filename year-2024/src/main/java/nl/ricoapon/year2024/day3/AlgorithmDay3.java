package nl.ricoapon.year2024.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay3 implements Algorithm {

    private final static Pattern MUL = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    @Override
    public Object part1(String input) {
        Matcher m = MUL.matcher(input);

        Long sumOfMul = 0L;

        while (m.find()) {
            sumOfMul += Integer.valueOf(m.group(1)) * Integer.valueOf(m.group(2));
        }

        return sumOfMul;
    }

    @Override
    public Object part2(String input) {
        // Instead of making the regex more complex (which is doable I think), we can just cut out any pieces of text "dont().*do()".
        // Then we can re-use part 1, which feels good.
        
        while (input.contains("don't()")) {
            var index = input.indexOf("don't()");
            var first = input.substring(0, index);
            var last = input.substring(input.indexOf("do()", index) + "do()".length());

            input = first + last;
        }

        return part1(input);
    }
}
