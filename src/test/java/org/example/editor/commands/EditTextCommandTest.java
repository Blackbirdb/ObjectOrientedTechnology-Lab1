package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTextCommandTest {

    private HtmlDocument mockDocument;
    private HtmlElement mockElement;
    private EditTextCommand command;

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
        mockElement = mock(HtmlElement.class);
    }

    @Test
    void execute_successfullyChangesTextContent() {
        // 设置模拟行为
        when(mockDocument.getElementById("element1")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn("old text");

        command = new EditTextCommand(mockDocument, "element1", "new text");

        command.execute();

        // 验证执行流程
        verify(mockElement).setTextContent("new text");

        // 验证undo能正确恢复
        command.undo();
        verify(mockElement).setTextContent("old text");
    }

    @Test
    void execute_throwsWhenElementNotFound() {
        when(mockDocument.getElementById("nonexistent")).thenReturn(null);
        command = new EditTextCommand(mockDocument, "nonexistent", "new text");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void execute_handlesNullTextContent() {
        when(mockDocument.getElementById("element1")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn(null);

        command = new EditTextCommand(mockDocument, "element1", "new text");

        command.execute();

        verify(mockElement).setTextContent("new text");

        // 验证undo能正确处理null
        command.undo();
        verify(mockElement).setTextContent(null);
    }

    @Test
    void execute_handlesSettingTextToNull() {
        when(mockDocument.getElementById("element1")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn("old text");

        command = new EditTextCommand(mockDocument, "element1", null);

        command.execute();

        verify(mockElement).setTextContent(null);

        // 验证undo能恢复原始文本
        command.undo();
        verify(mockElement).setTextContent("old text");
    }

    @Test
    void undo_throwsWhenElementNotFound() {
        // 先正常执行
        when(mockDocument.getElementById("element1")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn("old text");
        command = new EditTextCommand(mockDocument, "element1", "new text");
        command.execute();

        // 设置undo时元素不存在
        when(mockDocument.getElementById("element1")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, command::undo);
    }

    @Test
    void undo_restoresOriginalTextContent() {
        // 测试不同文本内容
        String[] testCases = {"some text", "", null};

        for (String originalText : testCases) {
            // 准备模拟
            reset(mockElement);
            when(mockDocument.getElementById("element1")).thenReturn(mockElement);
            when(mockElement.getTextContent()).thenReturn(originalText);

            // 执行命令
            command = new EditTextCommand(mockDocument, "element1", "new text");
            command.execute();

            // 执行undo
            command.undo();

            // 验证文本恢复正确
            verify(mockElement).setTextContent(originalText);
        }
    }

    @Test
    void multipleEdits_maintainCorrectState() {
        // 第一次编辑
        when(mockDocument.getElementById("element1")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn("version 1");
        command = new EditTextCommand(mockDocument, "element1", "version 2");
        command.execute();

        // 第二次编辑
        when(mockElement.getTextContent()).thenReturn("version 2");
        EditTextCommand secondEdit = new EditTextCommand(mockDocument, "element1", "version 3");
        secondEdit.execute();

        // 撤销第二次编辑
        secondEdit.undo();
        // 撤销第一次编辑
        command.undo();

        verify(mockElement).setTextContent("version 3");
        verify(mockElement, times(2)).setTextContent("version 2");
        verify(mockElement).setTextContent("version 1");
    }

    @Test
    void constructor_initializesFields() {
        // 测试构造函数是否正确初始化
        command = new EditTextCommand(mockDocument, "testId", "test content");

        // 通过执行和撤销来验证内部状态
        when(mockDocument.getElementById("testId")).thenReturn(mockElement);
        when(mockElement.getTextContent()).thenReturn("original");

        command.execute();
        command.undo();

        verify(mockElement).setTextContent("original");
    }
}