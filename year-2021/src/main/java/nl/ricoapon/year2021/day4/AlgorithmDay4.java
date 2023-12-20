package nl.ricoapon.year2021.day4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay4 implements Algorithm {
    @Override
    public Object part1(String input) {
        List<String> inputAsList = Arrays.stream(input.split("\n\n")).collect(Collectors.toCollection(ArrayList::new));
        List<Integer> drawnNumbers = Arrays.stream(inputAsList.get(0).split(",")).map(Integer::parseInt).toList();
        inputAsList.remove(0);
        List<BingoBoard> bingoBoards = inputAsList.stream().map(BingoBoard::of).toList();

        for (Integer drawnNumber : drawnNumbers) {
            for (BingoBoard bingoBoard : bingoBoards) {
                bingoBoard.markDrawnNumbers(drawnNumber);
                if (bingoBoard.hasBingo()) {
                    return String.valueOf(bingoBoard.score(drawnNumber));
                }
            }
        }

        throw new RuntimeException("This should not happen.");
    }

    @Override
    public Object part2(String input) {
        List<String> inputAsList = Arrays.stream(input.split("\n\n")).collect(Collectors.toCollection(ArrayList::new));
        List<Integer> drawnNumbers = Arrays.stream(inputAsList.get(0).split(",")).map(Integer::parseInt).toList();
        inputAsList.remove(0);
        List<BingoBoard> bingoBoards = inputAsList.stream().map(BingoBoard::of).collect(Collectors.toCollection(ArrayList::new));

        for (Integer drawnNumber : drawnNumbers) {
            // Create a copy over which we loop, since we want to remove elements from the list.
            for (BingoBoard bingoBoard : bingoBoards.stream().toList()) {
                bingoBoard.markDrawnNumbers(drawnNumber);
                if (bingoBoards.size() > 1 && bingoBoard.hasBingo()) {
                    bingoBoards.remove(bingoBoard);
                } else if (bingoBoards.size() == 1 && bingoBoard.hasBingo()) {
                    return String.valueOf(bingoBoard.score(drawnNumber));
                }
            }
        }

        throw new RuntimeException("This should not happen.");
    }
}
