package nl.ricoapon.day13;

import nl.ricoapon.Coordinate2D;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransparentPaper {
    private final Map<Coordinate2D, Boolean> dots;

    public TransparentPaper(List<Coordinate2D> dots) {
        this.dots = dots.stream().collect(Collectors.toMap(dot -> dot, (dot) -> true));
    }

    public void fold(FoldInstruction foldInstruction) {
        if (foldInstruction.foldY()) {
            foldY(foldInstruction.value());
        } else {
            foldX(foldInstruction.value());
        }
    }

    private void foldY(int value) {
        List<Coordinate2D> dotsToFold = dots.keySet().stream()
                .filter(dot -> dot.y() > value)
                .toList();

        for (Coordinate2D dot : dotsToFold) {
            dots.remove(dot);
            dots.put(new Coordinate2D(dot.x(), 2 * value - dot.y()), true);
        }
    }

    private void foldX(int value) {
        List<Coordinate2D> dotsToFold = dots.keySet().stream()
                .filter(dot -> dot.x() > value)
                .toList();

        for (Coordinate2D dot : dotsToFold) {
            dots.remove(dot);
            dots.put(new Coordinate2D(2 * value - dot.x(), dot.y()), true);
        }
    }

    public int countNumberOfDots() {
        return dots.values().size();
    }

    public String print() {
        StringBuilder stringBuilder = new StringBuilder();
        int maxX = dots.keySet().stream().max(Comparator.comparingInt(Coordinate2D::x)).orElseThrow().x();
        int maxY = dots.keySet().stream().max(Comparator.comparingInt(Coordinate2D::y)).orElseThrow().y();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                if (dots.containsKey(new Coordinate2D(x, y))) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
