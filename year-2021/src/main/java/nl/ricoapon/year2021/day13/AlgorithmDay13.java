package nl.ricoapon.year2021.day13;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmDay13 implements Algorithm {
    private List<Coordinate2D> readDots(String input) {
        return Arrays.stream(input.split("\n"))
                .map(s -> s.split(","))
                .map(l -> new Coordinate2D(Integer.parseInt(l[0]), Integer.parseInt(l[1])))
                .toList();
    }

    private List<FoldInstruction> readFoldInstructions(String input) {
        Pattern pattern = Pattern.compile("fold along ([xy])=(\\d+)");
        return Arrays.stream(input.split("\n"))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> new FoldInstruction(matcher.group(1).equals("y"), Integer.parseInt(matcher.group(2))))
                .toList();
    }

    @Override
    public Object part1(String input) {
        String[] twoPartInput = input.split("\n\n");
        TransparentPaper transparentPaper = new TransparentPaper(readDots(twoPartInput[0]));
        List<FoldInstruction> foldInstructions = readFoldInstructions(twoPartInput[1]);
        transparentPaper.fold(foldInstructions.get(0));
        return String.valueOf(transparentPaper.countNumberOfDots());
    }

    @Override
    public Object part2(String input) {
        String[] twoPartInput = input.split("\n\n");

        TransparentPaper transparentPaper = new TransparentPaper(readDots(twoPartInput[0]));
        readFoldInstructions(twoPartInput[1]).forEach(transparentPaper::fold);

        // The YAML reader trims the expected output as well, so this is needed here.
        return transparentPaper.print().trim();
    }
}
