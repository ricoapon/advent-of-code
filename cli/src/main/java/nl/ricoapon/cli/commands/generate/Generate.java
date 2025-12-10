package nl.ricoapon.cli.commands.generate;

import nl.ricoapon.cli.commands.setsession.AdventOfCodeSessionManager;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;

import static nl.ricoapon.cli.MyFileUtils.determineHomeDirectory;
import static nl.ricoapon.cli.MyFileUtils.overwriteContentOfFile;
import static nl.ricoapon.cli.MyFileUtils.readFile;
import static nl.ricoapon.cli.MyFileUtils.touchFile;

@CommandLine.Command(name = "generate", aliases = "g", mixinStandardHelpOptions = true,
        description = "Generates all the needed files needed to solve a problem.")
public class Generate implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The day of the problem to solve", defaultValue = CommandLine.Parameters.NULL_VALUE)
    private Integer day;

    @CommandLine.Parameters(index = "1", description = "The year of the problem to solve", defaultValue = CommandLine.Parameters.NULL_VALUE)
    private Integer year;

    /**
     * The day and year should be filled automatically with the current day and year, since it is most likely that the
     * generator is used on the day that the puzzle came out. It is not possible to do this with Picocli annotations, so
     * we use this method to override null values with dynamically determined defaults.
     */
    private void setParameterDefaultsIfNeeded() {
        LocalDate now = LocalDate.now();

        if (year == null) {
            // We want to find the year of the most recent puzzle. If it is not December yet, this means it was last year
            // and not this year.
            int adventOfCodeYear = now.getYear();
            if (now.getMonth() != Month.DECEMBER) {
                adventOfCodeYear--;
            }
            year = adventOfCodeYear;
        }

        if (day == null) {
            if (now.getMonth() != Month.DECEMBER || now.getDayOfMonth() > 25) {
                throw new RuntimeException("No new Advent of Code puzzle came out today. Please specify the day");
            }

            day = now.getDayOfMonth();
        }
    }

    @Override
    public void run() {
        setParameterDefaultsIfNeeded();

        FileInstanceCreator fileInstanceCreator = new FileInstanceCreator(determineHomeDirectory(), year, day);
        TemplateGenerator templateGenerator = new TemplateGenerator();

        if (!fileInstanceCreator.yearPom().exists()) {
            createNewModule(fileInstanceCreator, templateGenerator);
        }
        overwriteContentOfFile(fileInstanceCreator.algorithmDay(), templateGenerator.generateAlgorithmDay(year, day));
        overwriteContentOfFile(fileInstanceCreator.algorithmDayTest(), templateGenerator.generateAlgorithmDayTest(year, day));
        overwriteContentOfFile(fileInstanceCreator.expected(), templateGenerator.generateExpectedYaml());
        touchFile(fileInstanceCreator.example());
        downloadAndFillInputFile(fileInstanceCreator.input());
    }

    /**
     * Creates the new {@code pom.xml} and updates the parent pom to include the new module.
     */
    private void createNewModule(FileInstanceCreator fileInstanceCreator, TemplateGenerator templateGenerator) {
        overwriteContentOfFile(fileInstanceCreator.yearPom(), templateGenerator.generatePom(year));

        String parentPom = readFile(fileInstanceCreator.parentPom());
        // Take the line-ending into account when replacing.
        parentPom = parentPom.replaceAll("</module>(\r?\n) {4}</modules>",
                "</module>$1        <module>year-%s</module>$1    </modules>".formatted(year));
        overwriteContentOfFile(fileInstanceCreator.parentPom(), parentPom);
    }

    private void downloadAndFillInputFile(File inputFile) {
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

        overwriteContentOfFile(inputFile, response.body());
    }
}
