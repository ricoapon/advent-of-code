package nl.ricoapon.year2021.day8;

import java.util.HashMap;
import java.util.Map;

public enum Segment {
    TOP('a'),
    TOP_LEFT('b'),
    TOP_RIGHT('c'),
    MIDDLE('d'),
    BOTTOM_LEFT('e'),
    BOTTOM_RIGHT('f'),
    BOTTOM('g');

    private final char rep;

    Segment(char rep) {
        this.rep = rep;
    }

    private final static Map<Character, Segment> charSegmentMap;

    static {
        charSegmentMap = new HashMap<>();
        for (Segment segment : Segment.values()) {
            charSegmentMap.put(segment.rep, segment);
        }
    }

    public static Segment of(char c) {
        return charSegmentMap.get(c);
    }
}
