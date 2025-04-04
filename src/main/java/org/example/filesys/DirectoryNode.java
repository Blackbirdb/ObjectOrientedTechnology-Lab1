package org.example.filesys;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class DirectoryNode extends FileSystemNode {
    private List<FileSystemNode> children = new ArrayList<>();
    public DirectoryNode(Path path) {
        super(path);
    }
    public void addChild(FileSystemNode child) {
        children.add(child);
        child.setParent(this);
    }
    public List<FileSystemNode> getChildren() {
        return children;
    }
    @Override
    public void accept(FileSystemVisitor visitor, int depth) {
        visitor.visit(this, depth);
        for (FileSystemNode child : children) {
            child.accept(visitor, depth + 1);
        }
    }
}
