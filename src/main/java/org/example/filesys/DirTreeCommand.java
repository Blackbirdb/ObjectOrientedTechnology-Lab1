package org.example.filesys;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DirTreeCommand {
    private Path rootPath;
    private Set<Path> openFiles;
    public DirTreeCommand(Path rootPath, Set<Path> openFiles) {
        this.rootPath = rootPath;
        this.openFiles = openFiles;
    }
    public DirTreeCommand(Path rootPath) {
        this.rootPath = rootPath;
        this.openFiles = new HashSet<>();
    }

    private DirectoryNode buildFileTree(Path path) throws IOException {
        DirectoryNode root = new DirectoryNode(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    root.addChild(buildFileTree(entry));
                } else {
                    boolean isOpen = openFiles.contains(entry);
                    root.addChild(new FileNode(entry, isOpen));
                }
            }
        }
        return root;
    }
    public void execute() {
        try {
            DirectoryNode root = buildFileTree(rootPath);
            DirectoryPrinterVisitor visitor = new DirectoryPrinterVisitor();
            root.accept(visitor, 0);
            System.out.println(visitor.getOutput());
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }
}
