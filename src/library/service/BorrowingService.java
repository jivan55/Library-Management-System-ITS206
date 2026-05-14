package library.service;

import library.model.Book;
import library.model.LibraryMember;
import library.model.WaitlistEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * BorrowingService - Handles borrowing and returning books
 * Integrates BookService, MemberService, and WaitlistService
 */
public class BorrowingService {

    private final BookService bookService;
    private final MemberService memberService;
    private final WaitlistService waitlistService;
    private static final int DEFAULT_LOAN_DAYS = 14;  // 2 weeks loan period

    public BorrowingService(BookService bookService, MemberService memberService, 
                            WaitlistService waitlistService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.waitlistService = waitlistService;
    }

    // ========== BORROW ==========
    /**
     * Borrow a book for a member
     * If book unavailable, can add to waitlist
     */
    public void borrowBook(String memberId, String isbn, boolean joinWaitlistIfUnavailable) {
        LibraryMember member = memberService.getOrThrow(memberId);
        Book book = bookService.getOrThrow(isbn);

        // Check borrow limit
        if (!member.canBorrow()) {
            throw new IllegalStateException("Member " + member.getName() 
                    + " has reached borrow limit of " + member.getMaxBooks());
        }

        // Check if already borrowing
        if (member.isBorrowing(isbn)) {
            throw new IllegalStateException("Already borrowing this book.");
        }

        // Book unavailable - handle waitlist
        if (!book.isAvailable()) {
            if (joinWaitlistIfUnavailable) {
                waitlistService.addToWaitlist(memberId, isbn);
                System.out.println("Book unavailable. Added to waitlist.");
            } else {
                System.out.println("Book unavailable. Due back: " + book.getDueDate());
            }
            return;
        }

        // Borrow the book
        LocalDate dueDate = LocalDate.now().plusDays(DEFAULT_LOAN_DAYS);
        book.borrow(memberId, dueDate);
        member.addBorrowedBook(isbn);
        System.out.println("SUCCESS: '" + book.getTitle() + "' borrowed by " 
                + member.getName() + ". Due: " + dueDate);
    }

    // ========== RETURN ==========
    /**
     * Return a borrowed book and notify next waitlist member
     */
    public void returnBook(String memberId, String isbn) {
        LibraryMember member = memberService.getOrThrow(memberId);
        Book book = bookService.getOrThrow(isbn);

        if (!member.isBorrowing(isbn)) {
            throw new IllegalStateException("Member does not have this book.");
        }

        book.returnBook();
        member.removeBorrowedBook(isbn);
        System.out.println("Book '" + book.getTitle() + "' returned.");

        // Notify next person on waitlist
        Optional<WaitlistEntry> next = waitlistService.notifyNextOnWaitlist(isbn);
        next.ifPresent(e -> System.out.println(">> Member " + e.getMemberId() 
                + " can now borrow this book."));
    }

    // ========== DISPLAY ==========
    public void displayCurrentLoans(String memberId) {
        LibraryMember member = memberService.getOrThrow(memberId);
        System.out.println("=== Current Loans for " + member.getName() + " ===");
        member.getBorrowedIsbns().forEach(isbn -> 
            bookService.findByIsbn(isbn).ifPresent(b -> 
                System.out.println("  - " + b.getTitle() + " due: " + b.getDueDate())));
    }

    public void displayBorrowingHistory(String memberId) {
        LibraryMember member = memberService.getOrThrow(memberId);
        System.out.println("=== Borrowing History for " + member.getName() + " ===");
        member.getBorrowingHistory().forEach(isbn -> 
            bookService.findByIsbn(isbn).ifPresent(b -> 
                System.out.println("  - \"" + b.getTitle() + "\"")));
    }
}