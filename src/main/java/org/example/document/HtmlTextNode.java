package org.example.document;

import lombok.Getter;
import lombok.Setter;
import org.example.visitor.HtmlVisitor;

@Setter
@Getter
public class HtmlTextNode extends HtmlNode {
    private String text;

    public HtmlTextNode(String text, HtmlElement parent) {
        this.text = text;
        this.parent = parent;
    }

    @Override
    public void accept(HtmlVisitor visitor) {
        visitor.visit(this);
    }

}