# Library Management System (Java Console Application)

A simple command-line Library Management System built in core Java. It lets you
maintain a catalogue of books, register members, and issue/return books, with
all data persisted to plain text files so it survives between runs.

This project was built as the Evaluated Course Project for the Java
programming course.

## Features

- Add books to the catalogue and track total vs. available copies
- Register library members
- Issue a book to a member (fails gracefully if the book doesn't exist or has
  no copies left)
- Return a book (fails gracefully if that member doesn't actually have it)
- List all books and all members with their currently issued books
- Automatic save/load of state to `data/books.txt` and `data/members.txt`
- Custom checked exceptions (`BookNotFoundException`,
  `BookNotAvailableException`) for clear, specific error handling
- Core OOP design: encapsulated `Book` and `Member` model classes, a
  `Library` service class, and a thin `Main` class for the CLI menu

## Project Structure

```
LibraryManagementSystem/
├── src/
│   ├── Main.java                          # CLI menu / entry point
│   ├── Book.java                          # Book model
│   ├── Member.java                        # Member model
│   ├── Library.java                       # Business logic + file persistence
│   └── exceptions/
│       ├── BookNotFoundException.java
│       └── BookNotAvailableException.java
├── data/                                  # Created/used at runtime for persistence
├── README.md
└── PROJECT_REPORT.md
```

## Prerequisites

- Java Development Kit (JDK) 17 or later installed
  (verify with `java -version` and `javac -version`)
- Any terminal / command prompt (Windows, macOS, or Linux)
- No external libraries or build tools (Maven/Gradle) are required — this is
  plain, dependency-free Java

## Setup and How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/{your-username}/{your-repo-name}.git
   cd {your-repo-name}
   ```

2. **Compile the source code**

   From the project root directory, run:
   ```bash
   javac -d out src/Main.java src/Book.java src/Member.java src/Library.java src/exceptions/*.java
   ```
   This compiles all `.java` files and places the resulting `.class` files
   into an `out/` folder (created automatically).

3. **Run the application**

   Still from the project root:
   ```bash
   java -cp out Main
   ```

4. **Use the menu**

   You'll see a numbered menu in the terminal:
   ```
   ===== Library Management System =====
   1. List all books
   2. Add a new book
   3. Register a new member
   4. List all members
   5. Issue a book to a member
   6. Return a book
   7. Save and exit
   Enter choice:
   ```
   Type the number of the action you want and press Enter, then follow the
   prompts. On first run, the app seeds three sample books (`B001`-`B003`)
   and two sample members (`M001`, `M002`) so the menu isn't empty.

5. **Exit and persistence**

   Choose option `7` to save your changes and exit. State is written to
   `data/books.txt` and `data/members.txt` (created automatically on first
   save). The next time you run the program, it will pick up right where you
   left off.

## Example Session

```
Enter choice: 5
Member ID: M001
Book ID: B003
Book issued successfully.
```

## Notes for the Evaluator

- The project is fully executable from the command line with no GUI
  dependency, as required.
- No third-party dependencies are used, so no dependency installation step is
  needed beyond having a JDK available.
- Re-running `javac`/`java` from the project root as shown above is enough to
  build and execute the project from a clean checkout.
