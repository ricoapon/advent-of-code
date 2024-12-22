package nl.ricoapon.year2024.day13;

import nl.ricoapon.framework.Algorithm;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AlgorithmDay13 implements Algorithm {

    record Machine(
            Long ax, Long ay,
            Long bx, Long by,
            Long px, Long py
    ) {
        private final static Pattern REGEX = Pattern.compile(
               """
                   Button A: X\\+(\\d+), Y\\+(\\d+)
                   Button B: X\\+(\\d+), Y\\+(\\d+)
                   Prize: X=(\\d+), Y=(\\d+)""");
        public static Machine of(String lines) {
            Matcher matcher = REGEX.matcher(lines);
            if (!matcher.matches()) {
                throw new RuntimeException("Invalid machine line: " + lines);
            }

            return new Machine(
                    Long.parseLong(matcher.group(1)),
                    Long.parseLong(matcher.group(2)),
                    Long.parseLong(matcher.group(3)),
                    Long.parseLong(matcher.group(4)),
                    Long.parseLong(matcher.group(5)),
                    Long.parseLong(matcher.group(6))
            );
        }

        public static Machine ofPart2(String lines) {
            Matcher matcher = REGEX.matcher(lines);
            if (!matcher.matches()) {
                throw new RuntimeException("Invalid machine line: " + lines);
            }

            return new Machine(
                    Long.parseLong(matcher.group(1)),
                    Long.parseLong(matcher.group(2)),
                    Long.parseLong(matcher.group(3)),
                    Long.parseLong(matcher.group(4)),
                    Long.parseLong(matcher.group(5)) + 10000000000000L,
                    Long.parseLong(matcher.group(6)) + 10000000000000L
            );
        }

        // Returns empty if the machine cannot be solved.
        public Optional<Long> determineCost() {
            var top = ax * py - ay * px;
            var bottom = ax * by - bx * ay;
            if (top % bottom != 0) {
                return Optional.empty();
            }
            var pressB = top / bottom;
            if (pressB > 100) {
                return Optional.empty();
            }

            var top2 = px - pressB * bx;
            if (top2 % ax != 0) {
                return Optional.empty();
            }

            var pressA = top2 / ax;
            if (pressA > 100) {
                return Optional.empty();
            }

            return Optional.of(pressB + 3 * pressA);
        }

        public Optional<Long> determineCostPart2() {
            var top = ax * py - ay * px;
            var bottom = ax * by - bx * ay;
            if (top % bottom != 0) {
                return Optional.empty();
            }
            var pressB = top / bottom;

            var top2 = px - pressB * bx;
            if (top2 % ax != 0) {
                return Optional.empty();
            }

            var pressA = top2 / ax;

            return Optional.of(pressB + 3 * pressA);
        }
    }

    @Override
    public Object part1(String input) {
        return Stream.of(input.split("\\r?\\n\\r?\\n"))
                .map(Machine::of)
                .map(Machine::determineCost)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum();
    }

    @Override
    public Object part2(String input) {
        return Stream.of(input.split("\\r?\\n\\r?\\n"))
                .map(Machine::ofPart2)
                .map(Machine::determineCostPart2)
                .filter(Optional::isPresent)
                .mapToLong(Optional::get)
                .sum();
    }
}
