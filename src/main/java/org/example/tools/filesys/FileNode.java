package org.example.tools.filesys;

import lombok.Getter;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.Visitor;

import java.nio.file.Path;

public class FileNode extends LeafTreeNode {
    private final boolean isOpen;
    @Getter
    private final Path path;

    public FileNode(Path path, boolean isOpen) {
        this.path = path;
        this.isOpen = isOpen;
    }
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
