package nl.ricoapon.cli;

import nl.ricoapon.cli.actions.generate.Generate;
import nl.ricoapon.cli.actions.session.SetSessionCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "advent", mixinStandardHelpOptions = true,
        description = "Makes it easy to start programming by generating files for you!",
        subcommands = {Generate.class, SetSessionCommand.class})
public class Main implements Runnable {

    public static void main(String... args) {
        // Due to issues with Gradle and color formatting, disable ANSI.
        System.setProperty("picocli.ansi", "false");

        System.exit(new CommandLine(new Main()).execute(args));
    }

    @Override
    public void run() {
        // Any action is done by subcommands. We don't need to do anything here.
    }
}
