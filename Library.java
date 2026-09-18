import exceptions.BookNotAvailableException;
import exceptions.BookNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service class: owns the in-memory catalogue and membership
 * records, and persists them to plain-text files under data/ so
 * state survives between runs.
 */
public class Library {
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();

    private final String booksFile;
    private final String membersFile;

    public Library(String booksFile, String membersFile) {
        this.booksFile = booksFile;
        this.membersFile = membersFile;
        load();
    }

    // ---------- Book operations ----------

    public void addBook(String id, String title, String author, int copies) {
        books.put(id, new Book(id, title, author, copies));
    }

    public Book findBook(String id) throws BookNotFoundException {
        Book b = books.get(id);
        if (b == null) {
            throw new BookNotFoundException("No book found with ID: " + id);
        }
        return b;
    }

    public List<Book> listBooks() {
        return new ArrayList<>(books.values());
    }

    // ---------- Member operations ----------

    public void addMember(String id, String name) {
        members.put(id, new Member(id, name));
    }

    public Member findMember(String id) throws BookNotFoundException {
        Member m = members.get(id);
        if (m == null) {
            throw new BookNotFoundException("No member found with ID: " + id);
        }
        return m;
    }

    public List<Member> listMembers() {
        return new ArrayList<>(members.values());
    }

    // ---------- Issue / return workflow ----------

    public void issueBook(String memberId, String bookId)
            throws BookNotFoundException, BookNotAvailableException {
        Member member = findMember(memberId);
        Book book = findBook(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                    "'" + book.getTitle() + "' has no copies available right now.");
        }
        book.decrementAvailable();
        member.issueBook(bookId);
    }

    public void returnBook(String memberId, String bookId) throws BookNotFoundException {
        Member member = findMember(memberId);
        Book book = findBook(bookId);

        if (!member.getIssuedBookIds().contains(bookId)) {
            throw new BookNotFoundException(
                    member.getName() + " does not currently have this book issued.");
        }
        member.returnBook(bookId);
        book.incrementAvailable();
    }

    // ---------- Persistence ----------

    /** Loads books and members from disk if the data files already exist. */
    private void load() {
        loadBooks();
        loadMembers();
    }

    private void loadBooks() {
        File file = new File(booksFile);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Book book = Book.fromFileLine(line);
                books.put(book.getId(), book);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load books file (" + e.getMessage() + ")");
        }
    }

    private void loadMembers() {
        File file = new File(membersFile);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Member member = Member.fromFileLine(line);
                members.put(member.getId(), member);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load members file (" + e.getMessage() + ")");
        }
    }

    /** Writes the current in-memory state back to the data files. Call before exiting. */
    public void saveAll() {
        saveBooks();
        saveMembers();
    }

    private void saveBooks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(booksFile))) {
            for (Book b : books.values()) {
                writer.println(b.toFileLine());
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save books file (" + e.getMessage() + ")");
        }
    }

    private void saveMembers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(membersFile))) {
            for (Member m : members.values()) {
                writer.println(m.toFileLine());
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save members file (" + e.getMessage() + ")");
        }
    }
}
