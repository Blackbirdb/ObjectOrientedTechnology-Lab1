package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.languagetool.rules.RuleMatch;

import java.util.*;

public class SpellCheckVisitor implements HtmlVisitor {
    private final SpellCheckUtils spellCheckUtils = new SpellCheckUtils();
    private final Map<String, List<RuleMatch>> errorMap = new HashMap<>();

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
            String parentId = node.getParent().getId();
            errorMap.put(parentId, spellCheckUtils.checkText(text));
        }
    }

    public Map<String, List<RuleMatch>> getErrorMap() {
        return Collections.unmodifiableMap(errorMap);
    }

    public boolean hasErrors() {
        return !errorMap.isEmpty();
    }
}