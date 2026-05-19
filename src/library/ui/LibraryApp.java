package library.ui;

import library.service.BookService;
import library.service.BorrowingService;
import library.service.MemberService;
import library.service.WaitlistService;
import library.model.Book;
import library.model.WaitlistEntry;
import java.util.Scanner;
import java.util.List;

/**
 * LibraryApp - Main console user interface
 * Entry point for the Library Management System
 */
public class LibraryApp {

    // Service instances
    private static final BookService bookService = new BookService();
    private static final MemberService memberService = new MemberService();
    private static final WaitlistService waitlistService = new WaitlistService();
    private static final BorrowingService borrowingService = 
            new BorrowingService(bookService, memberService, waitlistService);
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedSampleData();  // Load sample books and members
        System.out.println("==============================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM — GROUP 5");
        System.out.println("==============================================");
        
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> bookMenu();
                case "2" -> memberMenu();
                case "3" -> borrowMenu();
                case "4" -> waitlistMenu();
                case "0" -> { System.out.println("Goodbye!"); running = false; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrowing Operations");
        System.out.println("4. Waitlist (Point 4 Extension)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    // ========== BOOK MENU ==========
    private static void bookMenu() {
        System.out.println("\n--- BOOK MANAGEMENT ---");
        System.out.println("1. Add Book");
        System.out.println("2. Update Book");
        System.out.println("3. Remove Book");
        System.out.println("4. Search by Title");
        System.out.println("5. Search by Author");
        System.out.println("6. Search by ISBN");
        System.out.println("7. List All Books");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
                case "1" -> {
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    System.out.print("Title: "); String title = scanner.nextLine().trim();
                    System.out.print("Author: "); String author = scanner.nextLine().trim();
                    bookService.addBook(isbn, title, author);
                }
                case "2" -> {
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    System.out.print("New title (Enter to skip): "); String t = scanner.nextLine().trim();
                    System.out.print("New author (Enter to skip): "); String a = scanner.nextLine().trim();
                    bookService.updateBook(isbn, t.isEmpty() ? null : t, a.isEmpty() ? null : a);
                }
                case "3" -> {
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    bookService.removeBook(isbn);
                }
                case "4" -> {
                    System.out.print("Title keyword: "); 
                    printBooks(bookService.searchByTitle(scanner.nextLine().trim()));
                }
                case "5" -> {
                    System.out.print("Author keyword: ");
                    printBooks(bookService.searchByAuthor(scanner.nextLine().trim()));
                }
                case "6" -> {
                    System.out.print("ISBN: ");
                    bookService.findByIsbn(scanner.nextLine().trim())
                        .ifPresentOrElse(System.out::println, () -> System.out.println("Not found."));
                }
                case "7" -> printBooks(bookService.getAllBooks());
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ========== MEMBER MENU ==========
    private static void memberMenu() {
        System.out.println("\n--- MEMBER MANAGEMENT ---");
        System.out.println("1. Register Member");
        System.out.println("2. Update Member");
        System.out.println("3. View Member");
        System.out.println("4. List All Members");
        System.out.println("5. View Borrowing History");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
                case "1" -> {
                    System.out.print("Name: "); String name = scanner.nextLine().trim();
                    System.out.print("Email: "); String email = scanner.nextLine().trim();
                    memberService.registerMember(name, email);
                }
                case "2" -> {
                    System.out.print("Member ID: "); String id = scanner.nextLine().trim();
                    System.out.print("New name (Enter to skip): "); String n = scanner.nextLine().trim();
                    System.out.print("New email (Enter to skip): "); String e = scanner.nextLine().trim();
                    memberService.updateMember(id, n.isEmpty() ? null : n, e.isEmpty() ? null : e);
                }
                case "3" -> {
                    System.out.print("Member ID: "); 
                    System.out.println(memberService.getOrThrow(scanner.nextLine().trim()));
                }
                case "4" -> memberService.getAllMembers().forEach(System.out::println);
                case "5" -> {
                    System.out.print("Member ID: ");
                    borrowingService.displayBorrowingHistory(scanner.nextLine().trim());
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ========== BORROW MENU ==========
    private static void borrowMenu() {
        System.out.println("\n--- BORROWING OPERATIONS ---");
        System.out.println("1. Borrow Book");
        System.out.println("2. Return Book");
        System.out.println("3. View Current Loans");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
                case "1" -> {
                    System.out.print("Member ID: "); String mid = scanner.nextLine().trim();
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    System.out.print("Join waitlist if unavailable? (y/n): ");
                    boolean wl = scanner.nextLine().trim().equalsIgnoreCase("y");
                    borrowingService.borrowBook(mid, isbn, wl);
                }
                case "2" -> {
                    System.out.print("Member ID: "); String mid = scanner.nextLine().trim();
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    borrowingService.returnBook(mid, isbn);
                }
                case "3" -> {
                    System.out.print("Member ID: ");
                    borrowingService.displayCurrentLoans(scanner.nextLine().trim());
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ========== WAITLIST MENU (Point 4 Extension) ==========
    private static void waitlistMenu() {
        System.out.println("\n--- WAITLIST MANAGEMENT (Point 4) ---");
        System.out.println("1. View Waitlist for a Book");
        System.out.println("2. Remove Myself from Waitlist");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        
        String c = scanner.nextLine().trim();
        try {
            switch (c) {
                case "1" -> {
                    System.out.print("ISBN: "); 
                    List<WaitlistEntry> wl = waitlistService.getWaitlistForBook(scanner.nextLine().trim());
                    if (wl.isEmpty()) System.out.println("No one on waitlist.");
                    else wl.forEach(e -> System.out.println("  " + e));
                }
                case "2" -> {
                    System.out.print("Member ID: "); String mid = scanner.nextLine().trim();
                    System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                    waitlistService.removeFromWaitlist(mid, isbn);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // ========== HELPERS ==========
    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) { System.out.println("No books found."); return; }
        books.forEach(System.out::println);
    }

    // ========== SAMPLE DATA ==========
    private static void seedSampleData() {
        bookService.addBook("978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee");
        bookService.addBook("978-0-7432-7356-5", "1984", "George Orwell");
        bookService.addBook("978-0-14-028329-7", "The Great Gatsby", "F. Scott Fitzgerald");
        
        memberService.registerMember("Alice Johnson", "alice@email.com");
        memberService.registerMember("Bob Smith", "bob@email.com");
        
        System.out.println("\n--- Sample data loaded ---\n");
    }
}