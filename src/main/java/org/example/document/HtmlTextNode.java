package org.example.document;

import lombok.Getter;
import lombok.Setter;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.Visitor;

@Setter
@Getter
public class HtmlTextNode extends LeafTreeNode {
    private String text;

    public HtmlTextNode(String text, HtmlElement parent) {
        this.text = text;
        this.parent = parent;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}