import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member and the set of book IDs currently
 * issued to them.
 */
public class Member {
    private final String id;
    private String name;
    private final List<String> issuedBookIds;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.issuedBookIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getIssuedBookIds() { return issuedBookIds; }

    public void issueBook(String bookId) {
        issuedBookIds.add(bookId);
    }

    public void returnBook(String bookId) {
        issuedBookIds.remove(bookId);
    }

    /** Serialises this member to a pipe-delimited line; issued IDs are comma-joined. */
    public String toFileLine() {
        String issued = String.join(",", issuedBookIds);
        return id + "|" + name + "|" + issued;
    }

    public static Member fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        Member m = new Member(parts[0], parts[1]);
        if (parts.length > 2 && !parts[2].isEmpty()) {
            for (String bookId : parts[2].split(",")) {
                m.issueBook(bookId);
            }
        }
        return m;
    }

    @Override
    public String toString() {
        return String.format("%-6s %-20s issued: %s", id, name,
                issuedBookIds.isEmpty() ? "none" : String.join(", ", issuedBookIds));
    }
}
