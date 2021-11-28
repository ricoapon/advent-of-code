package nl.ricoapon.cli.actions.session;

/**
 * Action to set the session for the application.
 */
public class Session {
    public void setSession(String session) {
        AdventOfCodeSessionManager.INSTANCE.setSession(session);
    }
}
