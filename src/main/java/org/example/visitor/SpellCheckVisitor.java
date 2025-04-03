package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;

import java.util.*;

public class SpellCheckVisitor implements HtmlVisitor {
    private final SpellCheckUtils spellCheckUtils = new SpellCheckUtils();
    private final Map<HtmlNode, List<String>> errorMap = new HashMap<>();

    @Override
    public void visit(HtmlElement element) {
        for (HtmlNode child : element.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(HtmlTextNode textNode) {
        checkText(textNode.getText(), textNode);
    }

    private void checkText(String text, HtmlNode node) {
        if (spellCheckUtils.hasErrors(text)) {
            errorMap.put(node, spellCheckUtils.getSuggestions(text));
        }
    }

    public Map<HtmlNode, List<String>> getErrorMap() {
        return Collections.unmodifiableMap(errorMap);
    }

    public boolean hasErrors() {
        return !errorMap.isEmpty();
    }
}