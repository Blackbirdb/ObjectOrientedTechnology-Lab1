package org.example.tools.filesys;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.*;
import java.util.*;

// 抽象节点类
@Getter
abstract class FileSystemNode {
    protected Path path;
    @Setter protected DirectoryNode parent;

    public FileSystemNode(Path path) {
        this.path = path;
    }

    public boolean isLastChild() {
        DirectoryNode parent = getParent();
        if (parent == null) {
            return true;
        }
        List<FileSystemNode> siblings = parent.getChildren();
        if (siblings == null || siblings.isEmpty()) {
            throw new IllegalStateException("Parent has no children");
        }
        return siblings.indexOf(this) == siblings.size() - 1;
    }

    public abstract void accept(FileSystemVisitor visitor);
}
