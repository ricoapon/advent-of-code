package nl.ricoapon.year2022.day10;

public class CRTDrawingDevice {
    private int cycle = 0;
    private int X = 1;
    private final StringBuilder stringBuilder = new StringBuilder();

    public void noop() {
        print();
        cycle++;
    }

    public void add(int v) {
        print();
        cycle++;
        print();
        X += v;
        cycle++;
    }

    private void print() {
        int horizontalX = cycle % 40;

        if (horizontalX % 40 == 0 && cycle > 0) {
            stringBuilder.append("\n");
        }

        if (Math.abs(X - horizontalX) <= 1) {
            stringBuilder.append("#");
        } else {
            stringBuilder.append(".");
        }
    }

    public String getDrawing() {
        return stringBuilder.toString();
    }
}
