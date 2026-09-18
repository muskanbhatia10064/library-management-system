# Project Report: Library Management System

> **Note:** This report covers the standard sections most Java course
> evaluations expect (objective, design, implementation, testing,
> conclusion). Check your course's Projects page on VITyarthi for the exact
> required report template/format and adjust headings accordingly before
> submitting.

## 1. Title
Library Management System — A Console-Based Java Application

## 2. Objective
To design and implement a command-line application in core Java that
demonstrates object-oriented programming, exception handling, collections,
and file-based data persistence, by modelling a simplified library
workflow: cataloguing books, registering members, and issuing/returning
books.

## 3. Problem Statement
Manually tracking which books a library owns, how many copies are
available, and who currently holds which book is error-prone. This project
builds a lightweight console tool that a librarian could use to:
- Maintain a catalogue of books with total and available copy counts
- Maintain a list of registered members
- Issue a book to a member (only if a copy is available)
- Accept a returned book
- Persist all of this to disk so data is not lost between sessions

## 4. Technology Used
- **Language:** Java (JDK 17+)
- **Paradigm:** Object-Oriented Programming
- **Persistence:** Plain text files (no external database or libraries)
- **Interface:** Command-line (java.util.Scanner for input)

## 5. System Design

### 5.1 Class Overview
| Class | Responsibility |
|---|---|
| `Main` | Presents the CLI menu, reads user input, delegates to `Library` |
| `Library` | Core service: owns collections of books/members, enforces business rules, handles save/load |
| `Book` | Model for a single catalogue entry (id, title, author, copy counts) |
| `Member` | Model for a library member and the list of book IDs issued to them |
| `BookNotFoundException` | Custom checked exception for unknown book/member IDs |
| `BookNotAvailableException` | Custom checked exception for issuing a book with zero copies left |

### 5.2 OOP Concepts Demonstrated
- **Encapsulation:** `Book` and `Member` keep their fields private and
  expose controlled getters/mutators (e.g. `incrementAvailable()` rather
  than a public setter).
- **Separation of concerns:** `Main` only handles I/O; `Library` only
  handles business logic and persistence; `Book`/`Member` only hold state.
- **Custom exceptions:** Two checked exceptions model the two distinct
  failure modes when issuing a book, so callers must explicitly handle
  each case rather than relying on generic error codes.
- **Collections:** `LinkedHashMap<String, Book>` and
  `LinkedHashMap<String, Member>` give O(1) lookup by ID while preserving
  insertion order for display.

### 5.3 Data Flow
1. On startup, `Library` loads any existing `data/books.txt` and
   `data/members.txt` into memory.
2. All menu operations act on the in-memory maps.
3. Choosing "Save and exit" writes the current in-memory state back to
   both files, overwriting the previous contents.

## 6. Implementation Highlights
- Book and member records are serialised to simple pipe-delimited text
  lines (`id|field|field...`), which keeps the persistence layer
  dependency-free and easy to inspect/debug by opening the file directly.
- Issuing a book decrements `availableCopies` on the `Book` and adds the
  book ID to the `Member`'s issued list in a single transactional method
  (`Library.issueBook`), so the two pieces of state never go out of sync.
- Invalid numeric input (e.g. entering text for "number of copies") is
  handled with a retry loop rather than crashing the program.

## 7. Testing Performed
Manual testing was carried out by running the compiled program with a
sequence of menu inputs and confirming the printed output at each step,
including:
- Listing the seeded catalogue on first run
- Issuing an available book successfully and confirming the available
  count decremented
- Attempting to issue a book with zero copies left and confirming
  `BookNotAvailableException` is caught and a friendly message is shown
- Attempting to issue a non-existent book ID and confirming
  `BookNotFoundException` is caught and a friendly message is shown
- Returning a book and confirming the available count incremented again
- Exiting and restarting the program to confirm state was correctly
  persisted and reloaded from the data files

## 8. Conclusion
The project meets its objective of demonstrating core Java concepts
(OOP design, custom exceptions, collections, file I/O) in a small,
fully command-line-executable application with no external dependencies.
Possible extensions include due dates and fines, search by title/author,
and a switch to JSON or a real database for persistence.
