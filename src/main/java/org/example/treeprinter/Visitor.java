package org.example.treeprinter;

import org.example.tools.filesys.DirectoryNode;
import org.example.tools.filesys.FileNode;

public interface Visitor {
    void visit(InnerTreeNode node, String text);

    void visit(LeafTreeNode node, String text);

}
