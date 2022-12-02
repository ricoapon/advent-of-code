package nl.ricoapon.day2;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay2 implements Algorithm {
    enum Shape {
        ROCK("A", "X", 1, "C"),
        PAPER("B", "Y", 2, "A"),
        SCISSORS("C", "Z", 3, "B");
        final String value1;
        final String value2;
        final Integer score;
        final String beats;

        private Shape(String value1, String value2, Integer score, String beats) {
            this.value1 = value1;
            this.value2 = value2;
            this.score = score;
            this.beats = beats;
        }

        public int beats(Shape other) {
            if (this == other) {
                return 0;
            }

            Shape beatsOtherShape = Shape.value(this.beats);
            if (other == beatsOtherShape) {
                return 1;
            }

            return -1;
        }


        public static Shape value(String input) {
            for (Shape s : Shape.values()) {
                if (s.value1.equals(input) || s.value2.equals(input)) {
                    return s;
                }
            }
            throw new RuntimeException("Should never happen");
        }
    }

    @Override
    public String part1(String input) {
        int score = 0;
        for (String line : input.split("\r?\n")) {
            Shape oppponent = Shape.value(String.valueOf(line.toCharArray()[0]));
            Shape you = Shape.value(String.valueOf(line.toCharArray()[2]));

            int result = you.beats(oppponent);
            score += you.score;
            if (result == 0) {
                score += 3;
            } else if (result > 0) {
                score += 6;
            }
        }
        return String.valueOf(score);
    }

    @Override
    public String part2(String input) {
        int score = 0;
        for (String line : input.split("\r?\n")) {
            Shape oppponent = Shape.value(String.valueOf(line.toCharArray()[0]));
            String wantedResultString = String.valueOf(line.toCharArray()[2]);
            int wantedResult;
            if (wantedResultString.equals("X")) {
                wantedResult = -1;
            } else if (wantedResultString.equals("Y")) {
                wantedResult = 0;
            } else {
                wantedResult = 1;
            }

            Shape shapeToChoose = null;
            for (Shape shape : Shape.values()) {
                if (shape.beats(oppponent) == wantedResult) {
                    shapeToChoose = shape;
                    break;
                }
            }

            score += shapeToChoose.score;
            if (wantedResult == 0) {
                score += 3;
            } else if (wantedResult > 0) {
                score += 6;
            }
        }
        return String.valueOf(score);
    }
}
