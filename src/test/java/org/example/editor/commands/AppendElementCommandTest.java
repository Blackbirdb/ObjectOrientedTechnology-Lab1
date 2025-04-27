package org.example.editor.commands;

import org.example.commands.AppendElementCommand;
import org.example.document.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AppendElementCommandTest {

    private HtmlDocument mockDocument;
    private AppendElementCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
    }

    @Test
    void execute_successfullyAppendsElement() {
        // 创建命令
        command = new AppendElementCommand(mockDocument, "div", "newId", "parentId", "Sample Text");

        // 执行命令
        command.execute();

        // 验证 appendElement 方法被正确调用
        verify(mockDocument).appendElement("div", "newId", "parentId", "Sample Text");
    }

    @Test
    void undo_successfullyRemovesElement() {
        // 创建命令
        command = new AppendElementCommand(mockDocument, "div", "newId", "parentId", "Sample Text");

        // 执行命令
        command.execute();

        // 执行撤销
        command.undo();

        // 验证 removeElement 方法被正确调用
        verify(mockDocument).removeElement("div");
    }


}