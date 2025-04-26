package org.example.session.commands;

import org.example.tools.SessionStateSaver.SessionState;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.example.tools.utils.PathUtils;

public class LoadSessionCommand implements SessionCommand{
    private final Session session;
    private final SessionStateService sessionStateService;

    public LoadSessionCommand(Session session, SessionStateService sessionStateService) {
        this.session = session;
        this.sessionStateService = sessionStateService;
    }

    @Override
    public void execute() {
        if (sessionStateService.sessionFileExists()) {
            SessionState state = sessionStateService.loadSession();
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
