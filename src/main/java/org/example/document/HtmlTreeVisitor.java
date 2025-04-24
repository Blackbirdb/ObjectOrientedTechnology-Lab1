package org.example.document;

import lombok.Getter;
import org.example.tools.spellchecker.JLanguageChecker;
import org.example.tools.treeprinter.InnerTreeNode;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.TreePrintVisitor;

@Getter
public class HtmlTreeVisitor extends TreePrintVisitor {
    private final boolean showId;
    private final JLanguageChecker JLanguageChecker;

    public HtmlTreeVisitor(boolean showId) {
        this.showId = showId;
        this.JLanguageChecker = new JLanguageChecker();
    }

    public HtmlTreeVisitor(boolean showId, JLanguageChecker JLanguageChecker) {
        this.showId = showId;
        this.JLanguageChecker = JLanguageChecker;
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

    private String getTagLabel(HtmlElement element) {
        if (JLanguageChecker.hasErrors(element.getTextContent()))
            return "[X]";
        else return "";
    }

    private String getId(HtmlElement element) {
        if (showId && element.getId() != null && !element.getId().isEmpty())
            return "#" + element.getId();
        else return "";
    }

}