package org.example.tools.treeprinter;


public abstract class LeafTreeNode extends TreeNode {

    @Override
    public abstract void accept(Visitor visitor);

}
