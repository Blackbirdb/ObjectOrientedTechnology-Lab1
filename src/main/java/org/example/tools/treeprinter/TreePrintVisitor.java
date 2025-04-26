package org.example.tools.treeprinter;

import java.util.List;
import java.util.Stack;

import static org.example.tools.utils.PrintTreeUtils.getConnectorString;
import static org.example.tools.utils.PrintTreeUtils.getIndentString;

public abstract class TreePrintVisitor implements Visitor {
    private final StringBuilder output = new StringBuilder();
    private int depth = 0;
    private final Stack<Boolean> isLastStack = new Stack<>();

    protected void visitInnerNode(InnerTreeNode node, String text) {

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

    protected void visitLeafNode(LeafTreeNode node, String text) {
        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(node.isLastChild(), depth))
                .append(text)
                .append("\n");
    }

    public String getOutput() {
        return output.toString();
    }

    public void clearOutput() {
        output.setLength(0);
    }

    @Override
    public abstract void visit(LeafTreeNode node);

    @Override
    public abstract void visit(InnerTreeNode node);

}