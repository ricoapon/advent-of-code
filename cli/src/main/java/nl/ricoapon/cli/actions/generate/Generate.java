package nl.ricoapon.cli.actions.generate;

import nl.ricoapon.cli.FileUtil;
import nl.ricoapon.cli.actions.session.AdventOfCodeSessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Action to generate all the files needed for a single day.
 */
public class Generate {
    private final FileInstanceCreator fileInstanceCreator;
    private final int year;
    private final int day;

    public Generate(File homeDirectory, int year, int day) {
        this.fileInstanceCreator = new FileInstanceCreator(homeDirectory, year, day);
        this.year = year;
        this.day = day;
    }

    public void generate() {
        step1_createAllFiles();
        step2_fillJavaClasses();
        step3_downloadAndFillInput();
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
        FileUtil.createFile(fileInstanceCreator.algorithmDay());
        FileUtil.createFile(fileInstanceCreator.algorithmDayTest());
        FileUtil.createFile(fileInstanceCreator.expected());
        FileUtil.createFile(fileInstanceCreator.input());
        FileUtil.createFile(fileInstanceCreator.part1example1());
        FileUtil.createFile(fileInstanceCreator.part2example1());
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
        FileUtil.appendContentOfFile(fileInstanceCreator.algorithmDay(), templateGenerator.generateAlgorithmDay(day));
        FileUtil.appendContentOfFile(fileInstanceCreator.algorithmDayTest(), templateGenerator.generateAlgorithmDayTest(day));
    }

    private void step3_downloadAndFillInput() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://adventofcode.com/" + year + "/day/" + day + "/input"))
                .header("Cookie", AdventOfCodeSessionManager.INSTANCE.getSession())
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not download input file", e);
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException("Response was not 200");
        }

        FileUtil.appendContentOfFile(fileInstanceCreator.input(), response.body());
    }
}
