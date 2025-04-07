package org.example.treeprinter;

import lombok.Getter;
import lombok.Setter;
import org.example.tools.filesys.DirectoryNode;

import java.util.List;

@Setter
@Getter
public abstract class TreeNode {
    protected InnerTreeNode parent;

    public abstract void accept(TreePrintVisitor visitor, String text);

    public abstract void accept(TreePrintVisitor visitor);

    public boolean isLastChild() {
        InnerTreeNode parent = getParent();
        if (parent == null) {
            return true;
        }
        List<TreeNode> siblings = parent.getChildren();
        if (siblings == null || siblings.isEmpty()) {
            throw new IllegalStateException("Parent has no children");
        }
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}