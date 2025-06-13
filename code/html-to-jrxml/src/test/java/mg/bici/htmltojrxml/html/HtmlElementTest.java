package mg.bici.htmltojrxml.html;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HtmlElementTest {
    
    private HtmlElement element;

    @Before
    public void setUp() {
        element = new HtmlElement();
        element.setTagName("div");
        element.setId("test-id");
        element.setClasses(Arrays.asList("class1", "class2"));
        element.setText("Test content");
        Map<String, String> attributes = new HashMap<>();
        attributes.put("data-test", "value");
        element.setAttributes(attributes);
        Map<String, String> inlineStyles = new HashMap<>();
        inlineStyles.put("color", "blue");
        element.setInlineStyles(inlineStyles);

        HtmlElement child = new HtmlElement();
        child.setTagName("span");
        child.setParent(element);
        element.getChildren().add(child);
    }

    @Test
    public void testHasText_ValidText_ReturnsTrue() {
        assertTrue(element.hasText());
        element.setText("");
        assertFalse(element.hasText());
        element.setText(null);
        assertFalse(element.hasText());
    }

    @Test
    public void testHasChildren_WithChildren_ReturnsTrue() {
        assertTrue(element.hasChildren());
        element.setChildren(Arrays.asList());
        assertFalse(element.hasChildren());
    }

    @Test
    public void testGetChildrenByTag_FiltersByTagName() {
        HtmlElement child2 = new HtmlElement();
        child2.setTagName("p");
        element.getChildren().add(child2);

        assertEquals(1, element.getChildrenByTag("span").size());
        assertEquals(1, element.getChildrenByTag("p").size());
        assertEquals(2, element.getChildrenByTag("span", "p").size());
        assertEquals(0, element.getChildrenByTag("div").size());
    }

    @Test
    public void testGetAttribute_ReturnsCorrectValue() {
        assertEquals("value", element.getAttribute("data-test", "default"));
        assertEquals("default", element.getAttribute("non-existent", "default"));
    }

    @Test
    public void testHasClass_ValidClass_ReturnsTrue() {
        assertTrue(element.hasClass("class1"));
        assertTrue(element.hasClass("class2"));
        assertFalse(element.hasClass("class3"));
    }

    @Test
    public void testIsBlock_KnownBlockTags_ReturnsTrue() {
        element.setTagName("div");
        assertTrue(element.isBlock());
        element.setTagName("p");
        assertTrue(element.isBlock());
        element.setTagName("span");
        assertFalse(element.isBlock());
    }

    @Test
    public void testIsInline_KnownInlineTags_ReturnsTrue() {
        element.setTagName("span");
        assertTrue(element.isInline());
        element.setTagName("strong");
        assertTrue(element.isInline());
        element.setTagName("div");
        assertFalse(element.isInline());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("div", element.getTagName());
        assertEquals("test-id", element.getId());
        assertEquals(Arrays.asList("class1", "class2"), element.getClasses());
        assertEquals("Test content", element.getText());
        assertEquals("value", element.getAttributes().get("data-test"));
        assertEquals("blue", element.getInlineStyles().get("color"));
        assertEquals(1, element.getChildren().size());
        assertNotNull(element.getChildren().get(0).getParent());
    }
}