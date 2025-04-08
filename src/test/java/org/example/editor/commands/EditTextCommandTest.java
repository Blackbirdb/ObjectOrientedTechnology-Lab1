package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EditTextCommandTest {

    private HtmlDocument mockDocument;
    private EditTextCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
    }

    @Test
    void execute_successfullyChangesTextContent() {
        // 模拟旧文本内容
        when(mockDocument.editText("element1", "new text")).thenReturn("old text");

        // 创建命令并执行
        command = new EditTextCommand(mockDocument, "element1", "new text");
        command.execute();

        // 验证 editText 方法被正确调用
        verify(mockDocument).editText("element1", "new text");
    }

    @Test
    void undo_successfullyRestoresOldTextContent() {
        // 模拟旧文本内容
        when(mockDocument.editText("element1", "new text")).thenReturn("old text");

        // 创建命令并执行
        command = new EditTextCommand(mockDocument, "element1", "new text");
        command.execute();

        // 执行撤销
        command.undo();

        // 验证 editText 方法被正确调用以恢复旧文本
        verify(mockDocument).editText("element1", "old text");
    }

}