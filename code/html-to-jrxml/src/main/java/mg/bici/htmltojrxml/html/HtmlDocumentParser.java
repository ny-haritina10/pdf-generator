package mg.bici.htmltojrxml.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mg.bici.htmltojrxml.exceptions.ParsingException;

/**
 * Parses HTML content into a structured format for JRXML conversion.
 */
public class HtmlDocumentParser {
    /**
     * Parses HTML content into a ParsedDocument.
     *
     * @param htmlContent the HTML content to parse
     * @return ParsedDocument containing elements and styles
     */
    public ParsedDocument parseHtml(String htmlContent) {
        try {
            Document doc = Jsoup.parse(htmlContent);
            // basic validation for unclosed tags
            validateHtmlStructure(doc, htmlContent);
            Map<String, String> inlineStyles = extractInlineStyles(doc);
            List<HtmlElement> elements = extractElements(doc.body());
            return new ParsedDocument(elements, inlineStyles);
        } catch (Exception e) {
            throw new ParsingException("Failed to parse HTML content", e);
        }
    }

    /**
     * Extracts HTML elements from the body.
     *
     * @param body the body element
     * @return list of HtmlElement objects
     */
    public List<HtmlElement> extractElements(Element body) {
        List<HtmlElement> elements = new ArrayList<>();
        for (Element element : body.children()) {
            if (isReportableElement(element)) {
                elements.add(createHtmlElement(element, 0));
            }
        }
        return elements;
    }

    /**
     * Extracts inline styles from the document.
     *
     * @param doc the JSoup document
     * @return map of element IDs to inline styles
     */
    public Map<String, String> extractInlineStyles(Document doc) {
        Map<String, String> inlineStyles = new HashMap<>();
        Elements elementsWithId = doc.select("[id]");
        for (Element element : elementsWithId) {
            String id = element.id();
            if (!id.isEmpty()) {
                String style = element.attr("style");
                inlineStyles.put(id, style != null ? style : "");
            }
        }
        return inlineStyles;
    }

    /**
     * Checks if an element is reportable (e.g., not a script or style tag).
     *
     * @param element the JSoup element
     * @return true if reportable
     */
    public boolean isReportableElement(Element element) {
        String tagName = element.tagName().toLowerCase();
        // exclude non-reportable tags
        if (tagName.equals("script") || tagName.equals("style") || tagName.equals("meta") || tagName.equals("link")) {
            return false;
        }
        // include elements with text, children, or non-block elements
        return element.hasText() || element.children().size() > 0 || !element.isBlock();
    }

    // creates HTML element with depth tracking
    private HtmlElement createHtmlElement(Element element, int depth) {
        HtmlElement htmlElement = HtmlElementFactory.createFromJsoupElement(element);
        for (Element child : element.children()) {
            if (isReportableElement(child)) {
                HtmlElement childElement = createHtmlElement(child, depth + 1);
                childElement.setParent(htmlElement);
                htmlElement.getChildren().add(childElement);
            }
        }
        return htmlElement;
    }

    // extracts text content
    private String extractText(Element element) {
        return element.ownText().trim();
    }

    // extracts attributes as map
    private Map<String, String> extractAttributes(Element element) {
        Map<String, String> attributes = new HashMap<>();
        element.attributes().forEach(attr -> attributes.put(attr.getKey(), attr.getValue()));
        return attributes;
    }

    // validates HTML structure for unclosed tags
    private void validateHtmlStructure(Document doc, String htmlContent) {
        // count opening and closing tags in raw input
        int openingTags = htmlContent.split("<[a-zA-Z]").length - 1;
        int closingTags = htmlContent.split("</[a-zA-Z]").length - 1;
        if (openingTags > closingTags) {
            throw new ParsingException("Invalid HTML: Unclosed tags detected");
        }
    }
}

/**
 * Represents the parsed HTML document.
 */
class ParsedDocument {
    private final List<HtmlElement> elements;
    private final Map<String, String> inlineStyles;

    public ParsedDocument(List<HtmlElement> elements, Map<String, String> inlineStyles) {
        this.elements = elements;
        this.inlineStyles = inlineStyles;
    }

    public List<HtmlElement> getElements() {
        return elements;
    }

    public Map<String, String> getInlineStyles() {
        return inlineStyles;
    }
}