package nl.ricoapon;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class GridTest {
    @SuppressWarnings("ClassCanBeRecord")
    private static class CellWithoutEquals {
        private final int value;

        public CellWithoutEquals(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /** An initialized grid for testing. Do not mutate this grid. */
    private static Grid<CellWithoutEquals> grid;

    @BeforeAll
    static void setup() {
        String gridExample = """
                1234
                5678
                9012""";
        List<List<CellWithoutEquals>> gridList = Arrays.stream(gridExample.split("\n"))
                .map(s -> Arrays.asList(s.split("")))
                .map(l -> l.stream().map(i -> new CellWithoutEquals(Integer.parseInt(i))).toList())
                .toList();
        grid = new Grid<>(gridList);
    }

    @Test
    void getCellWorks() {
        assertThat(grid.getCell(0, 0).getValue(), equalTo(1));
        assertThat(grid.getCell(2, 2).getValue(), equalTo(1));
    }

    @Test
    void determineCoordinatesWorks() {
        assertThat(grid.determineCoordinates(grid.getCell(0, 0)), equalTo(new Pair<>(0, 0)));
        assertThat(grid.determineCoordinates(grid.getCell(1, 2)), equalTo(new Pair<>(1, 2)));
    }

    @Nested
    class DetermineHorizontalAndVerticalAdjacentCells {
        @Test
        void cornersOnlyHaveTwoAdjacentCells() {
            assertThat(grid.determineHorizontalAndVerticalAdjacentCells(grid.getCell(0, 0)),
                    containsInAnyOrder(grid.getCell(0, 1), grid.getCell(1, 0)));
            assertThat(grid.determineHorizontalAndVerticalAdjacentCells(grid.getCell(2, 3)),
                    containsInAnyOrder(grid.getCell(2, 2), grid.getCell(1, 3)));
        }

        @Test
        void boundariesHaveThreeAdjacentCells() {
            assertThat(grid.determineHorizontalAndVerticalAdjacentCells(grid.getCell(1, 0)),
                    containsInAnyOrder(grid.getCell(0, 0), grid.getCell(2, 0), grid.getCell(1, 1)));
            assertThat(grid.determineHorizontalAndVerticalAdjacentCells(grid.getCell(2, 2)),
                    containsInAnyOrder(grid.getCell(1, 2), grid.getCell(2, 3), grid.getCell(2, 1)));
        }

        @Test
        void centersHaveFourAdjacentCells() {
            assertThat(grid.determineHorizontalAndVerticalAdjacentCells(grid.getCell(1, 1)),
                    containsInAnyOrder(grid.getCell(1, 0), grid.getCell(0, 1), grid.getCell(2, 1), grid.getCell(1, 2)));
        }
    }

    @Nested
    class DetermineDiagonallyAdjacentCells {
        @Test
        void cornersHaveOnlyOneAdjacentCell() {
            assertThat(grid.determineDiagonallyAdjacentCells(grid.getCell(0, 0)),
                    containsInAnyOrder(grid.getCell(1, 1)));
        }

        @Test
        void boundariesHaveTwoAdjacentCells() {
            assertThat(grid.determineDiagonallyAdjacentCells(grid.getCell(1, 0)),
                    containsInAnyOrder(grid.getCell(0, 1), grid.getCell(2, 1)));
        }

        @Test
        void centersHaveFourAdjacentCells() {
            assertThat(grid.determineDiagonallyAdjacentCells(grid.getCell(1, 1)),
                    containsInAnyOrder(grid.getCell(0, 0), grid.getCell(2, 2), grid.getCell(0, 2), grid.getCell(2, 0)));
        }
    }

    @Test
    void determineAllAdjacentCellsWorks() {
        // Testing a single case is enough, since it is just a union of the other two methods (which are properly tested).
        assertThat(grid.determineAllAdjacentCells(grid.getCell(1, 1)),
                containsInAnyOrder(
                        grid.getCell(0, 0), grid.getCell(0, 1), grid.getCell(0, 2),
                        grid.getCell(1, 0), grid.getCell(1, 2),
                        grid.getCell(2, 0), grid.getCell(2, 1), grid.getCell(2, 2)
                ));
    }

    @Test
    void streamWorks() {
        // We know the ordering is top-left to bottom-right (left to right), but we don't need to enforce that.
        // Use containsInAnyOrder for this test.
        assertThat(grid.stream().toList(), containsInAnyOrder(
                grid.getCell(0, 0), grid.getCell(0, 1), grid.getCell(0, 2), grid.getCell(0, 3),
                grid.getCell(1, 0), grid.getCell(1, 1), grid.getCell(1, 2), grid.getCell(1, 3),
                grid.getCell(2, 0), grid.getCell(2, 1), grid.getCell(2, 2), grid.getCell(2, 3)
        ));
    }
}
