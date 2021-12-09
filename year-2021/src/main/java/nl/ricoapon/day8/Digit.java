package nl.ricoapon.day8;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Digit {
    private final Set<Segment> segments;

    public static Digit of(String segmentsAsString) {
        Set<Segment> segments = segmentsAsString.chars().mapToObj(c -> Segment.of((char) c)).collect(Collectors.toSet());
        return new Digit(segments);
    }

    public Digit(Set<Segment> segments) {
        this.segments = segments;
    }

    public Set<Segment> intersection(Digit digit) {
        Set<Segment> intersectionSet = new HashSet<>(segments);
        intersectionSet.retainAll(digit.segments);
        return intersectionSet;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Digit digit = (Digit) o;

        return Objects.equals(segments, digit.segments);
    }

    @Override
    public int hashCode() {
        return segments != null ? segments.hashCode() : 0;
    }
}
