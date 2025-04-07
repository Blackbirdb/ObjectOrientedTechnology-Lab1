package org.example.tools.filesys;

import lombok.Getter;
import org.example.treeprinter.LeafTreeNode;
import org.example.treeprinter.TreePrintVisitor;
import org.example.treeprinter.Visitor;

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
        String text = path.getFileName().toString();
        if (isOpen) {
            text += "*";
        }
        visitor.visit(this);
    }
}
