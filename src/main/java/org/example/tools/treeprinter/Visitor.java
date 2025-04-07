package org.example.tools.treeprinter;

public interface Visitor {
    void visit (InnerTreeNode node);

    void visit(LeafTreeNode node);
}
