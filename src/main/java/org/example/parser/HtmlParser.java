package org.example.parser;

import org.example.document.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParser {
    private final HtmlDocument htmlDocument;
    private final HtmlElementFactory factory;

    public HtmlParser(HtmlDocument document) {
        this.htmlDocument = document;
        this.factory = document.getFactory();
    }

    /**
     * Parses the given HTML string and returns an HtmlDocument object.
     * @param html
     * @return htmlDocument
     */
    public HtmlDocument parse(String html) {
        Document jsoupDoc = Jsoup.parse(html);

        validateDocumentStructure(jsoupDoc);

        Element jsoupRootElement = jsoupDoc.select("html").first();
        assert jsoupRootElement != null;
        HtmlElement htmlRootElement = parseElement(jsoupRootElement, null);
        this.htmlDocument.setRoot(htmlRootElement);

        return htmlDocument;
    }


    /**
     * Parses a Jsoup Element(and its children, recurrently), and creates an HtmlElement.
     * @param jsoupElement
     * @return HtmlElement
     */
    private HtmlElement parseElement(Element jsoupElement, HtmlElement parent) {
        // 获取标签名和ID
        String tagName = jsoupElement.tagName();
        String id = jsoupElement.id();

        if (id.isEmpty()) {
            if (!isSpecialTag(tagName)) {
                throw new IllegalArgumentException("Tag <" + tagName + "> is not a special tag, and no id is given.");
            }
            id = tagName;
        }

        HtmlElement element = this.factory.createElement(tagName, id, jsoupElement.ownText(), parent);

        // 递归处理子元素
        for (Element child : jsoupElement.children()) {
            HtmlNode childNode;
            if (child.tagName().equals("#text")) {
                childNode = new HtmlTextNode(child.text());
            } else {
                childNode = parseElement(child, element);
            }
            element.addChild(childNode);
        }

        return element;
    }

    /**
     * Rebuilds a Jsoup Document from an HtmlDocument.
     * @param myDocument
     * @return jsoupDoc
     */
    public static Document rebuild(HtmlDocument myDocument) {
        Document jsoupDoc = new Document("");

        Element htmlElement = rebuildElement(myDocument.getRoot());
        jsoupDoc.appendChild(htmlElement);

        return jsoupDoc;
    }

    /**
     * Rebuilds a Jsoup Element from an HtmlElement.
     * @param myElement
     * @return Element
     */
    private static Element rebuildElement(HtmlElement myElement) {
        Element jsoupElement = new Element(myElement.getTagName());

        assert myElement.getId() != null;
        jsoupElement.attr("id", myElement.getId());

        if (myElement.getTextContent() != null) {
            jsoupElement.text(myElement.getTextContent());
        }

        for (HtmlNode child : myElement.getChildren()) {
            if (child instanceof HtmlElement) {
                jsoupElement.appendChild(rebuildElement((HtmlElement) child));
            } else if (child instanceof HtmlTextNode) {
                jsoupElement.appendText(((HtmlTextNode) child).getText());
            }
        }

        return jsoupElement;
    }

    private boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }

    /**
     * checks if a document has the correct structure.
     *
     * @param jsoupDoc
     */
    private void validateDocumentStructure(Document jsoupDoc) {
        if (jsoupDoc.select("html").size() != 1) {
            throw new IllegalArgumentException("Document must have exactly one html element");
        }

        Element html = jsoupDoc.select("html").first();
        if (html.children().select("head").size() != 1) {
            throw new IllegalArgumentException("html must have exactly one head child");
        }

        Element head = html.children().select("head").first();
        if (head.children().select("title").size() != 1) {
            throw new IllegalArgumentException("head must have exactly one title child");
        }

        if (html.children().select("body").size() != 1) {
            throw new IllegalArgumentException("html must have exactly one body child");
        }
    }
}