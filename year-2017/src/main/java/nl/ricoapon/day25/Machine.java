package nl.ricoapon.day25;

import java.util.HashMap;
import java.util.Map;

public class Machine {
    private final Map<Integer, Integer> tape;
    private int cursorIndex;

    public Machine() {
        tape = new HashMap<>();
        tape.put(0, 0);
        cursorIndex = 0;
    }

    public void moveCursor(int delta) {
        cursorIndex += delta;
    }

    public void writeValue(int value) {
        tape.put(cursorIndex, value);
    }

    public int getValueUnderCursor() {
        return tape.getOrDefault(cursorIndex, 0);
    }

    public long getDiagnosticCheckSum() {
        return tape.values().stream().filter(i -> i == 1).count();
    }
}
