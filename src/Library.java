import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Library implements Serializable {
    private List<Book> books;
    private List<User> users;
    private List<Admin> admins;
    private Map<Book, List<User>> lendings;
    private Set<String> uniqueCategories;
    private List<Borrowing> allBorrowings;



    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.lendings = new HashMap<>();
        this.uniqueCategories = new HashSet<>();
        this.allBorrowings = new ArrayList<>();

    }
    public void serializeUsers() {
        LibrarySerializer.saveUsers(users);
    }

    public void serializeBooks() {
        LibrarySerializer.saveBooks(books);
    }

    public void serializeBorrowings() {
        LibrarySerializer.saveBorrowings(allBorrowings);
    }
    public List<String> getBookTitles() {
        return books.stream().map(Book::getTitle).collect(Collectors.toList());
    }

    public void deserializeUsers() {
        List<User> loadedUsers = LibrarySerializer.loadUsers();
        if (loadedUsers != null) {
            users.clear();
            users.addAll(loadedUsers);
            System.out.println(loadedUsers);
        }
    }

    public void deserializeBooks() {
        List<Book> loadedBooks = LibrarySerializer.loadBooks();
        if (loadedBooks != null) {
            books.clear();
            books.addAll(loadedBooks);
            System.out.println(loadedBooks);

        }
    }

    public void deserializeBorrowings() {
        List<Borrowing> loadedBorrowings = LibrarySerializer.loadBorrowings();
        if (loadedBorrowings != null) {
            allBorrowings.clear();
            allBorrowings.addAll(loadedBorrowings);
            for (Borrowing borrowing : loadedBorrowings) {
                String user = borrowing.getUser().getUsername();
                String book = borrowing.getBook().getTitle();
                System.out.println("Loaded Borrowing - User: " + user + ", Book: " + book);
            }

        }
    }

     public List<Borrowing> getAllBorrowings () {
    	 return this.allBorrowings;
     }

    // Deserialize users from a file
    
    public void addRatingAndComment(User user, Book book, int rating, String comment) {
        if (user != null && book != null) {
            book.addRating(rating);
            book.addComment(comment);
            System.out.println("Rating and comment added successfully!");
        } else {
            System.out.println("User or book not found.");
        }
    }

    // Getters and setters
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public Map<Book, List<User>> getLendings() {
        return lendings;
    }

    public void setLendings(Map<Book, List<User>> lendings) {
        this.lendings = lendings;
    }

    public void addBook(Book book) {
        books.add(book);
    }
    public void removeBookFromBorrowings(String ISBN) {
        // Iterate through borrowings and remove entries involving the book with the given ISBN
        allBorrowings.removeIf(borrowing -> borrowing.getBook().getISBN().equals(ISBN));
    }
    
    public void removeBooksInCategoryFromBorrowings(String category) {
        // Iterate through borrowings and remove entries involving books in the given category
        allBorrowings.removeIf(borrowing -> borrowing.getBook().getCategory().equals(category));
    }
    //ADDING REMOVING AND EDITING BOOKS
    public void addBookByAdmin(Admin admin, String title, String author, String publisher, int releaseYear, String ISBN, int numCopies, String category) {
        Book newBook = new Book(title, author, publisher, releaseYear, ISBN, numCopies, category);
        addBook(newBook); // Utilize the existing addBook method
        System.out.println("Book added successfully!");
    }
    public void deleteBookByAdmin(Admin admin, String ISBN) {
        Book bookToDelete = null;

        // Find the book to delete
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                bookToDelete = book;
                break;
            }
        }

        if (bookToDelete != null) {
            // Remove the book from each user's borrowings
            for (User user : users) {
                List<Borrowing> userBorrowings = user.getBorrowings();
                List<Borrowing> borrowingsToRemove = new ArrayList<>();

                // Check if the book is in the user's borrowings
                for (Borrowing borrowing : userBorrowings) {
                    if (borrowing.getBook().equals(bookToDelete)) {
                        borrowingsToRemove.add(borrowing);
                    }
                }

                // Remove the book from the user's borrowings
                userBorrowings.removeAll(borrowingsToRemove);
            }

            // Remove the book from the library's list of books
            books.remove(bookToDelete);

            System.out.println("Book with ISBN " + ISBN + " deleted successfully.");

            // Print the updated information
            printLibraryInformation();
        } else {
            System.out.println("Book with ISBN " + ISBN + " not found.");
        }
    }

    // Helper method to print library information for debugging
    private void printLibraryInformation() {
        System.out.println("Current Library Information:");
        System.out.println("Total Books: " + books.size());
        System.out.println("Total Users: " + users.size());

        System.out.println("\nBooks:");
        for (Book book : books) {
            System.out.println(book);
        }

        System.out.println("\nUsers:");
        for (User user : users) {
            System.out.println(user);
        }

        System.out.println("\nBorrowings:");
        for (User user : users) {
            for (Borrowing borrowing : user.getBorrowings()) {
                System.out.println(borrowing);
            }
        }
    }

    public void editBookByAdmin(Admin admin, String ISBN, String newTitle, String newAuthor, String newPublisher, int newReleaseYear, int newNumCopies, String newCategory) {
        Book bookToEdit = null;
    
        // Find the book with the given ISBN
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                bookToEdit = book;
                break;
            }
        }
    
        // Edit the book if found
        if (bookToEdit != null) {
            // Update the book details
            bookToEdit.setTitle(newTitle);
            bookToEdit.setAuthor(newAuthor);
            bookToEdit.setPublisher(newPublisher);
            bookToEdit.setReleaseYear(newReleaseYear);
            bookToEdit.setNumCopies(newNumCopies);
            bookToEdit.setCategory(newCategory);
    
            System.out.println("Book with ISBN " + ISBN + " edited successfully.");
        } else {
            System.out.println("Book with ISBN " + ISBN + " not found.");
        }
    }
    

    //ADDING REMOVING AND EDITING CATEGORIES
    public void addCategory(String newCategory) {
        // Add the new category for all books
//        for (Book book : books) {
//            book.setCategory(newCategory);
            uniqueCategories.add(newCategory);
//        }
        System.out.println("Category added successfully.");
//        System.out.println(uniqueCategories);

    }
    

    public void removeCategoryAndBooks(String categoryToRemove) {
        // Remove all books in the specified category
        books.removeIf(book -> {
            if (book.getCategory().equals(categoryToRemove)) {
                uniqueCategories.remove(categoryToRemove);
                return true;
            }
            return false;
        });
        System.out.println("Category and associated books removed successfully.");
    }

    Set<String> getAllUniqueCategories() {
        Set<String> categories = new HashSet<>();
        for (Book book : books) {
            categories.add(book.getCategory());
        }
        return categories;
    }

public void printAllCategories() {
    System.out.println("List of all categories:");
    for (String category : getAllUniqueCategories()) {
        System.out.println(category);
    }
}
    public void UpdateCategory(String oldCategory, String newCategory) {
        // Add or update the category for all books
        for (Book book : books) {
            if (oldCategory == null || book.getCategory().equals(oldCategory)) {
                book.setCategory(newCategory);
            }
        }
        System.out.println("Category updated successfully.");
    }

    
    
    
    public void borrowBook(User user, Book book) {
        // Check if the book is available for borrowing
    	  if (canUserBorrowMoreBooks(user)) {
        if (book.getNumCopies() > 0) {
            book.setNumCopies(book.getNumCopies() - 1);
            Borrowing borrowing = new Borrowing(user, book);
            user.getBorrowings().add(borrowing);
            allBorrowings.add(borrowing);
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Sorry, no copies available for borrowing.");
        }
    } else {
        System.out.println("User has reached the maximum limit of borrowed books.");
    }
}
    private boolean canUserBorrowMoreBooks(User user) {
        // Check if the user has less than 2 borrowed books
        return getUserBorrowedBooksCount(user) < 2;
    }
    private int getUserBorrowedBooksCount(User user) {
        // Count the number of books borrowed by the user
        int count = 0;
        for (Borrowing borrowing : allBorrowings) {
            if (borrowing.getUser().getUsername().equals(user.getUsername())) {
                count++;
            }
        }
        return count;
    }
    public List<Book> getTopRatedBooks(int count) {
        return books.stream()
                .sorted((b1, b2) -> Double.compare(b2.getAverageRating(), b1.getAverageRating()))
                .limit(count)
                .collect(Collectors.toList());
    }
    public void viewBorrowedBooks(String username) {
        System.out.println("Borrowed Books for User " + username + ":");
        for (Borrowing borrowing : allBorrowings) {
            if (borrowing.getUser().getUsername().equals(username)) {
                System.out.println("Book: " + borrowing.getBook().getTitle() +
                        ", Borrowing Date: " + borrowing.getBorrowingDate());
            }
        }
    }

    public void viewAllBorrowings() {
        System.out.println("All Borrowings:");
        for (Borrowing borrowing : allBorrowings) {
            System.out.println("User: " + borrowing.getUser().getUsername() +
                               ", Book: " + borrowing.getBook().getTitle() +
                               ", Borrowing Date: " + borrowing.getBorrowingDate());
        }
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addSampleBooksAndRatings() {
        String[] sampleComments = {
            "A captivating read!",
            "Well-written and thought-provoking.",
            "Highly recommended.",
            "Couldn't put it down!",
            "Great characters and plot.",
            "An absolute classic.",
            "Interesting and informative.",
            "Page-turner from start to finish."
            // Add more meaningful comments as needed
        };
    
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Scribner", 1925, "1", 5, "Fiction");
        addRatingsAndComments(book1, sampleComments, 5);
    
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "J.B. Lippincott & Co.", 1960, "2", 3, "Classics");
        addRatingsAndComments(book2, sampleComments, 3);
    
        Book book3 = new Book("1984", "George Orwell", "Secker & Warburg", 1949, "3", 4, "Dystopian");
        addRatingsAndComments(book3, sampleComments, 4);
    
        Book book4 = new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Bloomsbury", 1997, "4", 8, "Fantasy");
        addRatingsAndComments(book4, sampleComments, 3);
    
        Book book5 = new Book("The Catcher in the Rye", "J.D. Salinger", "Little, Brown and Company", 1951, "5", 6, "Classics");
        addRatingsAndComments(book5, sampleComments, 4);
    
        Book book6 = new Book("The Hobbit", "J.R.R. Tolkien", "Allen & Unwin", 1937, "6", 7, "Fantasy");
        addRatingsAndComments(book6, sampleComments, 5);
    }
    
    private void addRatingsAndComments(Book book, String[] sampleComments, int numRatingsAndComments) {
        Random random = new Random();
    
        for (int i = 0; i < numRatingsAndComments; i++) {
            int randomRating = random.nextInt(5) + 1;  // Random rating between 1 and 5
            String randomComment = sampleComments[random.nextInt(sampleComments.length)];
    
            book.addRating(randomRating);
            book.addComment(randomComment);
        }
    
        // Assuming there is a method to add the book to the library (similar to addBook)
        addBook(book);
    }
    public void addCommentAndRating(User user, Book book, String comment, int rating) {
        if (user != null) {
            // Check if the user is currently borrowing the specified book
            boolean isCurrentlyBorrowing = getAllBorrowings().stream()
                    .anyMatch(borrowing -> borrowing.getUser().getUsername().equals(user.getUsername()) && borrowing.getBook().getTitle().equals(book.getTitle()));

            if (isCurrentlyBorrowing) {
                user.addCommentAndRating(book, comment, rating);
                System.out.println("Rating and comment added successfully!");
            } else {
                System.out.println("You can only add ratings and comments for books you are currently borrowing.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
    public List<Book> searchBooks(String title, String author, String releaseYear) {
        List<Book> results = new ArrayList<>();
    
        for (Book book : books) {
            boolean matchTitle = title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase());
            boolean matchAuthor = author.isEmpty() || book.getAuthor().toLowerCase().contains(author.toLowerCase());
            boolean matchReleaseYear = releaseYear.isEmpty() || String.valueOf(book.getReleaseYear()).equals(releaseYear);
    
            if (matchTitle && matchAuthor && matchReleaseYear) {
                results.add(book);
            }
        }
    
        return results;
    }
    public String getAllBooksInfo(LibraryUser user) {
        StringBuilder result = new StringBuilder();
        result.append("List of Books:\n");
    
        for (Book book : books) {
            result.append("Title: ").append(book.getTitle()).append("\n");
            result.append("Author: ").append(book.getAuthor()).append("\n");
            result.append("Publisher: ").append(book.getPublisher()).append("\n");
            result.append("Release Year: ").append(book.getReleaseYear()).append("\n");
            result.append("ISBN: ").append(book.getISBN()).append("\n");
            result.append("Number of Copies: ").append(book.getNumCopies()).append("\n");
            result.append("Average Rating: ").append(book.getAverageRating()).append("\n");
            result.append("Category: ").append(book.getCategory()).append("\n");
            result.append("Comments: ").append(String.join(", ", book.getComments())).append("\n\n");
        }
    
        return result.toString();
    }
    
    public void addRandomUsers(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            String username = "user" + (i + 1);
            String password = "password" + (i + 1);
            String name = "Name" + (i + 1);
            String surname = "Surname" + (i + 1);
            String adt = "ADT" + (i + 1);
            String email = "user" + (i + 1) + "@example.com";
    
            // Check if the generated username already exists
            while (usernameExists(username)) {
                i++;
                username = "user" + (i + 1);
            }
    
            // Create a new user and add it to the library
            User newUser = new User(username, password, name, surname, adt, email);
            addUser(newUser);
            System.out.println("Username: " + username + ", Password: " + password +
                    ", Name: " + name + ", Surname: " + surname + ", ADT: " + adt +
                    ", Email: " + email);
        }
    
        System.out.println("Random users added successfully!");
    }
    
    private boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }
    public void addSpecificBorrowings() {
        // Assuming you have users and books already added
    
        // User 1 borrowing Book 1
        borrowBook(users.get(0), books.get(0));
    
        // User 2 borrowing Book 2
        borrowBook(users.get(1), books.get(1));
    
        // User 3 borrowing Book 3
        borrowBook(users.get(2), books.get(2));
    
        // User 1 borrowing Book 2
        borrowBook(users.get(0), books.get(1));
    
        // User 2 borrowing Book 3
        borrowBook(users.get(1), books.get(2));
    
        // Displaying all borrowings after specific borrowings
        viewAllBorrowings();
    }

    public void removeBorrowing(Borrowing borrowing) {
        // Implement the logic to remove a borrowing from the library
        // This could involve removing it from a list of borrowings, etc.
        // Make sure to update your data structures accordingly.

        // For example, if you have a list of all borrowings in the library:
        allBorrowings.remove(borrowing);

        // If you have a map of borrowings by user, you may need to remove it from there as well:
//        borrowingsByUser.get(borrowing.getUser()).remove(bor rowing);
    }

    public void deleteUser(Admin admin, String username) {
        User userToDelete = getUserByUsername(username);

        if (userToDelete != null) {
            // Remove the user
            users.remove(userToDelete);

            // Mark all books borrowed by this user as returned
            for (Borrowing borrowing : allBorrowings) {
                if (borrowing.getUser().equals(userToDelete)) {
                    Book borrowedBook = borrowing.getBook();
                    borrowedBook.setNumCopies(borrowedBook.getNumCopies() + 1);
                }
            }

            // Remove all borrowings associated with this user
            allBorrowings.removeIf(borrowing -> borrowing.getUser().equals(userToDelete));

            System.out.println("User " + username + " deleted successfully.");
        } else {
            System.out.println("User " + username + " not found.");
        }
    }
    public Borrowing terminateBorrowingByAdmin(Admin admin, String username, String ISBN) {
        // Check if the admin has viewing privileges
        if (admin.hasViewingPrivileges()) {
            // Find the user by username
            User user = getUserByUsername(username);

            // Find the book by ISBN
            Book book = findBookByISBN(ISBN);

            if (user != null && book != null) {
                // Check if the book is currently borrowed by the user
                if (isBookBorrowed(user, book)) {
                    // Remove the borrowing entry
                    Borrowing borrowingToRemove = findBorrowing(user, book);
                    allBorrowings.remove(borrowingToRemove);
                    List <Borrowing> BorrowingsNew =  user.getBorrowings();
                    
                    boolean truee = BorrowingsNew.remove(borrowingToRemove);
                    System.out.println(user.getBorrowings().remove(borrowingToRemove));
                    user.setBorrowings(BorrowingsNew);

                    // Update the book's available copies
                    book.setNumCopies(book.getNumCopies() + 1);

                    System.out.println("Borrowing terminated successfully by admin.");
                    return borrowingToRemove; // Return the terminated borrowing
                } else {
                    System.out.println("User " + username + " is not currently borrowing the book with ISBN " + ISBN + ".");
                }
            } else {
                System.out.println("User or book not found.");
            }
        } else {
            System.out.println("Admin does not have sufficient privileges to terminate borrowings.");
        }

        return null; // Return null if termination fails
    }

    
    // Helper method to check if a book is currently borrowed by a user
    private boolean isBookBorrowed(User user, Book book) {
        for (Borrowing borrowing : allBorrowings) {
            if (borrowing.getUser().equals(user) && borrowing.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to find a borrowing entry for a specific user and book
    private Borrowing findBorrowing(User user, Book book) {
        for (Borrowing borrowing : allBorrowings) {
            if (borrowing.getUser().equals(user) && borrowing.getBook().equals(book)) {
                return borrowing;
            }
        }
        return null;
    }
    
        public Book findBookByISBN(String ISBN) {
            for (Book book : books) {
                if (book.getISBN().equals(ISBN)) {
                    return book;
                }
            }
            return null; // Book not found
        }
       
        
        // Add the findBorrowingByUserAndBookISBN method in the Library class
        // Inside the Library class
        public void editUserCredentialsByAdmin(Admin admin, String targetUsername, String newUsername, String newPassword, String newName, String newSurname, String newAdt, String newEmail) {
            // Check if the admin has privileges to edit user credentials
           
                User targetUser = getUserByUsername(targetUsername);
    
                if (targetUser != null) {
                    // Update user details
                    targetUser.setUsername(newUsername);
                    targetUser.setPassword(newPassword);
                    targetUser.setName(newName);
                    targetUser.setSurname(newSurname);
                    targetUser.setAdt(newAdt);
                    targetUser.setEmail(newEmail);
    
                    System.out.println("User credentials updated successfully by admin.");
                } else {
                    System.out.println("User not found.");
                }
           
            
        }
       
        private Borrowing findBorrowingByUserAndBookISBN(User user, String bookISBN) {
            for (Borrowing borrowing : allBorrowings) {
                if (borrowing.getUser().equals(user) && borrowing.getBook().getISBN().equals(bookISBN)) {
                    return borrowing;
                }
            }
            return null;
        }
        
    
    
    }
    // Additional methods for library actions
