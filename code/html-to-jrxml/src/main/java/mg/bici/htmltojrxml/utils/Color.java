package mg.bici.htmltojrxml.utils;

/**
 * Represents an RGB color.
 */
public class Color {
    
    private final int red, green, blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return red == color.red && green == color.green && blue == color.blue;
    }

    @Override
    public int hashCode() {
        return 31 * red + 17 * green + blue;
    }

    @Override
    public String toString() {
        return "Color(" + red + ", " + green + ", " + blue + ")";
    }
}