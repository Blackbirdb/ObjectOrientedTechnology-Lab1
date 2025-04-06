package org.example.session.commands;

import org.example.session.SessionStateSaver.SessionState;
import org.example.session.SessionStateSaver.SessionStateSaver;
import org.example.editor.HtmlEditor;
import org.example.session.Session;

import java.util.Map;

public class SaveSessionCommand implements SessionCommand {
    private final Session session;

    public SaveSessionCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        if (!session.isActive()) { return; }
        SessionState state = new SessionState();
        state.openFiles = session.getOpenEditorNames();
        state.showIdMap = session.getShowIdMap();
        state.activeEditorName = session.getActiveEditorName();
        state.cwd = session.getCwd();
        SessionStateSaver.saveSession(state);
    }
}
