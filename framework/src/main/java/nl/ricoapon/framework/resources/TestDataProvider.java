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

        List<String> allExpectedAnswers;
        try {
            allExpectedAnswers = Arrays.stream(FileUtil.readContentFromResource("/day" + day + "/expected.txt").split("\n")).toList();
        } catch (RuntimeException e) {
            // No expected list could be found. So we just continue with the next day.
            return testData;
        }

        List<InputFile> inputFilesForPart1 = inputFiles.stream()
                .filter(InputFile::canBeUsedForPart1)
                .toList();
        List<String> expectedAnswersPart1 = allExpectedAnswers.subList(0, Math.min(inputFilesForPart1.size(), allExpectedAnswers.size()));
        testData.addAll(createTestDataForSinglePart(day, 1, inputFilesForPart1, expectedAnswersPart1));

        List<InputFile> inputFilesForPart2 = inputFiles.stream()
                .filter(InputFile::canBeUsedForPart2)
                .toList();
        List<String> expectedAnswersPart2 = allExpectedAnswers.subList(expectedAnswersPart1.size(),
                expectedAnswersPart1.size() + Math.min(inputFilesForPart2.size(), allExpectedAnswers.size() - expectedAnswersPart1.size()));
        testData.addAll(createTestDataForSinglePart(day, 2, inputFilesForPart2, expectedAnswersPart2));

        return testData;
    }

    private static List<TestData> createTestDataForSinglePart(int day, int part, List<InputFile> inputFiles, List<String> expectedAnswers) {
        List<TestData> testData = new ArrayList<>();

        for (int i = 0; i < Math.min(inputFiles.size(), expectedAnswers.size()); i++) {
            InputFile inputFile = inputFiles.get(i);
            String expected = expectedAnswers.get(i);
            testData.add(new TestData(day, part, inputFile.readContent(), expected, inputFile.isExample()));
        }

        return testData;
    }
}
