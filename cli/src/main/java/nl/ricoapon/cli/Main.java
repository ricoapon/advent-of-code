package nl.ricoapon.cli;

import java.io.File;

public class Main {
    public static void main(String... args) {
        System.out.println("Received the following arguments: " + String.join(" ; ", args));
        System.out.println("We are in the directory: " + determineHomeDirectory().getAbsolutePath());
    }

    private static File determineHomeDirectory() {
        return new File("../");
    }
}
