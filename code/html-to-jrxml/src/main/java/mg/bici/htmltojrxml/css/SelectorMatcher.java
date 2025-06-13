package mg.bici.htmltojrxml.css;

import mg.bici.htmltojrxml.html.HtmlElement;

/**
 * Matches CSS selectors to HTML elements.
 */
public class SelectorMatcher {
    /**
     * Checks if an element matches a selector.
     *
     * @param element  the HTML element
     * @param selector the CSS selector
     * @return true if matches
     */
    public boolean matches(HtmlElement element, String selector) {
        if (selector.contains(">")) {
            return matchesChildSelector(element, selector);
        }
        String[] parts = selector.trim().split("\\s+");
        if (parts.length > 1) {
            // handle descendant selector
            return matchesDescendantSelector(element, selector);
        }
        return matchesSimpleSelector(element, parts[0]);
    }

    /**
     * Matches a simple selector (e.g., tag, .class, #id).
     *
     * @param element  the HTML element
     * @param selector the simple selector
     * @return true if matches
     */
    public boolean matchesSimpleSelector(HtmlElement element, String selector) {
        if (selector.startsWith("#")) {
            return matchesIdSelector(element, selector.substring(1));
        } else if (selector.startsWith(".")) {
            return matchesClassSelector(element, selector.substring(1));
        } else if (selector.contains("[")) {
            return matchesAttributeSelector(element, selector);
        } else {
            return element.getTagName().equalsIgnoreCase(selector);
        }
    }

    /**
     * Matches a descendant selector (e.g., "div p").
     *
     * @param element  the HTML element
     * @param selector the descendant selector
     * @return true if matches
     */
    public boolean matchesDescendantSelector(HtmlElement element, String selector) {
        String[] parts = selector.trim().split("\\s+");
        HtmlElement current = element;
        for (int i = parts.length - 1; i >= 0; i--) {
            if (!matchesSimpleSelector(current, parts[i])) {
                return false;
            }
            current = current.getParent();
            if (current == null && i > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Matches a child selector (e.g., "div > p").
     *
     * @param element  the HTML element
     * @param selector the child selector
     * @return true if matches
     */
    public boolean matchesChildSelector(HtmlElement element, String selector) {
        String[] parts = selector.trim().split("\\s*>\\s*");
        if (parts.length != 2) return false;
        if (!matchesSimpleSelector(element, parts[1].trim())) return false;
        HtmlElement parent = element.getParent();
        return parent != null && matchesSimpleSelector(parent, parts[0].trim());
    }

    /**
     * Matches a class selector (e.g., ".class").
     *
     * @param element    the HTML element
     * @param className  the class name
     * @return true if matches
     */
    public boolean matchesClassSelector(HtmlElement element, String className) {
        return element.hasClass(className);
    }

    /**
     * Matches an ID selector (e.g., "#id").
     *
     * @param element the HTML element
     * @param id      the ID
     * @return true if matches
     */
    public boolean matchesIdSelector(HtmlElement element, String id) {
        return id.equals(element.getId());
    }

    /**
     * Matches an attribute selector (e.g., "[data-test]").
     *
     * @param element   the HTML element
     * @param selector  the attribute selector
     * @return true if matches
     */
    public boolean matchesAttributeSelector(HtmlElement element, String selector) {
        String attribute = selector.replaceAll("\\[|\\]", "");
        return element.getAttributes().containsKey(attribute);
    }
}