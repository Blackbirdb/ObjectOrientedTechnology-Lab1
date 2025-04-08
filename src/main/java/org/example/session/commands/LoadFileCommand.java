package org.example.session.commands;

import org.example.session.Session;

public class LoadFileCommand implements SessionCommand{
    private final Session session;
    private final String fileName;

    public LoadFileCommand(Session session, String fileName) {
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        if (session.existEditorByName(fileName)) {
            throw new IllegalArgumentException(fileName + " is already opened");
        }
        String filePath = session.getPathFromName(fileName);

        session.loadEditor(filePath, fileName, true);

        session.setActiveEditorByName(fileName);
    }

}
