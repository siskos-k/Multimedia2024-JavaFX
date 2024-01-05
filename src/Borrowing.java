import java.io.Serializable;
import java.util.Date;

public class Borrowing implements Serializable {
    private User user;
    private Book book;
    private Date borrowingDate;

    public Borrowing(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowingDate = new Date(); // Set the borrowing date to the current date
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }
}
