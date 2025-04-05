package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HtmlNodeTest {

    // 创建测试用的具体子类
    private static class TestHtmlNode extends HtmlNode {
        @Override
        public void accept(HtmlVisitor visitor) {
            // 空实现，仅用于测试
        }
    }

    private HtmlElement parent;
    private TestHtmlNode node;
    private TestHtmlNode sibling1;
    private TestHtmlNode sibling2;

    @BeforeEach
    void setUp() {
        parent = new HtmlElement("div", "parent", null);
        node = new TestHtmlNode();
        sibling1 = new TestHtmlNode();
        sibling2 = new TestHtmlNode();

        // 设置父子关系
        node.setParent(parent);
        sibling1.setParent(parent);
        sibling2.setParent(parent);

        // 添加子节点
        parent.insertAtLast(sibling1);
        parent.insertAtLast(node);
    }

    @Test
    void isLastChild_returnsTrueWhenNodeIsLast() {
        assertTrue(node.isLastChild());
    }

    @Test
    void isLastChild_returnsFalseWhenNodeIsNotLast() {
        parent.insertAtLast(sibling2); // 添加另一个子节点
        assertFalse(node.isLastChild());
    }

    @Test
    void isLastChild_returnsTrueWhenNoParent() {
        TestHtmlNode orphanNode = new TestHtmlNode();
        orphanNode.setParent(null);
        assertTrue(orphanNode.isLastChild());
    }

    @Test
    void isLastChild_throwsExceptionWhenParentHasNoChildren() {
        HtmlElement emptyParent = new HtmlElement("div", "empty", null);
        TestHtmlNode testNode = new TestHtmlNode();
        testNode.setParent(emptyParent);

        assertThrows(IllegalStateException.class, () -> {
            testNode.isLastChild();
        });
    }

    @Test
    void parentGetterAndSetter_workCorrectly() {
        HtmlElement newParent = new HtmlElement("div", "newParent", null);
        node.setParent(newParent);
        assertEquals(newParent, node.getParent());
    }

    @Test
    void isLastChild_handlesSingleChildCase() {
        HtmlElement singleChildParent = new HtmlElement("div", "single", null);
        TestHtmlNode onlyChild = new TestHtmlNode();
        onlyChild.setParent(singleChildParent);
        singleChildParent.insertAtLast(onlyChild);

        assertTrue(onlyChild.isLastChild());
    }

    @Test
    void isLastChild_handlesMultipleChildren() {
        parent.insertAtLast(sibling2); // 现在有3个子节点
        assertFalse(sibling1.isLastChild());
        assertFalse(node.isLastChild());
        assertTrue(sibling2.isLastChild());
    }
}