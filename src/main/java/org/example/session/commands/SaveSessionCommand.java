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
        for (Map.Entry <String, HtmlEditor> entry : session.getOpenEditors().entrySet()) {
            state.openFiles.add(entry.getKey());
            state.showIdMap.put(entry.getKey(), entry.getValue().getShowId());
        }
        state.activeEditorName = session.getActiveEditorName();
        state.cwd = session.getCwd();
        SessionStateSaver.saveSession(state);
    }
}
