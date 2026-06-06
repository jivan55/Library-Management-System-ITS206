# Library Management System — Group 5
## ITS206 Software Construction and Design | NAPS

---

## Team Members
| Name | Role |
|------|------|
| Jeevan Ghimire | Team Lead — Book model, BookService, Tests T01-T02 |
| Ramesh Parajuli | Book validation, Search methods, Tests T03-T04 |
| Niranjan Thapa | Member model, StandardMember, MemberService, Borrowable interface |
| Rohit Dahal | BorrowingService, WaitlistService, LibraryApp UI |

---

## What This System Does
- Add, update, remove and search books
- Register and manage library members
- Borrow and return books (max 3 books per member)
- Waitlist feature — join a queue when a book is unavailable (Point 4 Extension)

---

## How to Run

### Requirements
- Java JDK 11 or higher
- IntelliJ IDEA (recommended)

### Steps
1. Clone or download the project
2. Open IntelliJ IDEA → **File → Open** → select the project folder
3. Wait for IntelliJ to load the project
4. Make sure the `src` folder is marked as Sources Root
   - Right-click `src` → **Mark Directory as → Sources Root**

### Run the App
1. Open `src/library/ui/LibraryApp.java`
2. Click the green **▶ Run** button
3. The menu will appear in the console — use number keys to navigate

### Run the Tests
1. Open `src/library/test/LibrarySystemTest.java`
2. Click the green **▶ Run** button
3. You should see 13 tests all passing

---

## Project Structure
```
src/
├── library/
│   ├── model/
│   │   ├── Book.java              - Book entity
│   │   ├── LibraryMember.java     - Abstract base member class
│   │   ├── StandardMember.java    - Standard member (max 3 books)
│   │   ├── Borrowable.java        - Interface for member types
│   │   └── WaitlistEntry.java     - Waitlist entry model
│   ├── service/
│   │   ├── BookService.java       - Book CRUD and search
│   │   ├── MemberService.java     - Member registration and updates
│   │   ├── BorrowingService.java  - Borrow and return logic
│   │   └── WaitlistService.java   - Waitlist queue management
│   ├── ui/
│   │   └── LibraryApp.java        - Main console interface
│   └── test/
│       └── LibrarySystemTest.java - 13 unit and integration tests
```

---

## OOP Concepts Used
- **Encapsulation** — all fields are private with getters/setters
- **Inheritance** — StandardMember extends LibraryMember
- **Abstraction** — LibraryMember is an abstract class
- **Polymorphism** — getMaxBooks() overridden per member type
- **Interface** — Borrowable interface implemented by all members
