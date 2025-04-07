package org.example.treeprinter;

import io.vavr.collection.Tree;
import org.example.tools.filesys.DirectoryNode;
import org.example.tools.filesys.FileNode;

import java.util.List;
import java.util.Stack;

import static org.example.tools.utils.PrintTreeUtils.getConnectorString;
import static org.example.tools.utils.PrintTreeUtils.getIndentString;

public class TreePrintVisitor implements Visitor {
    private final StringBuilder output = new StringBuilder();
    private int depth = 0;
    private final Stack<Boolean> isLastStack = new Stack<>();

    @Override
    public void visit(InnerTreeNode node, String text) {

        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(node.isLastChild(), depth))
                .append(text)
                .append("\n");

        List<TreeNode> children = node.getChildren();

        depth++;
        isLastStack.push(node.isLastChild());
        for (TreeNode child : children) {
            child.accept(this);
        }
        isLastStack.pop();
        depth--;
    }

    @Override
    public void visit(LeafTreeNode node, String text) {
        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(node.isLastChild(), depth))
                .append(text)
                .append("\n");
    }

    public String getOutput() {
        return output.toString();
    }

}