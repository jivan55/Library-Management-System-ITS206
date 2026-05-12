package library.model;

/**
 * StandardMember - Concrete implementation of LibraryMember
 * Can borrow up to 3 books at a time
 * Demonstrates OOP Inheritance
 */
public class StandardMember extends LibraryMember {

    private static final int MAX_BOOKS = 3;  // Borrow limit for standard members

    public StandardMember(String memberId, String name, String email) {
        super(memberId, name, email);  // Call parent constructor
    }

    @Override
    public int getMaxBooks() { 
        return MAX_BOOKS; 
    }

    @Override
    public String getMembershipType() { 
        return "Standard"; 
    }
}