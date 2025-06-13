package mg.bici.htmltojrxml.css;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import mg.bici.htmltojrxml.utils.Color;

public class ComputedStyleTest {
    private ComputedStyle style;

    @Before
    public void setUp() {
        style = new ComputedStyle();
    }

    @Test
    public void testDefaultValues() {
        assertEquals(0f, style.getWidth().getValue(), 0.01);
        assertEquals(CssLength.Unit.PX, style.getWidth().getUnit());
        assertEquals(PositionType.STATIC, style.getPosition());
        assertEquals("Arial", style.getFontFamily()); // from CssStyleAnalyzer defaults
        assertEquals(10f, style.getFontSize().getValue(), 0.01);
        assertEquals(FontWeight.NORMAL, style.getFontWeight());
        assertEquals(TextAlign.LEFT, style.getTextAlign());
        assertEquals(new Color(255, 255, 255), style.getBackgroundColor());
        assertEquals("inline", style.getDisplay());
    }

    @Test
    public void testSetAndGetWidth() {
        CssLength width = new CssLength(100, CssLength.Unit.PX);
        style.setWidth(width);
        assertEquals(width, style.getWidth());
    }

    @Test
    public void testSetAndGetFontProperties() {
        style.setFontFamily("Helvetica");
        style.setFontSize(new CssLength(14, CssLength.Unit.PT));
        style.setFontWeight(FontWeight.BOLD);
        style.setFontStyle(FontStyle.ITALIC);

        assertEquals("Helvetica", style.getFontFamily());
        assertEquals(14f, style.getFontSize().getValue(), 0.01);
        assertEquals(FontWeight.BOLD, style.getFontWeight());
        assertEquals(FontStyle.ITALIC, style.getFontStyle());
    }

    @Test
    public void testSetAndGetBoxModel() {
        Padding padding = new Padding(5, 10, 5, 10);
        Margin margin = new Margin(20, 20, 20, 20);
        Border border = new Border(1, "solid", new Color(0, 0, 0));

        style.setPadding(padding);
        style.setMargin(margin);
        style.setBorder(border);

        assertEquals(5f, style.getPadding().getTop(), 0.01);
        assertEquals(20f, style.getMargin().getTop(), 0.01);
        assertEquals(1f, style.getBorder().getWidth(), 0.01);
    }

    @Test
    public void testSetAndGetColor() {
        Color color = new Color(255, 0, 0);
        style.setColor(color);
        assertEquals(color, style.getColor());
    }

    @Test
    public void testSetAndGetTextProperties() {
        style.setTextAlign(TextAlign.CENTER);
        style.setTextDecoration(TextDecoration.UNDERLINE);
        style.setLineHeight(new CssLength(1.5f, CssLength.Unit.EM));

        assertEquals(TextAlign.CENTER, style.getTextAlign());
        assertEquals(TextDecoration.UNDERLINE, style.getTextDecoration());
        assertEquals(1.5f, style.getLineHeight().getValue(), 0.01);
    }
}