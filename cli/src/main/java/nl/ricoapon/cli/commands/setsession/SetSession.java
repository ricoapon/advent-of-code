package nl.ricoapon.cli.commands.setsession;

import picocli.CommandLine;

@CommandLine.Command(name = "set-session", mixinStandardHelpOptions = true,
        description = "Stores the session into a file on disk so it can be used to download files from http://adventofcode.com.")
public class SetSession implements Runnable {

    @CommandLine.Parameters(index = "0", description = "The session that is stored in the cookie")
    private String session;

    @Override
    public void run() {
        AdventOfCodeSessionManager.INSTANCE.setSession(session);
        System.out.println("Session changed successfully.");
    }
}
