package nl.ricoapon;

public record Pair<L, R>(L l, R r) {

    public L getL() {
        return l;
    }

    public R getR() {
        return r;
    }
}
