package nl.ricoapon.cli.commands.setsession;

import nl.ricoapon.cli.MyFileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class stores the session on disk and makes it available for other places in the application. This session can be
 * used to download files from <a href="http://adventofcode.com">http://adventofcode.com</a>.
 */
public enum AdventOfCodeSessionManager {
    INSTANCE;

    private final static File sessionFile = new File(MyFileUtils.determineHomeDirectory(), "cli/session/session.txt");

    public boolean isSessionSet() {
        return sessionFile.exists();
    }

    public void setSession(String session) {
        // Make sure to clean the file before creating a new one.
        if (sessionFile.exists() && !sessionFile.delete()) {
            throw new RuntimeException("Could not delete the old session file. Please do so manually.");
        }

        MyFileUtils.overwriteContentOfFile(sessionFile, "session=" + session);
    }

    public String getSession() {
        try {
            return Files.readString(sessionFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not read session file", e);
        }
    }
}
