package nl.ricoapon.cli.commands.generate;

import nl.ricoapon.cli.MyFileUtils;
import nl.ricoapon.cli.commands.session.AdventOfCodeSessionManager;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true,
        description = "Generates all the needed files needed to solve a problem.")
public class Generate implements Runnable {
    private FileInstanceCreator fileInstanceCreator;

    @CommandLine.Parameters(index = "0", description = "The year of the problem to solve")
    private int year;

    @CommandLine.Parameters(index = "1", description = "The day of the problem to solve")
    private int day;

    @Override
    public void run() {
        this.fileInstanceCreator = new FileInstanceCreator(MyFileUtils.determineHomeDirectory(), year, day);
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
        MyFileUtils.touchFile(fileInstanceCreator.algorithmDay());
        MyFileUtils.touchFile(fileInstanceCreator.algorithmDayTest());
        MyFileUtils.touchFile(fileInstanceCreator.expected());
        MyFileUtils.touchFile(fileInstanceCreator.input());
        MyFileUtils.touchFile(fileInstanceCreator.part1example1());
        MyFileUtils.touchFile(fileInstanceCreator.part2example1());
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
        MyFileUtils.overwriteContentOfFile(fileInstanceCreator.algorithmDay(), templateGenerator.generateAlgorithmDay(day));
        MyFileUtils.overwriteContentOfFile(fileInstanceCreator.algorithmDayTest(), templateGenerator.generateAlgorithmDayTest(day));
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

        MyFileUtils.overwriteContentOfFile(fileInstanceCreator.input(), response.body());
    }
}
