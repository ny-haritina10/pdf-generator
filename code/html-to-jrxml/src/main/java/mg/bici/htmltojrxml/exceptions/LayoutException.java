package mg.bici.htmltojrxml.exceptions;

/**
 * Exception for layout calculation errors.
 */
public class LayoutException extends ConversionException {
    
    public LayoutException(String message) {
        super(message, ErrorCode.LAYOUT_ERROR);
    }

    public LayoutException(String message, Throwable cause) {
        super(message, ErrorCode.LAYOUT_ERROR, cause);
    }
}