package org.example.document;

import lombok.Getter;
import lombok.Setter;
import org.example.tools.spellchecker.JLanguageChecker;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.example.tools.treeprinter.InnerTreeNode;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.TreePrintVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HtmlTreeVisitor extends TreePrintVisitor {
    @Setter private boolean showId;
    private final SpellCheckerService spellChecker;

    @Autowired
    public HtmlTreeVisitor(SpellCheckerService spellChecker) {
        this.spellChecker = spellChecker;
        this.showId = true;
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