package org.example.treeprinter;


public abstract class LeafTreeNode extends TreeNode {
    @Override
    public void accept(TreePrintVisitor visitor, String text){
        visitor.visit(this, text);
    }

    @Override
    public abstract void accept(TreePrintVisitor visitor);

}
