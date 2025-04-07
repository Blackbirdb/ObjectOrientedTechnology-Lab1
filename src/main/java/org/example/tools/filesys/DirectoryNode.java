package org.example.tools.filesys;

import lombok.Getter;
import org.example.treeprinter.InnerTreeNode;
import org.example.treeprinter.TreePrintVisitor;

import java.nio.file.Path;

@Getter
public class DirectoryNode extends InnerTreeNode {
    private final Path path;

    public DirectoryNode(Path path) {
        this.path = path;
    }

    @Override
    public void accept(TreePrintVisitor visitor) {
        String text = path.getFileName().toString() + "/";
        visitor.visit(this, text);
    }

}
