package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteElementCommandTest {

    private HtmlDocument mockDocument;
    private HtmlElement mockElement;
    private HtmlElement mockParent;
    private DeleteElementCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
        mockElement = mock(HtmlElement.class);
        mockParent = mock(HtmlElement.class);

        // 默认模拟行为
        when(mockDocument.getElementById("existingId")).thenReturn(mockElement);
        when(mockElement.getParent()).thenReturn(mockParent);
        when(mockElement.getTagName()).thenReturn("div");
        when(mockParent.getChildIndex(mockElement)).thenReturn(2);
    }

    @Test
    void execute_successfullyDeletesElement() {
        command = new DeleteElementCommand(mockDocument, "existingId");

        command.execute();

        // 验证元素被注销和移除
        verify(mockDocument).unregisterElement(mockElement);
        verify(mockParent).removeChild(mockElement);

        // 验证undo能正确恢复
        command.undo();
        verify(mockParent).insertAtIndex(2, mockElement);
    }

    @Test
    void execute_throwsWhenElementNotFound() {
        when(mockDocument.getElementById("nonexistent")).thenReturn(null);
        command = new DeleteElementCommand(mockDocument, "nonexistent");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void execute_throwsWhenElementIsSpecialTag() {
        when(mockElement.getTagName()).thenReturn("html");
        command = new DeleteElementCommand(mockDocument, "existingId");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void execute_throwsWhenParentIsNull() {
        when(mockElement.getParent()).thenReturn(null);
        command = new DeleteElementCommand(mockDocument, "existingId");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void undo_successfullyRestoresElement() {
        command = new DeleteElementCommand(mockDocument, "existingId");
        command.execute();

        command.undo();

        // 验证元素被重新插入到正确位置
        verify(mockParent).insertAtIndex(2, mockElement);
    }

    @Test
    void undo_throwsWhenNotExecuted() {
        command = new DeleteElementCommand(mockDocument, "existingId");

        assertThrows(IllegalStateException.class, command::undo);
    }

    @Test
    void undo_restoresElementAtCorrectPosition() {
        // 测试元素在不同位置的情况
        when(mockParent.getChildIndex(mockElement)).thenReturn(0); // 第一个子元素
        command = new DeleteElementCommand(mockDocument, "existingId");
        command.execute();
        command.undo();
        verify(mockParent).insertAtIndex(0, mockElement);

        reset(mockParent);
        when(mockParent.getChildIndex(mockElement)).thenReturn(4); // 中间子元素
        command.execute();
        command.undo();
        verify(mockParent).insertAtIndex(4, mockElement);
    }

    @Test
    void multipleExecutions_maintainCorrectState() {
        command = new DeleteElementCommand(mockDocument, "existingId");

        // 第一次执行和撤销
        command.execute();
        command.undo();
        verify(mockParent).insertAtIndex(2, mockElement);

        // 改变元素位置后再次测试
        when(mockParent.getChildIndex(mockElement)).thenReturn(3);
        command.execute();
        command.undo();
        verify(mockParent).insertAtIndex(3, mockElement);
    }
}