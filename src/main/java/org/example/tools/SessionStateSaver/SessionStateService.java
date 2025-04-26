package org.example.tools.SessionStateSaver;

public interface SessionStateService {
    void saveSession(SessionState state);
    SessionState loadSession();
    boolean sessionFileExists();
}
