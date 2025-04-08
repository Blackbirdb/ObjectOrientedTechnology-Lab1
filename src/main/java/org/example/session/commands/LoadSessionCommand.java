package org.example.session.commands;

import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateSaver;
import org.example.tools.utils.PathUtils;

public class LoadSessionCommand implements SessionCommand{
    private final Session session;
    private final SessionStateSaver sessionStateSaver;

    public LoadSessionCommand(Session session) {
        this.session = session;
        this.sessionStateSaver = new SessionStateSaver();
    }

    public LoadSessionCommand(Session session, SessionStateSaver sessionStateSaver) {
        this.session = session;
        this.sessionStateSaver = sessionStateSaver;
    }

    @Override
    public void execute() {
        if (sessionStateSaver.sessionFileExists()) {
            SessionState state = sessionStateSaver.loadSession();
            if (state != null) {
                session.setCwd(state.cwd);
                for (String fileName : state.openFiles) {
                    String filePath = PathUtils.getPathFromName(fileName, state.cwd);

                    session.loadEditor(filePath, fileName, state.showIdMap.getOrDefault(fileName, true));

                }
                session.setActiveEditorByName(state.activeEditorName);
            }
        }
    }
}
