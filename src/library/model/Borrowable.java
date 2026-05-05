package library.model;

/**
 * Borrowable Interface - Defines contract for all library members
 * Demonstrates OOP Interface concept
 */
public interface Borrowable {
    
    /**
     * Returns maximum number of books this member type can borrow
     * @return int representing borrow limit
     */
    int getMaxBooks();
    
    /**
     * Returns membership type (e.g., "Standard", "Premium")
     * @return String membership type name
     */
    String getMembershipType();
}