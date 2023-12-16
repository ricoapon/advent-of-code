package nl.ricoapon.year2021.day10;

import java.util.Map;
import java.util.Stack;

import nl.ricoapon.Pair;

public class ChunkLineValidator {
    public enum State {
        /** It needs extra closing characters. */
        INCOMPLETE,
        /** An invalid closing character is found. */
        CORRUPTED,
        /** A closed character is found while we don't open any new chunks. */
        TOO_MANY_CLOSED,
        /** Everything is A-OK. */
        VALID
    }

    private final static Map<Character, Character> openCloseMap = Map.of('(', ')', '<', '>', '{', '}', '[', ']');

    public Pair<State, Character> determineState(String line) {
        Stack<Character> openedCharacters = new Stack<>();
        for (Character character : line.toCharArray()) {
            if (isOpenChar(character)) {
                openedCharacters.add(character);
            } else {
                if (openedCharacters.empty()) {
                    return new Pair<>(State.TOO_MANY_CLOSED, character);
                }

                Character previousOpenCharacter = openedCharacters.pop();
                if (openCloseMap.get(previousOpenCharacter) != character) {
                    return new Pair<>(State.CORRUPTED, character);
                }
            }
        }

        if (!openedCharacters.empty()) {
            return new Pair<>(State.INCOMPLETE, null);
        }

        return new Pair<>(State.VALID, null);
    }

    public Pair<State, String> determineStatePart2(String line) {
        Stack<Character> openedCharacters = new Stack<>();
        for (Character character : line.toCharArray()) {
            if (isOpenChar(character)) {
                openedCharacters.add(character);
            } else {
                if (openedCharacters.empty()) {
                    return new Pair<>(State.TOO_MANY_CLOSED, null);
                }

                Character previousOpenCharacter = openedCharacters.pop();
                if (openCloseMap.get(previousOpenCharacter) != character) {
                    return new Pair<>(State.CORRUPTED, null);
                }
            }
        }

        if (!openedCharacters.isEmpty()) {
            StringBuilder finishingList = new StringBuilder();
            while (!openedCharacters.empty()) {
                finishingList.append(openCloseMap.get(openedCharacters.pop()));
            }
            return new Pair<>(State.INCOMPLETE, finishingList.toString());
        }

        return new Pair<>(State.VALID, null);
    }

    private boolean isOpenChar(Character character) {
        return openCloseMap.containsKey(character);
    }
}
