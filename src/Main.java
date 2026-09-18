import exceptions.BookNotAvailableException;
import exceptions.BookNotFoundException;

import java.util.Scanner;

/**
 * Entry point: a simple text menu that lets the user manage the
 * library catalogue, register members, and issue/return books.
 * State is loaded from and saved back to data/books.txt and
 * data/members.txt so it persists between runs.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Library library;

    public static void main(String[] args) {
        library = new Library("data/books.txt", "data/members.txt");
        seedIfEmpty();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> listBooks();
                case "2" -> addBook();
                case "3" -> addMember();
                case "4" -> listMembers();
                case "5" -> issueBook();
                case "6" -> returnBook();
                case "7" -> {
                    library.saveAll();
                    System.out.println("Saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please pick a number 1-7.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. List all books");
        System.out.println("2. Add a new book");
        System.out.println("3. Register a new member");
        System.out.println("4. List all members");
        System.out.println("5. Issue a book to a member");
        System.out.println("6. Return a book");
        System.out.println("7. Save and exit");
        System.out.print("Enter choice: ");
    }

    private static void listBooks() {
        System.out.println("\n-- Catalogue --");
        if (library.listBooks().isEmpty()) {
            System.out.println("No books in the catalogue yet.");
        }
        for (var book : library.listBooks()) {
            System.out.println(book);
        }
    }

    private static void addBook() {
        System.out.print("Book ID (e.g. B004): ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Number of copies: ");
        int copies = readInt();

        library.addBook(id, title, author, copies);
        System.out.println("Added '" + title + "' with " + copies + " cop" + (copies == 1 ? "y" : "ies") + ".");
    }

    private static void addMember() {
        System.out.print("Member ID (e.g. M004): ");
        String id = scanner.nextLine().trim();
        System.out.print("Member name: ");
        String name = scanner.nextLine().trim();

        library.addMember(id, name);
        System.out.println("Registered member '" + name + "'.");
    }

    private static void listMembers() {
        System.out.println("\n-- Members --");
        if (library.listMembers().isEmpty()) {
            System.out.println("No members registered yet.");
        }
        for (var member : library.listMembers()) {
            System.out.println(member);
        }
    }

    private static void issueBook() {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Book ID: ");
        String bookId = scanner.nextLine().trim();

        try {
            library.issueBook(memberId, bookId);
            System.out.println("Book issued successfully.");
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Could not issue book: " + e.getMessage());
        }
    }

    private static void returnBook() {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Book ID: ");
        String bookId = scanner.nextLine().trim();

        try {
            library.returnBook(memberId, bookId);
            System.out.println("Book returned successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Could not return book: " + e.getMessage());
        }
    }

    private static int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid whole number: ");
            }
        }
    }

    /** Adds a couple of sample records the very first time the app runs, so the menu isn't empty. */
    private static void seedIfEmpty() {
        if (library.listBooks().isEmpty()) {
            library.addBook("B001", "Clean Code", "Robert C. Martin", 2);
            library.addBook("B002", "Effective Java", "Joshua Bloch", 3);
            library.addBook("B003", "Introduction to Algorithms", "Cormen et al.", 1);
        }
        if (library.listMembers().isEmpty()) {
            library.addMember("M001", "Aditi Sharma");
            library.addMember("M002", "Rohan Verma");
        }
    }
}
