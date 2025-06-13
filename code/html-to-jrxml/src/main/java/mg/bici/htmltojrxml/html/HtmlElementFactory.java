package mg.bici.htmltojrxml.html;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Factory for creating HtmlElement instances.
 */
public class HtmlElementFactory {
    
    /**
     * Creates an HtmlElement from a JSoup Element.
     *
     * @param element the JSoup element
     * @return HtmlElement instance
     */
    public static HtmlElement createFromJsoupElement(Element element) {
        HtmlElement htmlElement = new HtmlElement();
        htmlElement.setTagName(element.tagName());
        htmlElement.setId(element.id());
        htmlElement.setClasses(Arrays.asList(element.classNames().toArray(new String[0])));
        htmlElement.setText(element.ownText().trim());
        
        // extract attributes
        Map<String, String> attributes = new HashMap<>();
        element.attributes().forEach(attr -> attributes.put(attr.getKey(), attr.getValue()));
        htmlElement.setAttributes(attributes);
        
        // extract inline styles
        String styleAttr = element.attr("style");
        if (!styleAttr.isEmpty()) {
            Map<String, String> inlineStyles = new HashMap<>();
            String[] stylePairs = styleAttr.split(";");
            for (String pair : stylePairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    inlineStyles.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            }
            htmlElement.setInlineStyles(inlineStyles);
        }
        
        return htmlElement;
    }

    /**
     * Creates a text-only HtmlElement.
     *
     * @param text the text content
     * @return HtmlElement instance
     */
    public static HtmlElement createTextElement(String text) {
        HtmlElement element = new HtmlElement();
        element.setTagName("span");
        element.setText(text);
        return element;
    }

    /**
     * Creates a container element with the specified tag.
     *
     * @param tagName the tag name
     * @return HtmlElement instance
     */
    public static HtmlElement createContainerElement(String tagName) {
        HtmlElement element = new HtmlElement();
        element.setTagName(tagName);
        return element;
    }

    /**
     * Creates HtmlElements from a JSoup Elements list.
     *
     * @param elements the JSoup elements
     * @return list of HtmlElement instances
     */
    public static List<HtmlElement> createFromNodeList(Elements elements) {
        return elements.stream()
                .map(HtmlElementFactory::createFromJsoupElement)
                .collect(Collectors.toList());
    }
}