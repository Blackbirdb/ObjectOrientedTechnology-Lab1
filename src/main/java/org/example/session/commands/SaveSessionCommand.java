package org.example.session.commands;

import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateSaver;
import org.example.session.Session;

public class SaveSessionCommand implements SessionCommand {
    private final Session session;
    private final SessionStateSaver sessionStateSaver;

    public SaveSessionCommand(Session session) {
        this.session = session;
        sessionStateSaver = new SessionStateSaver();
    }

    public SaveSessionCommand(Session session, SessionStateSaver sessionStateSaver) {
        this.session = session;
        this.sessionStateSaver = sessionStateSaver;
    }

    @Override
    public void execute() {
        if (!session.isActive()) { return; }
        SessionState state = new SessionState();
        state.openFiles = session.getOpenEditorNames();
        state.showIdMap = session.getShowIdMap();
        state.activeEditorName = session.getActiveEditorName();
        state.cwd = session.getCwd();
        sessionStateSaver.saveSession(state);
    }
}
