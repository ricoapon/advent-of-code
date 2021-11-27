package nl.ricoapon.cli.actions.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Action to generate all the files needed for a single day.
 */
public class Generate {
    private final File homeDirectory;
    private final PathCreator pathCreator;
    private final int day;

    public Generate(File homeDirectory, int year, int day) {
        this.homeDirectory = homeDirectory;
        this.day = day;
        this.pathCreator = new PathCreator(year, day);
    }

    public void generate() {
        step1_createAllFiles();
        step2_fillJavaClasses();
    }

    /**
     * Creates the following files in the right locations:
     * <ul>
     *     <li>{@code AlgorithmDayX.java}</li>
     *     <li>{@code AlgorithmDayXTest.java}</li>
     *     <li>{@code expected.txt}</li>
     *     <li>{@code input.txt}</li>
     *     <li>{@code part1_example1.txt}</li>
     *     <li>{@code part2_example1.txt}</li>
     * </ul>
     */
    private void step1_createAllFiles() {
        createFile(pathCreator.algorithmDay());
        createFile(pathCreator.algorithmDayTest());
        createFile(pathCreator.expected());
        createFile(pathCreator.input());
        createFile(pathCreator.part1example1());
        createFile(pathCreator.part2example1());
    }

    /**
     * Fill the following files with the relevant Java code:
     * <ul>
     *     <li>{@code AlgorithmDayX}</li>
     *     <li>{@code AlgorithmDayXTest}</li>
     * </ul>
     */
    private void step2_fillJavaClasses() {
        TemplateGenerator templateGenerator = new TemplateGenerator();
        fillContentOfFile(pathCreator.algorithmDay(), templateGenerator.generateAlgorithmDay(day));
        fillContentOfFile(pathCreator.algorithmDayTest(), templateGenerator.generateAlgorithmDayTest(day));
    }

    private void createFile(String pathRelativeToHomeDirectory) {
        File file = new File(homeDirectory, pathRelativeToHomeDirectory);
        try {
            // Make sure to only create directories of the parent file, otherwise the file itself will be created as a directory.
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();

            if (!file.createNewFile())  {
                throw new IOException("File " + file.getAbsolutePath() + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("File creation failed", e);
        }
    }

    private void fillContentOfFile(String pathRelativeToHomeDirectory, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(homeDirectory, pathRelativeToHomeDirectory)), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file", e);
        }
    }
}
