package org.example.commands;

import org.example.tools.SessionStateSaver.SessionState;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionStateService;

public class SaveSessionCommand implements IrrevocableCommand {
    private final Session session;
    private final SessionStateService sessionStateService;
    private final SessionState state;

    public SaveSessionCommand(Session session, SessionStateService sessionStateService, SessionState state) {
        this.session = session;
        this.sessionStateService = sessionStateService;
        this.state = state;
    }

    @Override
    public void execute() {
        if (!session.isActive()) { return; }
        state.openFiles = session.getOpenEditorNames();
        state.showIdMap = session.getShowIdMap();
        state.activeEditorName = session.getActiveEditorName();
        state.cwd = session.getCwd();
        sessionStateService.saveSession(state);
    }
}
