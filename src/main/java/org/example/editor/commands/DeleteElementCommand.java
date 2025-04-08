package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class DeleteElementCommand implements Command {
    private final HtmlDocument document;
    private final String elementId;
    private int index;
    private String parentId;
    private String textContent;
    private String tagName;

    public DeleteElementCommand(HtmlDocument document, String elementId) {
        this.document = document;
        this.elementId = elementId;
        index = -1;
    }

    @Override
    public void execute() {

        parentId = document.getParentId(elementId);
        index = document.getElementIndex(elementId);
        textContent = document.getElementText(elementId);
        tagName = document.getElementTag(elementId);

        document.removeElement(elementId);

    }

    @Override
    public void undo(){
        assert document.existElementById(parentId);
        document.insertElement(tagName, elementId, parentId, textContent, index);
    }
}
