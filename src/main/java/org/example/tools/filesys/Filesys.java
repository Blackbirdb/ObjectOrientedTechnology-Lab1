package org.example.tools.filesys;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Filesys {
    private final Path rootPath;
    private final Set<Path> openFiles;

    public Filesys(Path rootPath, HashSet<Path> openFiles) {
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
                    root.addChild(new FileNode(entry, isOpen));
                }
            }
        }
        return root;
    }

    public void print() {
        try {
            DirectoryNode root = buildFileTree(rootPath);
            DirectoryPrinterVisitor visitor = new DirectoryPrinterVisitor();
            root.accept(visitor);
            System.out.println(visitor.getOutput());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

