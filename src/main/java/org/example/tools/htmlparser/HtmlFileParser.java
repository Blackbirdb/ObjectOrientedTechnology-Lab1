package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;

public interface HtmlFileParser {
    String rebuild(HtmlDocument myDocument);
    HtmlDocument parse(String html, HtmlDocument htmlDocument);
}
