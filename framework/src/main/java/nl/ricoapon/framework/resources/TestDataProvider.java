package nl.ricoapon.framework.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDataProvider {

    private static Expected expectedOfDay(int year, int day) {
        String content = FileUtil.readContentFromResource(getPath(year, day, "expected.yml"));
        return Expected.of(content);
    }

    /**
     * @param year The year of which the test data is created.
     * @param day The day of which the test data is created.
     * @return A list of all test data that can be used for running tests.
     */
    public static List<TestData> determineTestDataForDay(int year, int day) {
        Expected expected = expectedOfDay(year, day);

        List<TestData> testData = new ArrayList<>();
        testData.addAll(createTestDataForSinglePart(year, day, 1, expected.part1));
        testData.addAll(createTestDataForSinglePart(year, day, 2, expected.part2));

        return testData;
    }

    private static List<TestData> createTestDataForSinglePart(int year, int day, int part, Map<String, String> filesToExpectedResultMap) {
        List<TestData> testData = new ArrayList<>();

        for (Map.Entry<String, String> entry : filesToExpectedResultMap.entrySet()) {
            String expected = entry.getValue();
            String filename = entry.getKey().endsWith(".txt") ? entry.getKey() : entry.getKey() + ".txt";

            testData.add(new TestData(
                    year,
                    day,
                    part,
                    FileUtil.readContentFromResource(getPath(year, day, filename)),
                    expected,
                    isExample(filename)));
        }

        return testData;
    }

    private static String getPath(int year, int day, String filename) {
        return "/year" + year + "/day" + day + "/" + filename;
    }

    private static boolean isExample(String filename) {
        return !"input.txt".equals(filename);
    }
}

