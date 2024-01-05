import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

public class Borrowing implements Serializable {
    private User user;
    private Book book;
    private Date borrowingDate;
    private Date returnDate;

    public Borrowing(User user, Book book) {
        this.user = user;
        this.book = book;
        this.borrowingDate = new Date(); // Set the borrowing date to the current date
        this.returnDate = calculateReturnDate();
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
        this.returnDate = calculateReturnDate();
    }

    public Date getReturnDate() {
        return returnDate;
    }

    private Date calculateReturnDate() {
        // Calculate return date as 5 days after borrowing date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowingDate);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        return calendar.getTime();
    }
}
