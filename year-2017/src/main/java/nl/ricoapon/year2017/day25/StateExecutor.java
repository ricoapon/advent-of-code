package nl.ricoapon.year2017.day25;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ClassCanBeRecord")
public class StateExecutor {
    private final String stateIdentifier;

    private final int writeThisValueIfCurrentValueIsZero;
    private final int moveIfCurrentValueIsZero;
    private final String nextStateIdentifierIfCurrentValueIsZero;

    private final int writeThisValueIfCurrentValueIsOne;
    private final int moveIfCurrentValueIsOne;
    private final String nextStateIdentifierIfCurrentValueIsOne;

    public StateExecutor(String stateIdentifier, int writeThisValueIfCurrentValueIsZero, int moveIfCurrentValueIsZero, String nextStateIdentifierIfCurrentValueIsZero, int writeThisValueIfCurrentValueIsOne, int moveIfCurrentValueIsOne, String nextStateIdentifierIfCurrentValueIsOne) {
        this.stateIdentifier = stateIdentifier;
        this.writeThisValueIfCurrentValueIsZero = writeThisValueIfCurrentValueIsZero;
        this.moveIfCurrentValueIsZero = moveIfCurrentValueIsZero;
        this.nextStateIdentifierIfCurrentValueIsZero = nextStateIdentifierIfCurrentValueIsZero;
        this.writeThisValueIfCurrentValueIsOne = writeThisValueIfCurrentValueIsOne;
        this.moveIfCurrentValueIsOne = moveIfCurrentValueIsOne;
        this.nextStateIdentifierIfCurrentValueIsOne = nextStateIdentifierIfCurrentValueIsOne;
    }

    @SuppressWarnings("RegExpRepeatedSpace")
    private final static Pattern STATE_PATTERN = Pattern.compile("""
            In state (.):
              If the current value is 0:
                - Write the value (\\d)\\.
                - Move one slot to the (right|left)\\.
                - Continue with state (.)\\.
              If the current value is 1:
                - Write the value (\\d)\\.
                - Move one slot to the (right|left)\\.
                - Continue with state (.)\\."""
    );

    public static StateExecutor of(String input) {
        Matcher matcher = STATE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new RuntimeException("Input doesn't match state pattern: " + input);
        }

        return new StateExecutor(
                matcher.group(1),
                Integer.parseInt(matcher.group(2)),
                "left".equals(matcher.group(3)) ? -1 : 1,
                matcher.group(4),
                Integer.parseInt(matcher.group(5)),
                "left".equals(matcher.group(6)) ? -1 : 1,
                matcher.group(7));
    }

    public String execute(Machine machine) {
        if (machine.getValueUnderCursor() == 0) {
            machine.writeValue(writeThisValueIfCurrentValueIsZero);
            machine.moveCursor(moveIfCurrentValueIsZero);
            return nextStateIdentifierIfCurrentValueIsZero;
        } else {
            machine.writeValue(writeThisValueIfCurrentValueIsOne);
            machine.moveCursor(moveIfCurrentValueIsOne);
            return nextStateIdentifierIfCurrentValueIsOne;
        }
    }

    public String getStateIdentifier() {
        return stateIdentifier;
    }
}
