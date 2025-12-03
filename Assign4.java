import java.util.*;
import java.io.*;
class Book{
    int bookId;
    String title;
    String author;
    String category;
    boolean isIssued;
    Book(int bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = false;
    }
    void displayBookDetails(){
        System.out.println("ID: " + bookId + " | Title: " + title + " | Author: " + author +
                " | Category: " + category + " | Issued: " + (isIssued ? "Yes" : "No"));
    }
    void markAsIssued(){
        this.isIssued = true;
    }
    void markAsReturned(){
        this.isIssued = false;
    }
}
class Member{
    int memberId;
    String name;
    String email;
    List<Integer> issuedBooks = new ArrayList<>();
    Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }
    void displayMemberDetails(){
        System.out.println("ID: " + memberId + " | Name: " + name + " | Email: " + email);
        System.out.println("Books Borrowed (IDs): " + issuedBooks);
    }
    void addIssuedBook(int bookId){
        issuedBooks.add(bookId);
    }
    void returnIssuedBook(int bookId){
        issuedBooks.remove((Integer) bookId);
    }
}
class LibraryManager{
    Map<Integer, Book> books = new HashMap<>();
    Map<Integer, Member> members = new HashMap<>();
    Scanner sc = new Scanner(System.in);
    final String BOOK_FILE = "books.ser";
    final String MEM_FILE = "members.ser";

    void addBook(){
        try{
            System.out.println("Enter Book ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            if(books.containsKey(id)){
                System.out.println("Book ID already exists");
                return;
            }
            System.out.print("Enter Title: ");
            String title = sc.nextLine();
            System.out.print("Enter Author: ");
            String author = sc.nextLine();
            System.out.print("Enter Category: ");
            String category = sc.nextLine();
            Book newBook = new Book(id,title,author,category);
            books.put(id, newBook);
            saveToFile();
            System.out.println("Book added");
        }
        catch(InputMismatchException e){
            System.out.println("Error");
            sc.nextLine();
        }
    }
    void addMember(){
        try{
            System.out.println("Enter member ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            if(members.containsKey(id)){
                System.out.println("Member already exists");
            }
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            Member newMember = new Member(id, name, email);
            members.put(id, newMember);
            saveToFile();
            System.out.println("Member added successfully.");
        }
        catch(InputMismatchException e){
            System.out.println("error");
            sc.nextLine();
        }
    }
    void issueBook(){
        System.out.print("Enter Book ID to issue: ");
        int bid = sc.nextInt();
        System.out.print("Enter Member ID: ");
        int mid = sc.nextInt();

        if (books.containsKey(bid) && members.containsKey(mid)) {
            Book b = books.get(bid);
            Member m = members.get(mid);

            if (!b.isIssued) {
                b.markAsIssued();
                m.addIssuedBook(bid);
                saveToFile();
                System.out.println("Book issued.");
            } else {
                System.out.println("Book is already issued.");
            }
        } else {
            System.out.println("Invalid Book ID or Member ID.");
        }
    }
    void returnBook(){
        System.out.print("Enter Book ID to return: ");
        int bid = sc.nextInt();
        System.out.print("Enter Member ID: ");
        int mid = sc.nextInt();

        if (books.containsKey(bid) && members.containsKey(mid)) {
            Book b = books.get(bid);
            Member m = members.get(mid);

            if (b.isIssued) {
                b.markAsReturned();
                m.returnIssuedBook(bid);
                saveToFile();
                System.out.println("Book returned.");
            } else {
                System.out.println("This book was not issued.");
            }
        } else {
            System.out.println("Invalid details.");
        }
    }
    void searchBooks(){
        sc.nextLine(); // consume buffer
        System.out.print("Enter Title/Author/Category to search: ");
        String query = sc.nextLine().toLowerCase();
        boolean found = false;

        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(query) ||
                    b.author.toLowerCase().contains(query) ||
                    b.category.toLowerCase().contains(query)) {
                b.displayBookDetails();
                found = true;
            }
        }
        if (!found) System.out.println("No books found.");
    }
    void sortBooks(){
        List<Book> bookList = new ArrayList<>(books.values());
        bookList.sort((b1, b2) -> b1.title.compareToIgnoreCase(b2.title));
        System.out.println("--- Books Sorted by Title ---");
        for (Book b : bookList) {
            b.displayBookDetails();
        }
    }
    void loadFromFile(){
        try {
            File f = new File(BOOK_FILE);
            if (f.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                books = (HashMap<Integer, Book>) ois.readObject();
                ois.close();
            }
            File f2 = new File(MEM_FILE);
            if (f2.exists()) {
                ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(f2));
                members = (HashMap<Integer, Member>) ois2.readObject();
                ois2.close();
            }
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    void saveToFile(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOK_FILE));
            oos.writeObject(books);
            oos.close();

            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(MEM_FILE));
            oos2.writeObject(members);
            oos2.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}


class Assign4 {
    public static void main(String[] args) {
        LibraryManager lib = new LibraryManager();
        Scanner input = new Scanner(System.in);

        lib.loadFromFile(); // Load data at startup

        while (true) {
            System.out.println("\n1. Add Book\n2. Add Member\n3. Issue Book\n4. Return Book");
            System.out.println("5. Search Books\n6. Sort Books\n7. Exit");
            System.out.print("Choice: ");

            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    lib.addBook();
                    break;
                case 2:
                    lib.addMember();
                    break;
                case 3:
                    lib.issueBook();
                    break;
                case 4:
                    lib.returnBook();
                    break;
                case 5:
                    lib.searchBooks();
                    break;
                case 6:
                    lib.sortBooks();
                    break;
                case 7:
                    lib.saveToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}
