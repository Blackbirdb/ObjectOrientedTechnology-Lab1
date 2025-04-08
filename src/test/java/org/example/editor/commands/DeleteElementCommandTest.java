package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteElementCommandTest {

    private HtmlDocument mockDocument;
    private DeleteElementCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);

        // 模拟默认行为
        when(mockDocument.getParentId("existingId")).thenReturn("parentId");
        when(mockDocument.getElementIndex("existingId")).thenReturn(2);
        when(mockDocument.getElementText("existingId")).thenReturn("Sample Text");
        when(mockDocument.getElementTag("existingId")).thenReturn("div");
        when(mockDocument.existElementById("parentId")).thenReturn(true);
    }

    @Test
    void execute_successfullyDeletesElement() {
        command = new DeleteElementCommand(mockDocument, "existingId");

        command.execute();

        // 验证 removeElement 方法被正确调用
        verify(mockDocument).removeElement("existingId");
    }


    @Test
    void undo_successfullyRestoresElement() {
        command = new DeleteElementCommand(mockDocument, "existingId");

        command.execute();
        command.undo();

        // 验证 insertElement 方法被正确调用
        verify(mockDocument).insertElement("div", "existingId", "parentId", "Sample Text", 2);
    }

    @Test
    void undo_throwsWhenParentNotFound() {
        when(mockDocument.existElementById("parentId")).thenReturn(false);
        command = new DeleteElementCommand(mockDocument, "existingId");

        command.execute();

        assertThrows(AssertionError.class, command::undo);
    }
}