package org.example.session;

import org.example.filesys.DirTreePrinter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class DirTreeCommand implements SessionCommand {
    private final Session session;

    public DirTreeCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        Path rootPath = Paths.get(session.getCwd());
        HashSet<Path> openFiles = new HashSet<>();

        for (String fileName : session.getOpenEditorPaths()) {
            Path filePath = Paths.get(fileName);
            openFiles.add(filePath);
        }

        DirTreePrinter dirTreePrinter = new DirTreePrinter(rootPath, openFiles);
        dirTreePrinter.execute();
    }
}
