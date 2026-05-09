package library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract LibraryMember class - Base for all member types
 * Demonstrates OOP: Abstraction, Encapsulation, Inheritance
 */
public abstract class LibraryMember implements Borrowable {

    // ========== FIELDS ==========
    private String memberId;          // Unique identifier
    private String name;              // Member's full name
    private String email;             // Member's email address
    private List<String> borrowedIsbns;    // Currently borrowed books
    private List<String> borrowingHistory; // All books ever borrowed

    // ========== CONSTRUCTOR ==========
    public LibraryMember(String memberId, String name, String email) {
        // Input validation
        if (memberId == null || memberId.isBlank()) 
            throw new IllegalArgumentException("Member ID cannot be empty.");
        if (name == null || name.isBlank()) 
            throw new IllegalArgumentException("Name cannot be empty.");
        if (email == null || !email.contains("@")) 
            throw new IllegalArgumentException("Invalid email address.");

        this.memberId = memberId.trim();
        this.name = name.trim();
        this.email = email.trim();
        this.borrowedIsbns = new ArrayList<>();
        this.borrowingHistory = new ArrayList<>();
    }

    // ========== GETTERS ==========
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    public List<String> getBorrowedIsbns() { 
        return Collections.unmodifiableList(borrowedIsbns); 
    }
    
    public List<String> getBorrowingHistory() { 
        return Collections.unmodifiableList(borrowingHistory); 
    }

    // ========== SETTERS ==========
    public void setName(String name) { 
        if (name != null && !name.isBlank()) 
            this.name = name.trim(); 
    }
    
    public void setEmail(String email) { 
        if (email != null && email.contains("@")) 
            this.email = email.trim(); 
    }

    // ========== BUSINESS METHODS ==========
    public boolean canBorrow() {
        return borrowedIsbns.size() < getMaxBooks();
    }

    public int currentBorrowCount() { 
        return borrowedIsbns.size(); 
    }

    public void addBorrowedBook(String isbn) {
        if (!canBorrow()) 
            throw new IllegalStateException("Borrow limit reached.");
        borrowedIsbns.add(isbn);
        borrowingHistory.add(isbn);
    }

    public void removeBorrowedBook(String isbn) {
        borrowedIsbns.remove(isbn);
    }

    public boolean isBorrowing(String isbn) {
        return borrowedIsbns.contains(isbn);
    }

    // ========== ABSTRACT METHODS (To be implemented by subclasses) ==========
    @Override
    public abstract int getMaxBooks();

    @Override
    public abstract String getMembershipType();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) — %s | Borrowed: %d/%d",
                memberId, name, email, getMembershipType(),
                borrowedIsbns.size(), getMaxBooks());
    }
}