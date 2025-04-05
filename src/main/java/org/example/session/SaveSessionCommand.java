package org.example.session;

import org.example.SessionStateSaver.SessionState;
import org.example.SessionStateSaver.SessionStateSaver;
import org.example.editor.HtmlEditor;

import java.util.Map;

public class SaveSessionCommand implements SessionCommand{
    private final Session session;

    public SaveSessionCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
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
