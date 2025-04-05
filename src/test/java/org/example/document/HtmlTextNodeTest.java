package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlTextNodeTest {

    private HtmlElement parentElement;
    private HtmlTextNode textNode;
    private HtmlVisitor visitor;

    @BeforeEach
    void setUp() {
        parentElement = new HtmlElement("div", "parent", null);
        textNode = new HtmlTextNode("Sample text", parentElement);
        visitor = mock(HtmlVisitor.class);
    }

    @Test
    void constructor_setsTextAndParentCorrectly() {
        assertEquals("Sample text", textNode.getText());
        assertEquals(parentElement, textNode.getParent());
    }

    @Test
    void constructor_handlesNullText() {
        HtmlTextNode nullTextNode = new HtmlTextNode(null, parentElement);
        assertNull(nullTextNode.getText());
    }

    @Test
    void constructor_handlesNullParent() {
        HtmlTextNode noParentNode = new HtmlTextNode("Text", null);
        assertNull(noParentNode.getParent());
    }

    @Test
    void accept_invokesCorrectVisitorMethod() {
        textNode.accept(visitor);
        verify(visitor).visit(textNode);
    }

    @Test
    void setText_updatesTextContent() {
        textNode.setText("Updated text");
        assertEquals("Updated text", textNode.getText());
    }

    @Test
    void setText_handlesNullValue() {
        textNode.setText(null);
        assertNull(textNode.getText());
    }

    @Test
    void isLastChild_behaviorFromParentClass() {
        // 测试继承自HtmlNode的方法
        HtmlElement parent = new HtmlElement("div", "test-parent", null);
        HtmlTextNode node1 = new HtmlTextNode("First", parent);
        HtmlTextNode node2 = new HtmlTextNode("Second", parent);

        parent.insertAtLast(node1);
        parent.insertAtLast(node2);

        assertFalse(node1.isLastChild());
        assertTrue(node2.isLastChild());
    }

    @Test
    void parentGetterAndSetter_workCorrectly() {
        HtmlElement newParent = new HtmlElement("p", "new-parent", null);
        textNode.setParent(newParent);
        assertEquals(newParent, textNode.getParent());
    }

}