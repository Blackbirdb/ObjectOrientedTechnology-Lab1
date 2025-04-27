package org.example.editor.commands;

import org.example.commands.InsertElementCommand;
import org.example.document.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InsertElementCommandTest {

    private HtmlDocument mockDocument;
    private InsertElementCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
    }

    @Test
    void execute_successfullyInsertsElement() {
        // 模拟父元素和插入位置
        when(mockDocument.getParentId("locationId")).thenReturn("parentId");
        when(mockDocument.getElementIndex("locationId")).thenReturn(2);

        // 创建命令并执行
        command = new InsertElementCommand(mockDocument, "div", "newId", "locationId", "Sample Text");
        command.execute();

        // 验证 insertElement 方法被正确调用
        verify(mockDocument).insertElement("div", "newId", "parentId", "Sample Text", 2);
    }

    @Test
    void undo_successfullyRemovesInsertedElement() {
        // 创建命令并执行
        command = new InsertElementCommand(mockDocument, "div", "newId", "locationId", "Sample Text");
        command.execute();

        // 执行撤销
        command.undo();

        // 验证 removeElement 方法被正确调用
        verify(mockDocument).removeElement("newId");
    }

}