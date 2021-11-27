package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDataProviderTest {

    @Test
    void day1IsCorrectlySortedAndCreated() {
        List<TestData> testData = TestDataProvider.determineTestDataForDay(1);
        assertEquals(5, testData.size());
        assertEquals(new TestData(1, 1, "input", "?"), testData.get(0));
        assertEquals(new TestData(1, 1, "part1_example1", "1"), testData.get(1));
        assertEquals(new TestData(1, 1, "part1_example2", "2"), testData.get(2));
        assertEquals(new TestData(1, 2, "input", "?"), testData.get(3));
        assertEquals(new TestData(1, 2, "part2_example1", "3"), testData.get(4));
    }

    @Test
    void singeFileIsAllowed() {
        List<TestData> testData = TestDataProvider.determineTestDataForDay(2);
        assertEquals(1, testData.size());
        assertEquals(new TestData(2, 1, "input", "?"), testData.get(0));
    }
}
