package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

import javax.lang.model.element.Element;


public class DeleteElementCommand implements Command {
    private final HtmlDocument document;
    private final String elementId;
    private int index;
    private HtmlElement parent;
    private HtmlElement oldElement;

    public DeleteElementCommand(HtmlDocument document, String elementId) {
        this.document = document;
        this.elementId = elementId;
        index = -1;
        parent = null;
    }

    @Override
    public void execute() {
        oldElement = document.getElementById(elementId);
        if (oldElement == null) {
            throw new IllegalArgumentException("Element with ID " + elementId + " does not exist.");
        }
        document.unregisterElement(oldElement);
        parent = oldElement.getParent();
        index = parent.getChildIndex(oldElement);
        parent.removeChild(oldElement);
    }

    @Override
    public void undo(){
        if (parent == null) {
            throw new IllegalStateException("Parent element is null, cannot undo.");
        }
        parent.insertAtIndex(index, oldElement);
    }
}
