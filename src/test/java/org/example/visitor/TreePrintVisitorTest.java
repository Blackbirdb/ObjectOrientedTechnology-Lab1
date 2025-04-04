package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlTextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TreePrintVisitorTest {
    private TreePrintVisitor visitor;
    private HtmlElement element;
    private HtmlTextNode textNode;

    @BeforeEach
    void setUp() {
        visitor = new TreePrintVisitor();
        element = mock(HtmlElement.class);
        textNode = mock(HtmlTextNode.class);
    }

    @Test
    void visitElement_shouldPrintElementWithId() {
        when(element.getTagName()).thenReturn("div");
        when(element.getId()).thenReturn("elementId");
        when(element.getChildren()).thenReturn(List.of());
        when(element.isLastChild()).thenReturn(true);

        visitor.visit(element);

        String expectedOutput = "div#elementId\n";
        assertEquals(expectedOutput, visitor.getTreeOutput());
    }

    @Test
    void visitElement_shouldPrintElementWithoutId() {
        when(element.getTagName()).thenReturn("div");
        when(element.getId()).thenReturn(null);
        when(element.getChildren()).thenReturn(List.of());
        when(element.isLastChild()).thenReturn(true);

        visitor.visit(element);

        String expectedOutput = "div\n";
        assertEquals(expectedOutput, visitor.getTreeOutput());
    }

    @Test
    void visitTextNode_shouldPrintTextNode() {
        when(textNode.getText()).thenReturn("text");
        when(textNode.isLastChild()).thenReturn(true);

        visitor.visit(textNode);

        String expectedOutput = "└── text\n";
        assertEquals(expectedOutput, visitor.getTreeOutput());
    }

    @Test
    void visitElement_shouldHandleNestedElements() {
        HtmlElement childElement = mock(HtmlElement.class);
        when(element.getTagName()).thenReturn("div");
        when(element.getId()).thenReturn("parent");
        when(element.getChildren()).thenReturn(List.of(childElement));
        when(element.isLastChild()).thenReturn(true);
        when(childElement.getTagName()).thenReturn("span");
        when(childElement.getId()).thenReturn("child");
        when(childElement.getChildren()).thenReturn(List.of());
        when(childElement.isLastChild()).thenReturn(true);
        doAnswer(invocation -> {
            HtmlVisitor visitor = invocation.getArgument(0);
            visitor.visit(childElement);
            return null;
        }).when(childElement).accept(any(HtmlVisitor.class));

        visitor.visit(element);

        String expectedOutput = "div#parent\n└── span#child\n";
        assertEquals(expectedOutput, visitor.getTreeOutput());
    }

    @Test
    void visitElement_shouldHandleMultipleChildren() {
        HtmlElement child1 = mock(HtmlElement.class);
        HtmlElement child2 = mock(HtmlElement.class);
        when(element.getTagName()).thenReturn("div");
        when(element.getId()).thenReturn("parent");
        when(element.getChildren()).thenReturn(List.of(child1, child2));
        when(element.isLastChild()).thenReturn(true);
        when(child1.getTagName()).thenReturn("span");
        when(child1.getId()).thenReturn("child1");
        when(child1.getChildren()).thenReturn(List.of());
        when(child1.isLastChild()).thenReturn(false);
        when(child2.getTagName()).thenReturn("span");
        when(child2.getId()).thenReturn("child2");
        when(child2.getChildren()).thenReturn(List.of());
        when(child2.isLastChild()).thenReturn(true);
        doAnswer(invocation -> {
            HtmlVisitor visitor = invocation.getArgument(0);
            visitor.visit(child1);
            return null;
        }).when(child1).accept(any(HtmlVisitor.class));
        doAnswer(invocation -> {
            HtmlVisitor visitor = invocation.getArgument(0);
            visitor.visit(child2);
            return null;
        }).when(child2).accept(any(HtmlVisitor.class));

        visitor.visit(element);

        String expectedOutput = "div#parent\n├── span#child1\n└── span#child2\n";
        assertEquals(expectedOutput, visitor.getTreeOutput());
    }
}