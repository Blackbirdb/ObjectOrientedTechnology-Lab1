package org.example.document;

import lombok.Getter;
import org.example.tools.utils.SpellCheckUtils;
import org.example.tools.treeprinter.InnerTreeNode;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.TreePrintVisitor;

public class HtmlTreeVisitor extends TreePrintVisitor {
    @Getter
    private final boolean showId;

    public HtmlTreeVisitor(boolean showId) {
        this.showId = showId;
    }

    @Override
    public void visit(InnerTreeNode element) {
        HtmlElement newElement = (HtmlElement) element;
        String text = getTagLabel(newElement) + newElement.getTagName() + getId(newElement);

        visitInnerNode(element, text);
    }

    @Override
    public void visit(LeafTreeNode textNode) {
        HtmlTextNode newTextNode = (HtmlTextNode) textNode;
        String text = newTextNode.getText();
        if (text == null || text.isEmpty()) {
            return;
        }
        visitLeafNode(textNode, text);
    }




//    private final StringBuilder output = new StringBuilder();
//    private int depth = 0;
//    private final Stack<Boolean> isLastStack = new Stack<>();
//    @Setter private boolean showId;
//
//    public HtmlTreeVisitor(){
//        this.showId = true;
//    }
//
//    public HtmlTreeVisitor(boolean showId) {
//        this.showId = showId;
//    }
//
//    public String getTreeOutput() {
//        return output.toString();
//    }
//
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
//
//    @Override
//    public void visit(HtmlElement element) {
//
//        output.append(getIndentString(isLastStack, depth))
//                .append(getConnectorString(element.isLastChild(), depth))
//                .append(getTagLabel(element))
//                .append(element.getTagName())
//                .append(getId(element))
//                .append("\n");
//
//        List<HtmlNode> children = element.getChildren();
//
//        depth++;
//        isLastStack.push(element.isLastChild());
//        for (HtmlNode child : children) {
//            child.accept(this);
//        }
//        isLastStack.pop();
//        depth--;
//    }
//
//    @Override
//    public void visit(HtmlTextNode textNode) {
//        String text = textNode.getText();
//        if (text == null || text.isEmpty()) {
//            return;
//        }
//
//        output.append(getIndentString(isLastStack, depth))
//                .append(getConnectorString(textNode.isLastChild(), depth))
//                .append(text)
//                .append("\n");
//    }

}