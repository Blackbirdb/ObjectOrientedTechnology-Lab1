package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditIdCommandTest {

    private HtmlDocument mockDocument;
    private HtmlElement mockElement;
    private EditIdCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
        mockElement = mock(HtmlElement.class);
    }

    @Test
    void execute_successfullyChangesId() {
        // 设置模拟行为
        when(mockDocument.getElementById("oldId")).thenReturn(mockElement);
        when(mockDocument.getElementById("newId")).thenReturn(null);

        command = new EditIdCommand(mockDocument, "oldId", "newId");

        command.execute();

        // 验证执行流程
        verify(mockDocument).unregisterElement(mockElement);
        verify(mockElement).setId("newId");
        verify(mockDocument).registerElement(mockElement);
    }

    @Test
    void execute_throwsWhenElementNotFound() {
        when(mockDocument.getElementById("nonexistent")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "nonexistent", "newId");

        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }

    @Test
    void execute_throwsWhenNewIdExists() {
        HtmlElement existingElement = mock(HtmlElement.class);
        when(mockDocument.getElementById("oldId")).thenReturn(mockElement);
        when(mockDocument.getElementById("existingId")).thenReturn(existingElement);

        command = new EditIdCommand(mockDocument, "oldId", "existingId");

        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }

    @Test
    void undo_successfullyRevertsIdChange() {
        // 设置初始执行状态
        when(mockDocument.getElementById("oldId")).thenReturn(mockElement);
        when(mockDocument.getElementById("newId")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "oldId", "newId");
        command.execute();

        // 设置undo时的模拟行为
        when(mockDocument.getElementById("newId")).thenReturn(mockElement);
        when(mockDocument.getElementById("oldId")).thenReturn(null);

        command.undo();

        // 验证undo流程
        verify(mockDocument, times(2)).unregisterElement(mockElement);
        verify(mockElement).setId("oldId");
        verify(mockDocument, times(2)).registerElement(mockElement);
    }

    @Test
    void undo_throwsWhenElementNotFound() {
        command = new EditIdCommand(mockDocument, "oldId", "newId");

        // 不执行execute()，直接尝试undo
        when(mockDocument.getElementById("newId")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> command.undo());
    }

    @Test
    void undo_throwsWhenOldIdExists() {
        // 先正常执行
        when(mockDocument.getElementById("oldId")).thenReturn(mockElement);
        when(mockDocument.getElementById("newId")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "oldId", "newId");
        command.execute();

        // 设置undo时的冲突情况
        HtmlElement conflictingElement = mock(HtmlElement.class);
        when(mockDocument.getElementById("newId")).thenReturn(mockElement);
        when(mockDocument.getElementById("oldId")).thenReturn(conflictingElement);

        assertThrows(IllegalArgumentException.class, () -> command.undo());
    }

    @Test
    void multipleIdChanges_workCorrectly() {
        // 第一次修改
        when(mockDocument.getElementById("id1")).thenReturn(mockElement);
        when(mockDocument.getElementById("id2")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "id1", "id2");
        command.execute();

        // 验证第一次修改
        verify(mockElement).setId("id2");

        // 准备第二次修改
        reset(mockElement);
        when(mockDocument.getElementById("id2")).thenReturn(mockElement);
        when(mockDocument.getElementById("id3")).thenReturn(null);

        // 第二次修改
        command = new EditIdCommand(mockDocument, "id2", "id3");
        command.execute();

        // 验证第二次修改
        verify(mockElement).setId("id3");
    }

    @Test
    void undoMultipleTimes_worksCorrectly() {
        // 初始执行
        when(mockDocument.getElementById("original")).thenReturn(mockElement);
        when(mockDocument.getElementById("modified")).thenReturn(null);
        command = new EditIdCommand(mockDocument, "original", "modified");
        command.execute();

        // 第一次undo
        when(mockDocument.getElementById("modified")).thenReturn(mockElement);
        when(mockDocument.getElementById("original")).thenReturn(null);
        command.undo();
        verify(mockElement).setId("original");

        // 准备第二次执行
        reset(mockElement);
        when(mockDocument.getElementById("original")).thenReturn(mockElement);
        when(mockDocument.getElementById("modified")).thenReturn(null);

        // 第二次执行
        command.execute();
        verify(mockElement).setId("modified");
    }
}