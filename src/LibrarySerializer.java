import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibrarySerializer {
    private static final String FILE_NAME = "medialab/books.ser";
    private static final String USERS_FILE = "medialab/users.ser";
    private static final String BORROWINGS_FILE = "medialab/borrowings.ser";
    private static final Logger LOGGER = Logger.getLogger(LibrarySerializer.class.getName());

    public static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "No previous users data found. Starting with an empty user list.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading users: " + e.getMessage(), e);
        }
        return null;
    }

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving users: " + e.getMessage(), e);
        }
    }

    public static void saveBooks(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
            LOGGER.log(Level.INFO, "Books saved successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving books: " + e.getMessage(), e);
        }
    }

    public static List<Borrowing> loadBorrowings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BORROWINGS_FILE))) {
            return (List<Borrowing>) ois.readObject();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "No previous borrowings data found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading borrowings: " + e.getMessage(), e);
        }
        return null;
    }

    public static void saveBorrowings(List<Borrowing> borrowings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWINGS_FILE))) {
            oos.writeObject(borrowings);
            LOGGER.log(Level.INFO, "Borrowings saved successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving borrowings: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Book> loadBooks() {
        List<Book> books = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (List<Book>) ois.readObject();
            LOGGER.log(Level.INFO, "Books loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading books: " + e.getMessage(), e);
        }
        return books;
    }
}
