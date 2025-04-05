package org.example.session;

import org.example.editor.HtmlEditor;
import org.example.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadFileCommand implements SessionCommand{
    private final Session session;
    private final String fileName;

    public LoadFileCommand(Session session, String fileName) {
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void execute() throws IOException {
        if (session.existEditorByName(fileName)) {
            throw new IllegalArgumentException(fileName + " is already opened");
        }
        String filePath = session.getPathFromName(fileName);
        if (!PathUtils.fileExists(filePath)) {
            initNewHtmlFileAt(filePath);
        }
        HtmlEditor editor = new HtmlEditor(filePath);
        session.addEditor(fileName, editor);
        session.setActiveEditor(editor);
    }

    // TODO: generate template directly, independent from default.html
    private static void initNewHtmlFileAt(String filePath) {
        Path sourcePath = Paths.get("src/main/resources/default.html");
        Path targetPath = Paths.get(filePath);

        try {
            Files.copy(sourcePath, targetPath);
        } catch (Exception e) {
            System.err.println("Failed to copy html file: " + filePath);
        }
    }
}
