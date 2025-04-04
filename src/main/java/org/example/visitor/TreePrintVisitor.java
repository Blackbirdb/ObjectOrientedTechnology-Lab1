package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.example.utils.SpellCheckUtils;

import java.util.List;
import java.util.Stack;

public class TreePrintVisitor implements HtmlVisitor {
    private final StringBuilder output = new StringBuilder();
    private int currentIndent = 0;
    private final Stack<Boolean> isLastStack = new Stack<>();
    private boolean showId;

    public TreePrintVisitor(){
        this.showId = true;
    }

    public TreePrintVisitor(boolean showId) {
        this.showId = showId;
    }

    public String getTreeOutput() {
        return output.toString();
    }

    @Override
    public void visit(HtmlElement element) {
        boolean isLast = element.isLastChild();
        String indent = getIndentString();
        String connector = currentIndent == 0 ? "" : getConnectorString(isLast);

        output.append(indent).append(connector);

        if (SpellCheckUtils.hasErrors(element.getTextContent())){
            output.append("[X]");
        }

        output.append(element.getTagName());

        if (showId && element.getId() != null && !element.getId().isEmpty()) {
            output.append("#").append(element.getId());
        }
        output.append("\n");

        List<HtmlNode> children = element.getChildren();

        currentIndent++;
        isLastStack.push(isLast);
        for (HtmlNode child : children) {
            child.accept(this);
        }
        isLastStack.pop();
        currentIndent--;
    }

    @Override
    public void visit(HtmlTextNode textNode) {
        String text = textNode.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        String indent = getIndentString();
        boolean isLast = textNode.isLastChild();
        String connector = getConnectorString(isLast);

        output.append(indent).append(connector).append(text).append("\n");
    }

    private String getIndentString() {
        StringBuilder sb = new StringBuilder();
        int level = 1;

        while (level <= currentIndent - 1) {
            boolean isLast = isLastStack.get(level);
            String appendString = isLast ? "    " : "│   ";
            sb.append(appendString);
            level++;
        }
        return sb.toString();
    }

    private String getConnectorString(boolean isLast) {
        return isLast ? "└── " : "├── ";
    }
}