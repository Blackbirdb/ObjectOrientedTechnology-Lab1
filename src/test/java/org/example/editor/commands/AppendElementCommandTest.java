package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppendElementCommandTest {

    private HtmlDocument mockDocument;
    private HtmlElement mockParent;
    private HtmlElement mockNewElement;
    private HtmlElementFactory mockFactory;
    private AppendElementCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
        mockParent = mock(HtmlElement.class);
        mockNewElement = mock(HtmlElement.class);
        mockFactory = mock(HtmlElementFactory.class);

        when(mockDocument.getFactory()).thenReturn(mockFactory);
        when(mockParent.getTagName()).thenReturn("div");
    }

    @Test
    void execute_successfullyAppendsElement() {
        // 设置模拟行为
        when(mockDocument.getElementById("parent1")).thenReturn(mockParent);
        when(mockFactory.createElement("p", "child1", "Hello", mockParent))
                .thenReturn(mockNewElement);

        // 创建命令
        command = new AppendElementCommand(mockDocument, "p", "child1", "parent1", "Hello");

        // 执行命令
        command.execute();

        // 验证交互
        verify(mockParent).insertAtLast(mockNewElement);
        verify(mockDocument, never()).unregisterElement(any());
    }

    @Test
    void execute_throwsWhenParentIsNull() {
        when(mockDocument.getElementById("nonexistent")).thenReturn(null);

        command = new AppendElementCommand(mockDocument, "p", "child1", "nonexistent", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute();
        });
    }

    @Test
    void execute_throwsWhenParentIsHtml() {
        when(mockDocument.getElementById("htmlParent")).thenReturn(mockParent);
        when(mockParent.getTagName()).thenReturn("html");

        command = new AppendElementCommand(mockDocument, "p", "child1", "htmlParent", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute();
        });
    }

    @Test
    void execute_throwsWhenTagIsSpecial() {
        when(mockDocument.getElementById("parent1")).thenReturn(mockParent);

        command = new AppendElementCommand(mockDocument, "html", "child1", "parent1", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute();
        });
    }

    @Test
    void undo_successfullyRemovesElement() {
        // 设置模拟行为
        when(mockDocument.getElementById("child1")).thenReturn(mockNewElement);
        when(mockDocument.getElementById("parent1")).thenReturn(mockParent);
        when(mockNewElement.getTagName()).thenReturn("p");
        when(mockParent.getTagName()).thenReturn("div");

        // 创建命令并先执行
        command = new AppendElementCommand(mockDocument, "p", "child1", "parent1", "text");
        command.execute();

        // 执行撤销
        command.undo();

        // 验证交互
        verify(mockParent).removeChild(mockNewElement);
        verify(mockDocument).unregisterElement(mockNewElement);
    }

    @Test
    void undo_throwsWhenElementToRemoveIsNull() {
        when(mockDocument.getElementById("nonexistent")).thenReturn(null);

        command = new AppendElementCommand(mockDocument, "p", "nonexistent", "parent1", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.undo();
        });
    }

    @Test
    void undo_throwsWhenElementIsSpecialTag() {
        when(mockDocument.getElementById("specialChild")).thenReturn(mockNewElement);
        when(mockNewElement.getTagName()).thenReturn("html");

        command = new AppendElementCommand(mockDocument, "html", "specialChild", "parent1", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.undo();
        });
    }

    @Test
    void undo_throwsWhenParentIsNull() {
        when(mockDocument.getElementById("child1")).thenReturn(mockNewElement);
        when(mockDocument.getElementById("nonexistentParent")).thenReturn(null);
        when(mockNewElement.getTagName()).thenReturn("p");

        command = new AppendElementCommand(mockDocument, "p", "child1", "nonexistentParent", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.undo();
        });
    }

    @Test
    void undo_throwsWhenParentIsHtml() {
        when(mockDocument.getElementById("child1")).thenReturn(mockNewElement);
        when(mockDocument.getElementById("htmlParent")).thenReturn(mockParent);
        when(mockNewElement.getTagName()).thenReturn("p");
        when(mockParent.getTagName()).thenReturn("html");

        command = new AppendElementCommand(mockDocument, "p", "child1", "htmlParent", "text");

        assertThrows(IllegalArgumentException.class, () -> {
            command.undo();
        });
    }

}