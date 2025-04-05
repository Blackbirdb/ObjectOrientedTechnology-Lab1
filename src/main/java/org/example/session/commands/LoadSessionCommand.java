package org.example.session.commands;

import org.example.session.SessionStateSaver.SessionState;
import org.example.session.SessionStateSaver.SessionStateSaver;
import org.example.editor.HtmlEditor;
import org.example.session.Session;
import org.example.tools.utils.PathUtils;

public class LoadSessionCommand implements SessionCommand{
    private final Session session;

    public LoadSessionCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        if (SessionStateSaver.sessionFileExists()) {
            SessionState state = SessionStateSaver.loadSession();
            if (state != null) {
                session.setCwd(state.cwd);
                for (String fileName : state.openFiles) {
                    String filePath = PathUtils.getPathFromName(fileName, state.cwd);
                    HtmlEditor editor = new HtmlEditor(filePath);
                    editor.setShowId(state.showIdMap.getOrDefault(fileName, true));
                    session.addEditor(fileName, editor);
                    if (fileName.equals(state.activeEditorName)){
                        session.setActiveEditor(editor);
                    }
                }
            }
        }
    }
}
