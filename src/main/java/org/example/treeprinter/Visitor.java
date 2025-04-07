package org.example.treeprinter;

import org.example.tools.filesys.DirectoryNode;
import org.example.tools.filesys.FileNode;

public interface Visitor {
    void visit (InnerTreeNode node);

    void visit(LeafTreeNode node);
}
