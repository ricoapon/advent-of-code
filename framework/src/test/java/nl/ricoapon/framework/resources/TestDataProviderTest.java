package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDataProviderTest {

    @Test
    void testDataIsSortedInOrderOfTheYamlFile() {
        List<TestData> testData = TestDataProvider.determineTestDataForDay(2000, 1);
        assertEquals(7, testData.size());
        assertEquals(new TestData(2000, 1, 1, "input", "x1", false), testData.get(0));
        assertEquals(new TestData(2000, 1, 1, "example", "1", true), testData.get(1));
        assertEquals(new TestData(2000, 1, 1, "part1_example1", "2", true), testData.get(2));
        assertEquals(new TestData(2000, 1, 1, "part1_example2", "3", true), testData.get(3));
        assertEquals(new TestData(2000, 1, 2, "input", "x2", false), testData.get(4));
        assertEquals(new TestData(2000, 1, 2, "example", "4", true), testData.get(5));
        assertEquals(new TestData(2000, 1, 2, "part2_example1", "5", true), testData.get(6));
    }

    @Test
    void txtExtensionIsAddedOnlyIfMissingFromFilename() {
        List<TestData> testData = TestDataProvider.determineTestDataForDay(2000, 2);
        assertEquals(2, testData.size());
        assertEquals(new TestData(2000, 2, 1, "input", "x", false), testData.get(0));
        assertEquals(new TestData(2000, 2, 2, "input", "y", false), testData.get(1));
    }
}
