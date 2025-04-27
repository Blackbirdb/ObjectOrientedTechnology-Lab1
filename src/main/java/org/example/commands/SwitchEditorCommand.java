package org.example.commands;

import org.example.session.Session;

public class SwitchEditorCommand implements IrrevocableCommand {
    private final Session session;
    private final String fileName;

    public SwitchEditorCommand(Session session, String fileName) {
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        if (session.existEditorByName(fileName)) {
            session.setActiveEditorByName(fileName);
        } else {
            throw new IllegalStateException("File not loaded: " + fileName);
        }
    }
}
