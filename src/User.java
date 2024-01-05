import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements LibraryUser, Serializable {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String adt;
    private String email;
    
    public boolean login(String username, String password) {
        // Implementation for admin login
        return false;
    }
    public User(String username, String password, String name, String surname, String adt, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.adt = adt;
        this.email = email;
        // this.borrowedBooks = new/ ArrayList<>();
        this.borrowings = new ArrayList<>();

    }
    public void serializeUser(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // Deserialization method
    public static User deserializeUser(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (User) in.readObject();
        }
    }
    public void addCommentAndRating(Book book, String comment, int rating) {
        if (book != null) {
            book.addComment(comment);
            book.addRating(rating);
            System.out.println("Comment and rating added successfully!");
        } else {
            System.out.println("Book not found.");
        }
    }
    // Getters and setters
    public String getUsername() {
        return username;
    }

    public boolean hasBorrowedBook(Book book) {
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
    public boolean hasViewingPrivileges() {
        return false;
    }
    private List<Borrowing> borrowings;

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void borrowBook(Book book) {
        if (book.getNumCopies() > 0) {
            book.setNumCopies(book.getNumCopies() - 1);
            Borrowing borrowing = new Borrowing(this, book);
            borrowings.add(borrowing);
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Sorry, no copies available for borrowing.");
        }
    }

    public void viewBorrowedBooks() {
        System.out.println("Borrowed Books:");
        System.out.println("Number of Borrowings: " + borrowings.size()); // Add this line
        for (Borrowing borrowing : borrowings) {
            System.out.println("Book: " + borrowing.getBook().getTitle() +
                    ", Borrowing Date: " + borrowing.getBorrowingDate());
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    // Setter for surname
    public void setSurname(String surname) {
        this.surname = surname;
    }

    // Setter for ADT
    public void setAdt(String adt) {
        this.adt = adt;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    // Getter for surname
    public String getSurname() {
        return surname;
    }

    // Getter for ADT
    public String getAdt() {
        return adt;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }
    
}
