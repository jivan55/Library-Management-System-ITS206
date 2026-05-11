package library.service;

import library.model.WaitlistEntry;
import java.util.*;

/**
 * WaitlistService - FIFO queue management for book waitlists
 * POINT 4 EXTENSION: When book is borrowed, members join waitlist
 * When book returned, next member is notified
 */
public class WaitlistService {

    // Map: ISBN -> Queue of waiting members (FIFO)
    private final Map<String, Queue<WaitlistEntry>> waitlists = new HashMap<>();

    /**
     * Add member to waitlist for a book
     * Cannot add same member twice for same book
     */
    public void addToWaitlist(String memberId, String isbn) {
        waitlists.putIfAbsent(isbn, new LinkedList<>());
        Queue<WaitlistEntry> queue = waitlists.get(isbn);

        // Prevent duplicate entries
        boolean alreadyWaiting = queue.stream()
                .anyMatch(e -> e.getMemberId().equals(memberId));

        if (alreadyWaiting) {
            throw new IllegalStateException("Already on waitlist for this book.");
        }

        queue.add(new WaitlistEntry(memberId, isbn));
        System.out.println("Member " + memberId + " added to waitlist for ISBN " + isbn);
    }

    /**
     * Get next person on waitlist (FIFO) and remove them
     * Called when a book is returned
     */
    public Optional<WaitlistEntry> notifyNextOnWaitlist(String isbn) {
        Queue<WaitlistEntry> queue = waitlists.get(isbn);
        if (queue == null || queue.isEmpty()) return Optional.empty();

        WaitlistEntry next = queue.poll();
        System.out.println("NOTIFICATION: Book " + isbn + " available. Member " 
                + next.getMemberId() + " can borrow.");
        return Optional.of(next);
    }

    public List<WaitlistEntry> getWaitlistForBook(String isbn) {
        Queue<WaitlistEntry> queue = waitlists.get(isbn);
        if (queue == null) return Collections.emptyList();
        return new ArrayList<>(queue);
    }

    public int getQueueSize(String isbn) {
        Queue<WaitlistEntry> queue = waitlists.get(isbn);
        return (queue == null) ? 0 : queue.size();
    }

    public List<String> getBooksWaitedForByMember(String memberId) {
        return waitlists.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(w -> w.getMemberId().equals(memberId)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void removeFromWaitlist(String memberId, String isbn) {
        Queue<WaitlistEntry> queue = waitlists.get(isbn);
        if (queue != null) {
            queue.removeIf(e -> e.getMemberId().equals(memberId));
        }
    }
}