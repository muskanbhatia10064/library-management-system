package exceptions;

/**
 * Thrown when a requested book ID does not exist in the library catalogue.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
