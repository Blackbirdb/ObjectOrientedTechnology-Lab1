package org.example.utils;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlParserTest {
    private HtmlParser parser;
    private HtmlDocument htmlDocument;

    @BeforeEach
    void setUp() {
        htmlDocument = mock(HtmlDocument.class);
        HtmlElementFactory factory = mock(HtmlElementFactory.class);
        when(htmlDocument.getFactory()).thenReturn(factory);
        parser = new HtmlParser();
    }

    @Test
    void parse_shouldReturnHtmlDocumentForValidHtml() throws Exception {
        String html = "<html><head><title>Title</title></head><body><div id='content'>Text</div></body></html>";
        HtmlDocument result = parser.parse(html);
        assertNotNull(result);
    }

    @Test
    void parse_shouldThrowExceptionForInvalidHtmlStructure() {
        String html = "<html><head><title></title></head></html>";
        assertThrows(IllegalArgumentException.class, () -> parser.parse(html));
    }

    @Test
    void parse_shouldHandleEmptyBody() throws Exception {
        String html = "<html><head><title>Title</title></head><body></body></html>";
        HtmlDocument result = parser.parse(html);
        assertNotNull(result);
    }

    @Test
    void parse_shouldHandleNestedElements() throws Exception {
        String html = "<html><head><title>Title</title></head><body><div id='content'><span id='span'>Text</span></div></body></html>";
        HtmlDocument result = parser.parse(html);
        assertNotNull(result);
    }

    @Test
    void parse_shouldHandleElementsWithAttributes() throws Exception {
        String html = "<html><head><title>Title</title></head><body><div id='content' class='main'>Text</div></body></html>";
        HtmlDocument result = parser.parse(html);
        assertNotNull(result);
    }

    @Test
    void rebuild_shouldReturnJsoupDocument() {
        HtmlElement rootElement = mock(HtmlElement.class);
        when(htmlDocument.getRoot()).thenReturn(rootElement);
        when(rootElement.getTagName()).thenReturn("html");

        Document result = parser.rebuild(htmlDocument);
        assertNotNull(result);
    }

    @Test
    void rebuild_shouldHandleEmptyDocument() {
        HtmlElement rootElement = mock(HtmlElement.class);
        when(htmlDocument.getRoot()).thenReturn(rootElement);
        when(rootElement.getTagName()).thenReturn("html");
        when(rootElement.getChildren()).thenReturn(List.of());

        Document result = parser.rebuild(htmlDocument);
        assertNotNull(result);
    }

    @Test
    void rebuild_shouldHandleNestedElements() {
        HtmlElement rootElement = mock(HtmlElement.class);
        HtmlElement childElement = mock(HtmlElement.class);
        when(htmlDocument.getRoot()).thenReturn(rootElement);
        when(rootElement.getTagName()).thenReturn("html");
        when(rootElement.getChildren()).thenReturn(List.of(childElement));
        when(childElement.getTagName()).thenReturn("body");

        Document result = parser.rebuild(htmlDocument);
        assertNotNull(result);
    }

    @Test
    void rebuild_shouldHandleElementsWithAttributes() {
        HtmlElement rootElement = mock(HtmlElement.class);
        HtmlElement childElement = mock(HtmlElement.class);
        when(htmlDocument.getRoot()).thenReturn(rootElement);
        when(rootElement.getTagName()).thenReturn("html");
        when(rootElement.getChildren()).thenReturn(List.of(childElement));
        when(childElement.getTagName()).thenReturn("div");
        when(childElement.getId()).thenReturn("content");

        Document result = parser.rebuild(htmlDocument);
        assertNotNull(result);
    }
}