package nl.ricoapon.framework.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for providing all the possible {@link InputFile} that can be found.
 */
public class InputFileProvider {

    /**
     * @param day The day of which the files should be found.
     * @return A list of {@link InputFile} that are available as input for the algorithm.
     */
    public static List<InputFile> getAllInputFiles(int day) {
        String directory = "/day" + day + "/";
        List<InputFile> inputFiles = new ArrayList<>();

        InputFile inputFile = InputFile.of(directory + "input.txt");
        if (inputFile.exists()) {
            inputFiles.add(inputFile);
        } else {
            return inputFiles;
        }

        InputFile exampleFile = InputFile.of(directory + "example.txt");
        if (exampleFile.exists()) {
            inputFiles.add(exampleFile);
        }

        addAllExamplesForPart(directory, 1, inputFiles);
        addAllExamplesForPart(directory, 2, inputFiles);

        return inputFiles;
    }

    /**
     * Modifies the given list to add {@link InputFile}s for each example file.
     * @param directory  The directory to search in.
     * @param part       The part of the examples.
     * @param inputFiles The list to add the objects to.
     */
    private static void addAllExamplesForPart(String directory, int part, List<InputFile> inputFiles) {
        // We cannot get a list of resources in a directory. So we just loop from 1 to higher.
        // If examples in between are missing, the files are ignored.
        int exampleNumber = 1;
        InputFile inputFile = InputFile.of(directory + "part" + part + "_example" + exampleNumber + ".txt");
        while (inputFile.exists()) {
            inputFiles.add(inputFile);
            exampleNumber++;
            inputFile = InputFile.of(directory + "part" + part + "_example" + exampleNumber + ".txt");
        }
    }
}
