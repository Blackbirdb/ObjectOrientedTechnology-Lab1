package org.example.commands;

import org.example.session.Session;
import org.example.tools.filesys.Filesys;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class DirTreeCommand implements IrrevocableCommand {
    private final Session session;
    private final Filesys filesys;

    public DirTreeCommand(Session session, Filesys filesys) {
        this.session = session;
        this.filesys = filesys;
    }

    @Override
    public void execute() {

        Path rootPath = Paths.get(session.getCwd());
        HashSet<Path> openFiles = new HashSet<>();

        for (String fileName : session.getOpenEditorPaths()) {
            Path filePath = Paths.get(fileName);
            openFiles.add(filePath);
        }

        filesys.setOpenFiles(openFiles);
        filesys.setRootPath(rootPath);

        filesys.print();
    }
}
