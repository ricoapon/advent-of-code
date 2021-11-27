package nl.ricoapon.framework.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataProvider {
    /**
     * @param day The day of which the test data is created.
     * @return A list of all test data that can be used for running tests.
     */
    public static List<TestData> determineTestDataForDay(int day) {
        List<TestData> testData = new ArrayList<>();
        List<InputFile> inputFiles = InputFileProvider.getAllInputFiles(day);
        if (inputFiles.size() == 0) {
            return testData;
        }

        List<String> expectedAnswers;
        try {
            expectedAnswers = Arrays.stream(FileUtil.readContentFromResource("/day" + day + "/expected.txt").split("\n")).toList();
        } catch (RuntimeException e) {
            // No expected list could be found. So we just continue with the next day.
            return testData;
        }

        int nrOfExamplesInPart1 = (int) inputFiles.stream()
                .filter(InputFile::isExample)
                .filter(f -> f.part() == 1)
                .count();
        int nrOfExamplesInPart2 = (int) inputFiles.stream()
                .filter(InputFile::isExample)
                .filter(f -> f.part() == 2)
                .count();

        if (expectedAnswers.size() == 0) {
            return testData;
        }
        testData.add(new TestData(day, 1, inputFiles.get(0).readContent(), expectedAnswers.get(0)));

        for (int i = 0; i < Math.min(nrOfExamplesInPart1, expectedAnswers.size() - 1); i++) {
            testData.add(new TestData(day,
                    1,
                    inputFiles.get(1 + i).readContent(),
                    expectedAnswers.get(1 + i)));
        }

        if (expectedAnswers.size() == nrOfExamplesInPart1 + 1) {
            return testData;
        }

        testData.add(new TestData(day, 2, inputFiles.get(0).readContent(), expectedAnswers.get(nrOfExamplesInPart1 + 1)));

        for (int i = 0; i < Math.min(nrOfExamplesInPart2, expectedAnswers.size() - 2 - nrOfExamplesInPart1); i++) {
            testData.add(new TestData(day,
                    2,
                    inputFiles.get(1 + nrOfExamplesInPart1 + i).readContent(),
                    expectedAnswers.get(2 + nrOfExamplesInPart1 + i)));
        }

        return testData;
    }
}
