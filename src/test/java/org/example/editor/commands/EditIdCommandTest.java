package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EditIdCommandTest {

    private HtmlDocument mockDocument;
    private EditIdCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
    }

    @Test
    void execute_successfullyChangesId() {
        // 设置模拟行为
        when(mockDocument.getElementById("oldId")).thenReturn(mock(HtmlElement.class));
        when(mockDocument.getElementById("newId")).thenReturn(null);

        command = new EditIdCommand(mockDocument, "oldId", "newId");

        // 执行命令
        command.execute();

        // 验证 editId 方法被正确调用
        verify(mockDocument).editId("oldId", "newId");
    }


    @Test
    void undo_successfullyRevertsIdChange() {
        // 设置初始执行状态
        when(mockDocument.getElementById("oldId")).thenReturn(mock(HtmlElement.class));
        when(mockDocument.getElementById("newId")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "oldId", "newId");
        command.execute();

        // 设置 undo 时的模拟行为
        when(mockDocument.getElementById("newId")).thenReturn(mock(HtmlElement.class));
        when(mockDocument.getElementById("oldId")).thenReturn(null);

        // 执行撤销
        command.undo();

        // 验证 editId 方法被正确调用
        verify(mockDocument).editId("newId", "oldId");
    }

}