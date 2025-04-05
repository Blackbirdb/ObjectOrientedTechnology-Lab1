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
        try {
            session.getActiveEditor().saveToFile(session.getPathFromName(fileName));
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
