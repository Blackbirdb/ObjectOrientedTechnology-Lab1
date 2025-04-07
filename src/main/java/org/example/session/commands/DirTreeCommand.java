package org.example.session.commands;

import org.example.session.Session;
import org.example.tools.filesys.Filesys;

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

        Filesys filesys = new Filesys(rootPath, openFiles);
        filesys.print();
    }
}
