package library.service;

import library.model.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * BookService - Manages all book-related operations
 * Handles CRUD operations and search functionality
 */
public class BookService {

    private final List<Book> books = new ArrayList<>();  // In-memory storage

    // ========== CREATE (Add new book) ==========
    /**
     * Adds a new book to the catalogue
     * Prevents duplicate ISBNs
     */
    public void addBook(String isbn, String title, String author) {
        if (findByIsbn(isbn).isPresent()) {
            throw new IllegalArgumentException("Book with ISBN '" + isbn + "' already exists.");
        }
        books.add(new Book(isbn, title, author));
        System.out.println("Book added: " + title);
    }

    // ========== UPDATE ==========
    /**
     * Updates book details (title and/or author)
     */
    public void updateBook(String isbn, String newTitle, String newAuthor) {
        Book book = getOrThrow(isbn);
        if (newTitle != null && !newTitle.isBlank()) 
            book.setTitle(newTitle);
        if (newAuthor != null && !newAuthor.isBlank()) 
            book.setAuthor(newAuthor);
        System.out.println("Book updated: " + book.getTitle());
    }

    // ========== DELETE ==========
    /**
     * Removes a book from catalogue
     * Cannot remove borrowed books
     */
    public void removeBook(String isbn) {
        Book book = getOrThrow(isbn);
        if (!book.isAvailable()) {
            throw new IllegalStateException("Cannot remove a borrowed book.");
        }
        books.remove(book);
        System.out.println("Book removed: " + book.getTitle());
    }

    // ========== SEARCH/READ ==========
    public Optional<Book> findByIsbn(String isbn) {
        return books.stream()
                .filter(b -> b.getIsbn().equalsIgnoreCase(isbn))
                .findFirst();
    }

    public List<Book> searchByTitle(String keyword) {
        String kw = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String keyword) {
        String kw = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    // ========== HELPER METHOD ==========
    public Book getOrThrow(String isbn) {
        return findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + isbn));
    }
}