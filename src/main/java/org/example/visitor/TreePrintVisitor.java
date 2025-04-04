package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.example.utils.SpellCheckUtils;

import java.util.List;
import java.util.Stack;

import static org.example.utils.PrintTreeUtil.*;

public class TreePrintVisitor implements HtmlVisitor {
    private final StringBuilder output = new StringBuilder();
    private int depth = 0;
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

    private String getTagLabel(HtmlElement element) {
        if (SpellCheckUtils.hasErrors(element.getTextContent()))
            return "[X]";
        else return "";
    }

    private String getId(HtmlElement element) {
        if (showId && element.getId() != null && !element.getId().isEmpty())
            return "#" + element.getId();
        else return "";
    }

    @Override
    public void visit(HtmlElement element) {

        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(element.isLastChild(), depth))
                .append(getTagLabel(element))
                .append(element.getTagName())
                .append(getId(element))
                .append("\n");

        List<HtmlNode> children = element.getChildren();

        depth++;
        isLastStack.push(element.isLastChild());
        for (HtmlNode child : children) {
            child.accept(this);
        }
        isLastStack.pop();
        depth--;
    }

    @Override
    public void visit(HtmlTextNode textNode) {
        String text = textNode.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(textNode.isLastChild(), depth))
                .append(text)
                .append("\n");
    }

}