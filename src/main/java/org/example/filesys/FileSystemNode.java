package org.example.filesys;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

// 抽象节点类
abstract class FileSystemNode {
    protected Path path;
    public FileSystemNode(Path path) {
        this.path = path;
    }
    public Path getPath() {
        return path;
    }
    public abstract void accept(FileSystemVisitor visitor, int depth);
}
