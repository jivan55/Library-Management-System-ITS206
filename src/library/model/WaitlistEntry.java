package library.model;

/**
 * WaitlistEntry - Represents a member waiting for a book
 * Part of Point 4 Extension (Waitlist Feature)
 */
public class WaitlistEntry {

    private final String memberId;           // Member waiting for the book
    private final String isbn;               // Book they want
    private final java.time.LocalDateTime requestedAt;  // When they joined waitlist

    public WaitlistEntry(String memberId, String isbn) {
        this.memberId = memberId;
        this.isbn = isbn;
        this.requestedAt = java.time.LocalDateTime.now();  // Current timestamp
    }

    // ========== GETTERS ==========
    public String getMemberId() { return memberId; }
    public String getIsbn() { return isbn; }
    public java.time.LocalDateTime getRequestedAt() { return requestedAt; }

    @Override
    public String toString() {
        return String.format("Member %s waiting for ISBN %s (requested: %s)",
                memberId, isbn, requestedAt.toLocalDate());
    }