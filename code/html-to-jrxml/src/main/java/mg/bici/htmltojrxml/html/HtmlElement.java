package mg.bici.htmltojrxml.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an HTML element with attributes, styles, and hierarchy.
 */
public class HtmlElement {
    
    private String tagName;
    private String id;
    private List<String> classes;
    private String text;
    private Map<String, String> attributes;
    private List<HtmlElement> children;
    private HtmlElement parent;
    private Map<String, String> inlineStyles;

    public HtmlElement() {
        classes = new ArrayList<>();
        attributes = new HashMap<>();
        children = new ArrayList<>();
        inlineStyles = new HashMap<>();
    }

    // checks if element has text content
    public boolean hasText() {
        return text != null && !text.trim().isEmpty();
    }

    // checks if element has children
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    // gets children by tag names
    public List<HtmlElement> getChildrenByTag(String... tags) {
        List<String> tagList = Arrays.asList(tags);
        return children.stream()
                .filter(child -> tagList.contains(child.getTagName()))
                .collect(Collectors.toList());
    }

    // gets attribute with default value
    public String getAttribute(String name, String defaultValue) {
        return attributes.getOrDefault(name, defaultValue);
    }

    // checks if element has a specific class
    public boolean hasClass(String className) {
        return classes.contains(className);
    }

    // checks if element is a block element
    public boolean isBlock() {
        String[] blockTags = {"div", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "table"};
        return Arrays.asList(blockTags).contains(tagName.toLowerCase());
    }

    // checks if element is an inline element
    public boolean isInline() {
        String[] inlineTags = {"span", "a", "strong", "em", "b", "i", "img"};
        return Arrays.asList(inlineTags).contains(tagName.toLowerCase());
    }

    // getters and setters
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<HtmlElement> getChildren() {
        return children;
    }

    public void setChildren(List<HtmlElement> children) {
        this.children = children;
    }

    public HtmlElement getParent() {
        return parent;
    }

    public void setParent(HtmlElement parent) {
        this.parent = parent;
    }

    public Map<String, String> getInlineStyles() {
        return inlineStyles;
    }

    public void setInlineStyles(Map<String, String> inlineStyles) {
        this.inlineStyles = inlineStyles;
    }
}