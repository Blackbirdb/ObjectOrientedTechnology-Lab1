package org.example.session.commands;

import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.GsonStateService;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionStateService;

public class SaveSessionCommand implements SessionCommand {
    private final Session session;
    private final SessionStateService sessionStateService;

    public SaveSessionCommand(Session session, SessionStateService sessionStateService) {
        this.session = session;
        this.sessionStateService = sessionStateService;
    }

    @Override
    public void execute() {
        if (!session.isActive()) { return; }
        SessionState state = new SessionState();
        state.openFiles = session.getOpenEditorNames();
        state.showIdMap = session.getShowIdMap();
        state.activeEditorName = session.getActiveEditorName();
        state.cwd = session.getCwd();
        sessionStateService.saveSession(state);
    }
}
