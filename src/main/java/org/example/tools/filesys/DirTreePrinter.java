package org.example.tools.filesys;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DirTreePrinter {
    private final Path rootPath;
    private final Set<Path> openFiles;

    public DirTreePrinter(Path rootPath, HashSet<Path> openFiles) {
        this.rootPath = rootPath;
        this.openFiles = openFiles;
    }

    private DirectoryNode buildFileTree(Path path) throws IOException {
        DirectoryNode root = new DirectoryNode(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    root.addChild(buildFileTree(entry));
                } else {
                    boolean isOpen = openFiles.contains(rootPath.relativize(entry));
                    System.out.println(rootPath.relativize(entry) + " " + isOpen);
                    root.addChild(new FileNode(entry, isOpen));
                }
            }
        }
        return root;
    }
    public void print() {
        try {
            DirectoryNode root = buildFileTree(rootPath);
            System.out.println(openFiles);
            DirectoryPrinterVisitor visitor = new DirectoryPrinterVisitor();
            root.accept(visitor);
            System.out.println(visitor.getOutput());
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }
}
