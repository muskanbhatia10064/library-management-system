package exceptions;

/**
 * Thrown when a book exists in the catalogue but has no copies
 * currently available to be issued.
 */
public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
