package org.example.treeprinter;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class InnerTreeNode extends TreeNode {
    protected final List<TreeNode> children = new ArrayList<>();

    public void addChild (TreeNode child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public abstract void accept(Visitor visitor);

}