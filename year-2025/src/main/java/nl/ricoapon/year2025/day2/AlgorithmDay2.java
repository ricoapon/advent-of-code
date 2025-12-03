package nl.ricoapon.year2025.day2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay2 implements Algorithm {
    @Override
    public Object part1(String input) {
        return Stream.of(input.split(",")).map(Range::of).flatMap(Range::findInvalidIds).mapToLong(x -> x).sum();
    }

    // Integers won't work with this input.
    private record Range(long min, long max) {
        public static Range of(String rangeAsString) {
            long min = Long.valueOf(rangeAsString.substring(0, rangeAsString.indexOf("-")));
            long max = Long.valueOf(rangeAsString.substring(rangeAsString.indexOf("-") + 1));
            return new Range(min, max);
        }

        public Stream<Long> findInvalidIds() {
            return LongStream.range(min, max + 1).filter(this::isInvalid).boxed();
        }

        private boolean isInvalid(long l) {
            String s = ("" + l);

            // By definition, an invalid ID must have even length.
            if (s.length() % 2 != 0) {
                return false;
            }

            String left = s.substring(0, s.length() / 2);
            return (left + left).equals(s);
        }

        public Stream<Long> findInvalidIdsPart2() {
            return LongStream.range(min, max + 1).filter(this::isInvalidPart2).boxed();
        }

        private boolean isInvalidPart2(long l) {
            String s = ("" + l);
            List<Integer> possibleLenghts = divisors(s.length());
            return possibleLenghts.stream().filter(i -> isRepeatedSequence(s, i)).findAny().isPresent();
        }

        private boolean isRepeatedSequence(String s, int repeatedSequenceLength) {
            String part = s.substring(0, repeatedSequenceLength);
            StringBuilder total = new StringBuilder();
            for (int i = 0; i < s.length() / repeatedSequenceLength; i++) {
                total.append(part);
            }
            return total.toString().equals(s);
        }

        // Do not add "n" itself, since that will never be a solution to the puzzle.
        private List<Integer> divisors(int n) {
            List<Integer> divisors = new ArrayList<>();
            for (int i = 1; 2 * i <= n; i++) {
                if (n % i == 0) {
                    divisors.add(i);
                }
            }
            return divisors;
        }
    }

    @Override
    public Object part2(String input) {
        return Stream.of(input.split(",")).map(Range::of).flatMap(Range::findInvalidIdsPart2).mapToLong(x -> x).sum();
    }
}
