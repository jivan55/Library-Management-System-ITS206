package library.test;

import library.model.*;
import library.service.*;

/**
 * LibrarySystemTest - Complete test suite for LMS
 * 13 test cases covering all functionality
 */
public class LibrarySystemTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  LMS UNIT & INTEGRATION TESTS — GROUP 5");
        System.out.println("========================================\n");

        // Book Tests (T01-T05)
        testBookCreationValid();
        testBookCreationInvalidIsbn();
        testBookAddAndSearch();
        testDuplicateIsbnRejected();
        testRemoveUnavailableBook();

        // Member Tests (T06-T07)
        testStandardMemberBorrowLimit();
        testMemberRegistrationDuplicateEmail();

        // Borrowing Tests (T08-T10)
        testBorrowSuccess();
        testBorrowUnavailableBook();
        testReturnBook();

        // Waitlist Tests (T11-T12)
        testWaitlistAddAndNotify();
        testWaitlistNoDuplicateEntry();

        // Integration Test (T13)
        testMemberHistory();

        System.out.println("\n========================================");
        System.out.printf("  RESULTS: %d PASSED, %d FAILED%n", passed, failed);
        System.out.println("========================================");
    }

    // ========== T01-T05: BOOK TESTS ==========
    
    static void testBookCreationValid() {
        try {
            Book b = new Book("ISBN-001", "Clean Code", "Robert Martin");
            assertTrue("Book title correct", "Clean Code".equals(b.getTitle()));
            assertTrue("Book author correct", "Robert Martin".equals(b.getAuthor()));
            assertTrue("Book available", b.isAvailable());
            pass("T01: Valid book creation");
        } catch (Exception e) { fail("T01: " + e.getMessage()); }
    }

    static void testBookCreationInvalidIsbn() {
        try {
            new Book("", "Title", "Author");
            fail("T02: Should reject empty ISBN");
        } catch (IllegalArgumentException e) {
            pass("T02: Empty ISBN rejected");
        }
    }

    static void testBookAddAndSearch() {
        BookService bs = new BookService();
        bs.addBook("ISBN-010", "Design Patterns", "GoF");
        var results = bs.searchByTitle("Design");
        assertTrue("T03: Search finds book", !results.isEmpty());
        pass("T03: Book add and search works");
    }

    static void testDuplicateIsbnRejected() {
        BookService bs = new BookService();
        bs.addBook("ISBN-020", "Book A", "Author A");
        try {
            bs.addBook("ISBN-020", "Book B", "Author B");
            fail("T04: Duplicate ISBN should be rejected");
        } catch (IllegalArgumentException e) {
            pass("T04: Duplicate ISBN rejected");
        }
    }

    static void testRemoveUnavailableBook() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        bs.addBook("ISBN-030", "Unavail Book", "Author");
        String mid = ms.registerMember("Test User", "test@example.com");
        brs.borrowBook(mid, "ISBN-030", false);

        try {
            bs.removeBook("ISBN-030");
            fail("T05: Cannot remove borrowed book");
        } catch (IllegalStateException e) {
            pass("T05: Borrowed book protected");
        }
    }

    // ========== T06-T07: MEMBER TESTS ==========

    static void testStandardMemberBorrowLimit() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        for (int i = 1; i <= 4; i++) bs.addBook("B" + i, "Book " + i, "Author");
        String mid = ms.registerMember("Limit Test", "limit@example.com");
        
        for (int i = 1; i <= 3; i++) brs.borrowBook(mid, "B" + i, false);
        
        try {
            brs.borrowBook(mid, "B4", false);
            fail("T06: 4th borrow should be rejected");
        } catch (IllegalStateException e) {
            pass("T06: Borrow limit (3 books) enforced");
        }
    }

    static void testMemberRegistrationDuplicateEmail() {
        MemberService ms = new MemberService();
        ms.registerMember("User One", "dup@example.com");
        try {
            ms.registerMember("User Two", "dup@example.com");
            fail("T07: Duplicate email rejected");
        } catch (IllegalArgumentException e) {
            pass("T07: Duplicate email prevented");
        }
    }

    // ========== T08-T10: BORROWING TESTS ==========

    static void testBorrowSuccess() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        bs.addBook("BS1", "Borrowed Book", "Author");
        String mid = ms.registerMember("Borrower", "borrower@email.com");
        brs.borrowBook(mid, "BS1", false);

        Book book = bs.getOrThrow("BS1");
        assertTrue("T08: Book marked borrowed", !book.isAvailable());
        pass("T08: Borrow successful");
    }

    static void testBorrowUnavailableBook() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        bs.addBook("UB1", "Unavailable", "Author");
        String mid1 = ms.registerMember("First", "first@email.com");
        String mid2 = ms.registerMember("Second", "second@email.com");

        brs.borrowBook(mid1, "UB1", false);
        brs.borrowBook(mid2, "UB1", true);
        
        assertTrue("T09: Waitlist size = 1", ws.getQueueSize("UB1") == 1);
        pass("T09: Waitlist works");
    }

    static void testReturnBook() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        bs.addBook("RB1", "Return Me", "Author");
        String mid = ms.registerMember("Returner", "returner@email.com");
        brs.borrowBook(mid, "RB1", false);
        brs.returnBook(mid, "RB1");

        assertTrue("T10: Book available after return", bs.getOrThrow("RB1").isAvailable());
        pass("T10: Return successful");
    }

    // ========== T11-T12: WAITLIST TESTS ==========

    static void testWaitlistAddAndNotify() {
        WaitlistService ws = new WaitlistService();
        ws.addToWaitlist("M001", "ISBN-WL1");
        ws.addToWaitlist("M002", "ISBN-WL1");

        assertTrue("T11: Queue size = 2", ws.getQueueSize("ISBN-WL1") == 2);

        var next = ws.notifyNextOnWaitlist("ISBN-WL1");
        assertTrue("T11: FIFO order - M001 first", next.get().getMemberId().equals("M001"));
        pass("T11: Waitlist FIFO works");
    }

    static void testWaitlistNoDuplicateEntry() {
        WaitlistService ws = new WaitlistService();
        ws.addToWaitlist("M001", "ISBN-WL2");
        try {
            ws.addToWaitlist("M001", "ISBN-WL2");
            fail("T12: Duplicate waitlist entry rejected");
        } catch (IllegalStateException e) {
            pass("T12: No duplicate waitlist entries");
        }
    }

    // ========== T13: INTEGRATION TEST ==========

    static void testMemberHistory() {
        BookService bs = new BookService();
        MemberService ms = new MemberService();
        WaitlistService ws = new WaitlistService();
        BorrowingService brs = new BorrowingService(bs, ms, ws);

        bs.addBook("H1", "History Book", "Author");
        String mid = ms.registerMember("Historian", "hist@email.com");
        brs.borrowBook(mid, "H1", false);
        brs.returnBook(mid, "H1");

        LibraryMember member = ms.getOrThrow(mid);
        assertTrue("T13: History contains book", member.getBorrowingHistory().contains("H1"));
        pass("T13: Borrowing history tracked correctly");
    }

    // ========== ASSERTION HELPERS ==========
    static void assertTrue(String msg, boolean condition) {
        if (!condition) fail(msg);
    }

    static void pass(String name) {
        System.out.println("  ✓ PASS: " + name);
        passed++;
    }

    static void fail(String name) {
        System.out.println("  ✗ FAIL: " + name);
        failed++;
    }
}
    // T03-T04: Test methods added by Ramesh Parajuli
