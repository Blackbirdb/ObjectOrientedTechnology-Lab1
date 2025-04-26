package org.example.document;

import static org.junit.jupiter.api.Assertions.*;

import org.example.tools.treeprinter.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HtmlElementTest {

    private HtmlElement parentElement;
    private HtmlElement testElement;

    @BeforeEach
    void setUp() {
        parentElement = new HtmlElement("div", "parent", null);
        testElement = new HtmlElement("p", "paragraph", parentElement);
    }

    @Test
    void constructor_shouldSetPropertiesCorrectly() {
        assertEquals("p", testElement.getTagName());
        assertEquals("paragraph", testElement.getId());
        assertSame(parentElement, testElement.getParent());
        assertTrue(testElement.getChildren().isEmpty());
    }

    @Test
    void toString_shouldFormatCorrectly() {
        testElement.setTextContent("Sample text");
        String result = testElement.toString();

        assertTrue(result.startsWith("<p id=\"paragraph\">"));
        assertTrue(result.contains("text: Sample text"));
        assertTrue(result.endsWith("</p>\n"));
    }

    @Test
    void getTextContent_withTextNode_shouldReturnText() {
        testElement.setTextContent("Hello World");
        assertEquals("Hello World", testElement.getTextContent());
    }

    @Test
    void getTextContent_withoutTextNode_shouldReturnNull() {
        assertNull(testElement.getTextContent());
    }

    @Test
    void setTextContent_shouldCreateTextNodeWhenEmpty() {
        testElement.setTextContent("New content");
        assertEquals(1, testElement.getChildren().size());
        assertEquals("New content", testElement.getTextContent());
    }

    @Test
    void setTextContent_shouldUpdateExistingTextNode() {
        testElement.setTextContent("Initial");
        testElement.setTextContent("Updated");

        assertEquals(1, testElement.getChildren().size());
        assertEquals("Updated", testElement.getTextContent());
    }

    @Test
    void setTextContent_withNull_shouldSetEmptyString() {
        testElement.setTextContent(null);
        assertNull(testElement.getTextContent());
    }

    @Test
    void insertAtLast_shouldAddChild() {
        HtmlElement child = new HtmlElement("span", "child", testElement);
        testElement.insertAtLast(child);

        assertEquals(1, testElement.getChildren().size());
        assertSame(child, testElement.getChildren().getFirst());
        assertSame(testElement, child.getParent());
    }

    @Test
    void insertAtIndex_shouldAddChildAtPosition() {
        HtmlElement child1 = new HtmlElement("span", "child1", null);
        HtmlElement child2 = new HtmlElement("div", "child2", null);

        testElement.insertAtIndex(0, child1);
        testElement.insertAtIndex(0, child2);

        assertEquals(2, testElement.getChildren().size());
        assertSame(child2, testElement.getChildren().getFirst());
        assertSame(child1, testElement.getChildren().getLast());
    }

    @Test
    void insertAtIndex_withInvalidIndex_shouldThrowException() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> testElement.insertAtIndex(-1, new HtmlElement("span", "child", null)));
        assertThrows(IndexOutOfBoundsException.class,
                () -> testElement.insertAtIndex(1, new HtmlElement("span", "child", null)));
    }

    @Test
    void removeChild_shouldRemoveNode() {
        HtmlElement child = new HtmlElement("span", "child", null);
        testElement.insertAtLast(child);
        testElement.removeChild(child);

        assertTrue(testElement.getChildren().isEmpty());
        assertNull(child.getParent());
    }

    @Test
    void getChildIndex_shouldReturnCorrectPosition() {
        HtmlElement child1 = new HtmlElement("span", "child1", testElement);
        HtmlElement child2 = new HtmlElement("div", "child2", testElement);

        testElement.insertAtLast(child1);
        testElement.insertAtLast(child2);

        assertEquals(0, testElement.getChildIndex(child1));
        assertEquals(1, testElement.getChildIndex(child2));
    }

    @Test
    void accept_shouldInvokeVisitor() {
        Visitor mockVisitor = mock(Visitor.class);
        testElement.accept(mockVisitor);

        verify(mockVisitor).visit(testElement);
    }

    @Test
    void mixedContentOperations_shouldWorkCorrectly() {
        // 添加文本节点
        testElement.setTextContent("Initial text");

        // 添加元素节点
        HtmlElement childElement = new HtmlElement("strong", "bold", null);
        testElement.insertAtLast(childElement);

        // 验证结构
        assertEquals(2, testElement.getChildren().size());
        assertTrue(testElement.getChildren().getFirst() instanceof HtmlTextNode);
        assertSame(childElement, testElement.getChildren().getLast());

        // 更新文本内容
        testElement.setTextContent("Updated text");
        assertEquals("Updated text", testElement.getTextContent());
        assertEquals(2, testElement.getChildren().size()); // 应保留元素节点
    }
}