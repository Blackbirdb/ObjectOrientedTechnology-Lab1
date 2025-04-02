package org.example.document;

import org.example.visitor.NodeVisitor;

public abstract class HtmlNode {
    public abstract void accept(NodeVisitor visitor);
}