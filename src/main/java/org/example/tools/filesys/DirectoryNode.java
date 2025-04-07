package org.example.tools.filesys;

import lombok.Getter;
import org.example.treeprinter.InnerTreeNode;
import org.example.treeprinter.TreePrintVisitor;
import org.example.treeprinter.Visitor;

import java.nio.file.Path;

@Getter
public class DirectoryNode extends InnerTreeNode {
    private final Path path;

    public DirectoryNode(Path path) {
        this.path = path;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
