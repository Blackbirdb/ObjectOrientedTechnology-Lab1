package org.example.tools.filesys;

import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
class DirectoryNode extends FileSystemNode {
    private final List<FileSystemNode> children = new ArrayList<>();
    public DirectoryNode(Path path) {
        super(path);
    }
    public void addChild(FileSystemNode child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}
