package nl.ricoapon.cli;

import nl.ricoapon.cli.actions.generate.Generate;

public class Main {
    public static void main(String... args) {
        if (args[0].equals("generate")) {
            int year = Integer.parseInt(args[1]);
            int day = Integer.parseInt(args[2]);
            Generate generate = new Generate(FileUtil.determineHomeDirectory(), year, day);
            generate.generate();
        }
    }
}
