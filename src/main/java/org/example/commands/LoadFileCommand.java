package org.example.commands;

import org.example.session.Session;
import org.example.tools.utils.PathUtils;

public class LoadFileCommand implements IrrevocableCommand {
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

        if (!PathUtils.fileExists(filePath)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        session.loadEditor(filePath, fileName, true);

        session.setActiveEditorByName(fileName);
    }

}
