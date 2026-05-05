package library.model;

/**
 * Book class - Represents a book in the Library Management System
 */
public class Book {

    // ========== FIELDS (Encapsulation - private access) ==========
    private String isbn;              // Unique identifier for the book
    private String title;             // Book title
    private String author;            // Book author
    private boolean available;        // true if book can be borrowed, false if borrowed
    private String borrowedByMemberId; // ID of member who borrowed (null if available)
    private java.time.LocalDate dueDate; // When the book should be returned

    // ========== CONSTRUCTOR ==========
    /**
     * Creates a new book with validation
     * @param isbn International Standard Book Number (cannot be empty)
     * @param title Book title (cannot be empty)
     * @param author Book author (cannot be empty)
     * @throws IllegalArgumentException if any parameter is null or blank
     */
    public Book(String isbn, String title, String author) {
        // Input validation - OOP Encapsulation principle
        if (isbn == null || isbn.isBlank()) 
            throw new IllegalArgumentException("ISBN cannot be empty.");
        if (title == null || title.isBlank()) 
            throw new IllegalArgumentException("Title cannot be empty.");
        if (author == null || author.isBlank()) 
            throw new IllegalArgumentException("Author cannot be empty.");

        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.available = true;  // New books start as available
    }

    // ========== GETTERS (Read-only access) ==========
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public String getBorrowedByMemberId() { return borrowedByMemberId; }
    public java.time.LocalDate getDueDate() { return dueDate; }

    // ========== SETTERS (With validation) ==========
    public void setTitle(String title) { 
        if (title != null && !title.isBlank()) 
            this.title = title.trim(); 
    }
    
    public void setAuthor(String author) { 
        if (author != null && !author.isBlank()) 
            this.author = author.trim(); 
    }

    // ========== BUSINESS METHODS ==========
    /**
     * Marks book as borrowed by a member
     * @param memberId ID of member borrowing the book
     * @param dueDate Date when book must be returned
     * @throws IllegalStateException if book is already borrowed
     */
    public void borrow(String memberId, java.time.LocalDate dueDate) {
        if (!available) 
            throw new IllegalStateException("Book is already borrowed.");
        this.available = false;
        this.borrowedByMemberId = memberId;
        this.dueDate = dueDate;
    }

    /**
     * Marks book as returned
     */
    public void returnBook() {
        this.available = true;
        this.borrowedByMemberId = null;
        this.dueDate = null;
    }

    /**
     * String representation of the book for display
     */
    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed (due: " + dueDate + ")";
        return String.format("[%s] \"%s\" by %s — %s", isbn, title, author, status);
    }
}
// Contribution by Ramesh Parajuli - Added validation logic
