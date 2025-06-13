package mg.bici.htmltojrxml.exceptions;

/**
 * Exception for JRXML validation errors.
 */
public class ValidationException extends ConversionException {
    
    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, ErrorCode.VALIDATION_ERROR, cause);
    }
}