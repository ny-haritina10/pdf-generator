package mg.bici.htmltojrxml.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern RGB_PATTERN = Pattern.compile("rgb\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
    
    public static Color parseColor(String colorString) {
        if (colorString == null) {
            System.err.println("Color string is null, returning black");
            return new Color(0, 0, 0); // default black
        }
        
        colorString = colorString.trim().toLowerCase();
        
        // Handle hex colors
        if (colorString.startsWith("#")) {
            try {
                String hex = colorString.substring(1).replaceAll("\\s", "");
                if (hex.length() == 3) {
                    hex = "" + hex.charAt(0) + hex.charAt(0) +
                          hex.charAt(1) + hex.charAt(1) +
                          hex.charAt(2) + hex.charAt(2);
                }
                if (hex.length() != 6) {
                    System.err.println("Invalid hex color: " + colorString);
                    return new Color(0, 0, 0);
                }
                int rgb = Integer.parseInt(hex, 16);
                return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse hex color: " + colorString + ", " + e.getMessage());
                return new Color(0, 0, 0); // fallback black
            }
        }
        
        // Handle RGB functional notation
        if (colorString.startsWith("rgb(")) {
            Matcher matcher = RGB_PATTERN.matcher(colorString);
            if (matcher.matches()) {
                try {
                    int r = Integer.parseInt(matcher.group(1));
                    int g = Integer.parseInt(matcher.group(2));
                    int b = Integer.parseInt(matcher.group(3));
                    
                    // Clamp values to 0-255 range
                    r = Math.max(0, Math.min(255, r));
                    g = Math.max(0, Math.min(255, g));
                    b = Math.max(0, Math.min(255, b));
                    
                    return new Color(r, g, b);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse RGB values: " + colorString + ", " + e.getMessage());
                    return new Color(0, 0, 0);
                }
            } else {
                System.err.println("Invalid RGB format: " + colorString);
                return new Color(0, 0, 0);
            }
        }
        
        // Handle named colors
        switch (colorString) {
            case "red":
                return new Color(255, 0, 0);
            case "blue":
                return new Color(0, 0, 255);
            case "black":
                return new Color(0, 0, 0);
            case "white":
                return new Color(255, 255, 255);
            default:
                System.err.println("Unknown color name: " + colorString);
                return new Color(0, 0, 0); // fallback black
        }
    }
}