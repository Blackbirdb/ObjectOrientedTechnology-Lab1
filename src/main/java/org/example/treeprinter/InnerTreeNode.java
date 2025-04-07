package org.example.treeprinter;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class InnerTreeNode extends TreeNode {
    private final List<TreeNode> children = new ArrayList<>();

    public void addChild (TreeNode child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public void accept(TreePrintVisitor visitor, String text){
        visitor.visit(this, text);
    }

    @Override
    public abstract void accept(TreePrintVisitor visitor);

}