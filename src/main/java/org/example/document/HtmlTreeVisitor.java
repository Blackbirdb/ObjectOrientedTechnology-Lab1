package org.example.document;

import lombok.Getter;
import org.example.tools.utils.SpellChecker;
import org.example.tools.treeprinter.InnerTreeNode;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.TreePrintVisitor;

@Getter
public class HtmlTreeVisitor extends TreePrintVisitor {
    private final boolean showId;
    private final SpellChecker spellChecker;

    public HtmlTreeVisitor(boolean showId) {
        this.showId = showId;
        this.spellChecker = new SpellChecker();
    }

    public HtmlTreeVisitor(boolean showId, SpellChecker spellChecker) {
        this.showId = showId;
        this.spellChecker = spellChecker;
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
        if (spellChecker.hasErrors(element.getTextContent()))
            return "[X]";
        else return "";
    }

    private String getId(HtmlElement element) {
        if (showId && element.getId() != null && !element.getId().isEmpty())
            return "#" + element.getId();
        else return "";
    }

}