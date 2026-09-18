/**
 * Represents a single book title in the library catalogue.
 * totalCopies is the number owned; availableCopies tracks how many
 * are currently on the shelf (not issued out).
 */
public class Book {
    private final String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String id, String title, String author, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // Used when reloading state from the data file, where availableCopies
    // may differ from totalCopies because some copies are already issued.
    public Book(String id, String title, String author, int totalCopies, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void incrementAvailable() { availableCopies++; }
    public void decrementAvailable() { availableCopies--; }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    /** Serialises this book to a single pipe-delimited line for file storage. */
    public String toFileLine() {
        return id + "|" + title + "|" + author + "|" + totalCopies + "|" + availableCopies;
    }

    /** Reconstructs a Book from a line previously produced by toFileLine(). */
    public static Book fromFileLine(String line) {
        String[] parts = line.split("\\|");
        return new Book(
                parts[0],
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4])
        );
    }

    @Override
    public String toString() {
        return String.format("%-6s %-30s %-20s %d/%d available",
                id, title, author, availableCopies, totalCopies);
    }
}
