package mg.bici.htmltojrxml.css;

import mg.bici.htmltojrxml.utils.Color;

/**
 * Represents the computed CSS style for an element.
 */
public class ComputedStyle {
    
    // dimensions
    private CssLength width;
    private CssLength height;
    private CssLength minWidth;
    private CssLength maxWidth;

    // positioning
    private PositionType position;
    private CssLength top, right, bottom, left;
    private CssLength zIndex;

    // typography
    private String fontFamily;
    private CssLength fontSize;
    private FontWeight fontWeight;
    private FontStyle fontStyle;
    private Color color;

    // box model
    private Padding padding;
    private Margin margin;
    private Border border;
    private Color backgroundColor;

    // text
    private TextAlign textAlign;
    private TextDecoration textDecoration;
    private CssLength lineHeight;

    // display
    private String display;

    public ComputedStyle() {
        // initialize defaults
        width = height = minWidth = maxWidth = new CssLength(0, CssLength.Unit.PX);
        position = PositionType.STATIC;
        top = right = bottom = left = zIndex = new CssLength(0, CssLength.Unit.PX);
        fontFamily = "Arial";
        fontSize = new CssLength(10, CssLength.Unit.PT);
        color = new Color(0, 0, 0); // black
        padding = new Padding(0, 0, 0, 0);
        margin = new Margin(0, 0, 0, 0);
        border = new Border(0, "none", new Color(0, 0, 0));
        backgroundColor = new Color(255, 255, 255); // white
        textAlign = TextAlign.LEFT;
        textDecoration = TextDecoration.NONE;
        lineHeight = new CssLength(1.2f, CssLength.Unit.EM);
        fontWeight = FontWeight.NORMAL;
        fontStyle = FontStyle.NORMAL;
        display = "inline";
    }

    // getters and setters
    public CssLength getWidth() { return width; }
    public void setWidth(CssLength width) { this.width = width; }

    public CssLength getHeight() { return height; }
    public void setHeight(CssLength height) { this.height = height; }

    public CssLength getMinWidth() { return minWidth; }
    public void setMinWidth(CssLength minWidth) { this.minWidth = minWidth; }

    public CssLength getMaxWidth() { return maxWidth; }
    public void setMaxWidth(CssLength maxWidth) { this.maxWidth = maxWidth; }

    public PositionType getPosition() { return position; }
    public void setPosition(PositionType position) { this.position = position; }

    public CssLength getTop() { return top; }
    public void setTop(CssLength top) { this.top = top; }

    public CssLength getRight() { return right; }
    public void setRight(CssLength right) { this.right = right; }

    public CssLength getBottom() { return bottom; }
    public void setBottom(CssLength bottom) { this.bottom = bottom; }

    public CssLength getLeft() { return left; }
    public void setLeft(CssLength left) { this.left = left; }

    public CssLength getZIndex() { return zIndex; }
    public void setZIndex(CssLength zIndex) { this.zIndex = zIndex; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public CssLength getFontSize() { return fontSize; }
    public void setFontSize(CssLength fontSize) { this.fontSize = fontSize; }

    public FontWeight getFontWeight() { return fontWeight; }
    public void setFontWeight(FontWeight fontWeight) { this.fontWeight = fontWeight; }

    public FontStyle getFontStyle() { return fontStyle; }
    public void setFontStyle(FontStyle fontStyle) { this.fontStyle = fontStyle; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Padding getPadding() { return padding; }
    public void setPadding(Padding padding) { this.padding = padding; }

    public Margin getMargin() { return margin; }
    public void setMargin(Margin margin) { this.margin = margin; }

    public Border getBorder() { return border; }
    public void setBorder(Border border) { this.border = border; }

    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }

    public TextAlign getTextAlign() { return textAlign; }
    public void setTextAlign(TextAlign textAlign) { this.textAlign = textAlign; }

    public TextDecoration getTextDecoration() { return textDecoration; }
    public void setTextDecoration(TextDecoration textDecoration) { this.textDecoration = textDecoration; }

    public CssLength getLineHeight() { return lineHeight; }
    public void setLineHeight(CssLength lineHeight) { this.lineHeight = lineHeight; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }
}

/**
 * Represents a CSS length value with unit.
 */
class CssLength {
    private final float value;
    private final Unit unit;

    public enum Unit { PX, PT, EM, PERCENT, CM, MM, INCH }

    public CssLength(float value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public float getValue() { return value; }
    public Unit getUnit() { return unit; }
}

/**
 * Represents a position type.
 */
enum PositionType {
    STATIC, RELATIVE, ABSOLUTE, FIXED
}

/**
 * Represents font weight.
 */
enum FontWeight {
    NORMAL, BOLD, LIGHTER, BOLDER
}

/**
 * Represents font style.
 */
enum FontStyle {
    NORMAL, ITALIC, OBLIQUE
}

/**
 * Represents text alignment.
 */
enum TextAlign {
    LEFT, RIGHT, CENTER, JUSTIFY;

    public static TextAlign fromString(String value) {
        try {
            return TextAlign.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LEFT; // default
        }
    }
}

/**
 * Represents text decoration.
 */
enum TextDecoration {
    NONE, UNDERLINE, OVERLINE, LINE_THROUGH
}

/**
 * Represents padding box model.
 */
class Padding {
    private final float top, right, bottom, left;

    public Padding(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public float getTop() { return top; }
    public float getRight() { return right; }
    public float getBottom() { return bottom; }
    public float getLeft() { return left; }
}

/**
 * Represents margin box model.
 */
class Margin {
    private final float top, right, bottom, left;

    public Margin(float top, float right, float bottom, float left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public float getTop() { return top; }
    public float getRight() { return right; }
    public float getBottom() { return bottom; }
    public float getLeft() { return left; }
}

/**
 * Represents border properties.
 */
class Border {
    private final float width;
    private final String style;
    private final Color color;

    public Border(float width, String style, Color color) {
        this.width = width;
        this.style = style;
        this.color = color;
    }

    public float getWidth() { return width; }
    public String getStyle() { return style; }
    public Color getColor() { return color; }
}