package library.service;

import library.model.LibraryMember;
import library.model.StandardMember;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MemberService - Manages library members
 * Handles registration, updates, and member lookups
 */
public class MemberService {

    private final List<LibraryMember> members = new ArrayList<>();
    private int idCounter = 1000;  // Auto-increment ID counter

    // ========== REGISTER ==========
    /**
     * Registers a new standard member
     * Auto-generates member ID: M1001, M1002, etc.
     */
    public String registerMember(String name, String email) {
        if (findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered.");
        }
        String memberId = "M" + (++idCounter);
        members.add(new StandardMember(memberId, name, email));
        System.out.println("Member registered: " + name + " (" + memberId + ")");
        return memberId;
    }

    // ========== UPDATE ==========
    public void updateMember(String memberId, String newName, String newEmail) {
        LibraryMember member = getOrThrow(memberId);
        if (newName != null && !newName.isBlank()) 
            member.setName(newName);
        if (newEmail != null && !newEmail.isBlank()) 
            member.setEmail(newEmail);
        System.out.println("Member updated: " + member.getName());
    }

    // ========== FIND ==========
    public Optional<LibraryMember> findById(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equalsIgnoreCase(memberId))
                .findFirst();
    }

    public Optional<LibraryMember> findByEmail(String email) {
        return members.stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<LibraryMember> getAllMembers() {
        return Collections.unmodifiableList(members);
    }

    public LibraryMember getOrThrow(String memberId) {
        return findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
    }
}