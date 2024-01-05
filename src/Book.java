import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String publisher;
    private int releaseYear;
    private String ISBN;
    private int numCopies;
    private int rating;
    private String category;
    private List<String> comments;
    private List<Integer> ratings;
    private double averageRating;

    public Book(String title, String author, String publisher, int releaseYear, String ISBN,
                int numCopies, String category) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.releaseYear = releaseYear;
        this.ISBN = ISBN;
        this.numCopies = numCopies;
        this.rating = 0;
        this.comments = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.category = category;
    }

    public double getAverageRating() {
        return averageRating;
    }

    private void updateAverageRating() {
        if (!ratings.isEmpty()) {
            int sumRatings = ratings.stream().mapToInt(Integer::intValue).sum();
            averageRating = (double) sumRatings / ratings.size();
        } else {
            averageRating = 0;
        }
    }

    public void addRating(int rating) {
        ratings.add(rating);
        updateAverageRating();
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumCopies() {
        return numCopies;
    }

    public void setNumCopies(int numCopies) {
        this.numCopies = numCopies;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
        updateAverageRating();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
