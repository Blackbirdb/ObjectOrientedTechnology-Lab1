package org.example.commands;

import org.example.session.Session;

public class SaveActiveEditor implements IrrevocableCommand {
    private final Session session;
    private final String fileName;

    public SaveActiveEditor(Session session, String fileName) {
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        if (session.existActivateEditor()){
            session.saveActiveEditorAs(fileName);
        }
        else {
            throw new IllegalStateException("No active editor found");
        }
    }
}

