package nl.ricoapon.cli;

import nl.ricoapon.cli.actions.generate.Generate;
import nl.ricoapon.cli.actions.session.Session;

public class Main {
    public static void main(String... args) {
        if (args[0].equals("generate")) {
            int year = Integer.parseInt(args[1]);
            int day = Integer.parseInt(args[2]);
            Generate generate = new Generate(MyFileUtils.determineHomeDirectory(), year, day);
            generate.generate();
        } else if (args[0].equals("set-session")) {
            Session session = new Session();
            session.setSession(args[1]);
        }
    }
}
