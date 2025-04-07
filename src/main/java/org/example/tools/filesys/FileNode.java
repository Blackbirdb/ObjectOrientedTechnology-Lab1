package org.example.tools.filesys;

import org.example.treeprinter.LeafTreeNode;
import org.example.treeprinter.TreePrintVisitor;

import java.nio.file.Path;

public class FileNode extends LeafTreeNode {
    private final boolean isOpen;
    private final Path path;

    public FileNode(Path path, boolean isOpen) {
        this.path = path;
        this.isOpen = isOpen;
    }
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void accept(TreePrintVisitor visitor) {
        String text = path.getFileName().toString();
        if (isOpen) {
            text += "*";
        }
        visitor.visit(this, text);
    }
}
