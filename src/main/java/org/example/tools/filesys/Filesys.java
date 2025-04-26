package org.example.tools.filesys;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Component
public class Filesys {
    @Setter private Path rootPath;
    @Setter private Set<Path> openFiles;
    private final DirectoryPrinterVisitor visitor;

//    public Filesys(Path rootPath, HashSet<Path> openFiles) {
//        this.rootPath = rootPath;
//        this.openFiles = openFiles;
//    }

    @Autowired
    public Filesys(DirectoryPrinterVisitor visitor) {
        this.visitor = visitor;
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
            root.accept(visitor);
            System.out.println(visitor.getOutput());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

