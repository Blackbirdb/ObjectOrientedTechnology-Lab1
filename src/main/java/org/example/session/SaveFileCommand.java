package org.example.session;

import java.io.IOException;

public class SaveFileCommand implements SessionCommand{
    private final Session session;
    private final String fileName;

    public SaveFileCommand(Session session, String fileName) {
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

