import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UserActionsScreen extends Application {

    private User user;
    private Library library;

    private ListView<String> borrowedBooksListView;
    private TextArea searchResultsTextArea;
    private VBox root;  // Add this line

    public UserActionsScreen(User user, Library library) {
        this.user = user;
        this.library = library;
    }

    @Override
    public void start(Stage primaryStage) {
    	 primaryStage.setTitle("User Actions");

         root = new VBox(10);  // Update this line
         root.setPadding(new Insets(10));
         Label welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
         root.getChildren().add(welcomeLabel);

         Label titleLabel = new Label("Borrowed Books:");
         borrowedBooksListView = new ListView<>();
         updateBorrowedBooksListView();

         Button borrowButton = new Button("Borrow a Book");
         borrowButton.setOnAction(e -> borrowBook());

         Button addRatingButton = new Button("Add Rating and Comment");
         addRatingButton.setOnAction(e -> addRatingAndComment());

         Button searchButton = new Button("Search Books");
         searchButton.setOnAction(e -> searchBooks());

         searchResultsTextArea = new TextArea();
         searchResultsTextArea.setEditable(false);
         searchResultsTextArea.setWrapText(true);
         searchResultsTextArea.setFont(Font.font("Arial", 12));

         Button exitButton = new Button("Exit User Actions");
         exitButton.setOnAction(e -> {
             primaryStage.close();
             new LibraryManagementSystem().start(new Stage());
         });
         root.getChildren().addAll(titleLabel, borrowedBooksListView, borrowButton, addRatingButton, searchButton,
                 exitButton);

         Scene scene = new Scene(root, 400, 400);
         primaryStage.setScene(scene);
         primaryStage.show();
         

     }

    private void updateBorrowedBooksListView() {
//        library.deserializeUsers();
//        library.deserializeBooks();
//        library.deserializeBorrowings();
        
        System.out.println("Current username is: ");
        System.out.println("updateBorrowedBooksListView called" + library.getAllBorrowings());
        for(Borrowing borrowing : library.getAllBorrowings()) {
       	 System.out.println("User: " + borrowing.getUser().getUsername() + " has book: "+  borrowing.getBook().getTitle());
       }
        ObservableList<String> borrowedBooks = FXCollections.observableArrayList();

        // Create a list to store borrowings to be removed
        List<Borrowing> borrowingsToRemove = new ArrayList<>();

        // Iterate over all borrowings in the library
        
       	 for (Borrowing borrowing : library.getAllBorrowings()) {
            // Check if the borrowing belongs to the current user
            if (borrowing.getUser().getAdt().equals(user.getAdt())) {
                Book borrowedBook = borrowing.getBook();

                // Check if the borrowed book title is in the library
                if (borrowedBook != null && isBookTitleInLibrary(borrowedBook.getTitle())) {
                    // Book title is in the library, include it in the borrowed books list
                    String bookInfo = String.format("Title: %s, \nMax Return Date: %s",
                            borrowedBook.getTitle(), borrowing.getReturnDate());
                    borrowedBooks.add(bookInfo);
                } else {
                    // Book is not in the library, add the borrowing to the removal list
                    borrowingsToRemove.add(borrowing);
                }
            }
        }

        // Remove borrowings outside the loop
        library.getAllBorrowings().removeAll(borrowingsToRemove);

        borrowedBooksListView.setItems(borrowedBooks);

        // Debug print statement
        System.out.println("Borrowed Books: " + borrowedBooks);
    }

    private boolean isBookTitleInLibrary(String bookTitle) {
        for (Book libraryBook : library.getBooks()) {
            if (libraryBook.getTitle().equals(bookTitle)) {
                return true;
            }
        }
        return false;
    }




    private void borrowBook() {
        int maxBooksAllowed = 2;

        long userBorrowingsCount = library.getAllBorrowings().stream()
                .filter(borrowing -> borrowing.getUser().getAdt().equals(user.getAdt()))
                .count();

        if (userBorrowingsCount >= maxBooksAllowed) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("You can only borrow up to " + maxBooksAllowed + " books concurrently.");
            errorAlert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Borrow a Book");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the ISBN of the book to borrow:");

        // Traditional way to get the response value.
        String bookISBNToBorrow = dialog.showAndWait().orElse("");

        Book bookToBorrow = library.findBookByISBN(bookISBNToBorrow);

        if (bookToBorrow != null) {
            library.borrowBook(user, bookToBorrow);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Borrow Book");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Book borrowed successfully!");
            
            successAlert.showAndWait();

            // Update the Borrowed Books ListView
            updateBorrowedBooksListView();
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Book with ISBN " + bookISBNToBorrow + " not found.");
            errorAlert.showAndWait();
        }
    }

    
    private void addRatingAndComment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Rating and Comment");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the ISBN of the book to rate and comment:");

        // Traditional way to get the response value.
        String bookISBNToRate = dialog.showAndWait().orElse("");

        Book bookToRate = library.findBookByISBN(bookISBNToRate);

        if (bookToRate != null) {
            // Check if the user is currently borrowing the specified book
            boolean isCurrentlyBorrowing = library.getAllBorrowings().stream()
                    .anyMatch(borrowing -> borrowing.getUser().getAdt().equals(user.getAdt()) && borrowing.getBook().getTitle().equals(bookToRate.getTitle()));

            if (isCurrentlyBorrowing) {
                // Ask the user for a rating
                int rating = getRatingFromUser();

                // Check if the rating is within the valid range (1-5)
                if (isValidRating(rating)) {
                    // Ask the user for a comment
                    TextInputDialog commentDialog = new TextInputDialog();
                    commentDialog.setTitle("Add Comment");
                    commentDialog.setHeaderText(null);
                    commentDialog.setContentText("Enter your comment for the book:");

                    String comment = commentDialog.showAndWait().orElse("");

                    // Add the rating and comment to the book
                    library.addCommentAndRating(user, bookToRate, comment, rating);
                    library.serializeUsers();
                    library.serializeBooks();
                    library.serializeBorrowings();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Add Rating and Comment");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Rating and comment added successfully!");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Invalid rating. Please enter a number between 1 and 5.");
                    errorAlert.showAndWait();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("You can only add ratings and comments for books you are currently borrowing.");
                errorAlert.showAndWait();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Book with ISBN " + bookISBNToRate + " not found.");
            errorAlert.showAndWait();
        }
    }

    private int getRatingFromUser() {
        while (true) {
            TextInputDialog ratingDialog = new TextInputDialog();
            ratingDialog.setTitle("Add Rating");
            ratingDialog.setHeaderText(null);
            ratingDialog.setContentText("Enter your rating (1-5) for the book:");

            try {
                int rating = Integer.parseInt(ratingDialog.showAndWait().orElse(""));
                if (isValidRating(rating)) {
                    return rating;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid input. Please enter a number between 1 and 5.");
                errorAlert.showAndWait();
            }
        }
    }

    private boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
    
   
    private void searchBooks() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Search Books");
        titleDialog.setHeaderText("Enter the title to search (press Enter to skip):");

        TextInputDialog authorDialog = new TextInputDialog();
        authorDialog.setTitle("Search Books");
        authorDialog.setHeaderText("Enter the author to search (press Enter to skip):");

        TextInputDialog releaseYearDialog = new TextInputDialog();
        releaseYearDialog.setTitle("Search Books");
        releaseYearDialog.setHeaderText("Enter the release year to search (press Enter to skip):");

        // Traditional way to get the response values.
        String searchTitle = titleDialog.showAndWait().orElse("");
        String searchAuthor = authorDialog.showAndWait().orElse("");
        String searchReleaseYear = releaseYearDialog.showAndWait().orElse("");

        // Perform the search
        List<Book> searchResults = library.searchBooks(searchTitle, searchAuthor, searchReleaseYear);

        // Create a new VBox to hold the search result buttons and "Back to User Actions" button
        VBox searchResultsBox = new VBox(10);

        // Initialize a new StringBuilder for each search
        StringBuilder resultsText = new StringBuilder();

        if (!searchResults.isEmpty()) {
            for (Book result : searchResults) {
                // Create a Button for each book
                Button bookButton = new Button(result.getTitle());

                // Set the action for the button
                bookButton.setOnAction(e -> displayBookDetails(result));

                // Add the button to the VBox
                searchResultsBox.getChildren().add(bookButton);

                // Append book details to the StringBuilder
                resultsText.append("Title: ").append(result.getTitle())
                        .append(", Author: ").append(result.getAuthor())
                        .append(", Release Year: ").append(result.getReleaseYear())
                        .append(", ISBN: ").append(result.getISBN())
                        .append(", Rating: ").append(result.getAverageRating())
                        .append(", Number of Reviews: ").append(result.getRatings().size())
                        .append("\n");
            }

            // Add "Back to User Actions" button
            Button backToUserActionsButton = new Button("Back to User Actions");
            backToUserActionsButton.setOnAction(e -> {
                // Reset the screen by calling the start method again
                start(new Stage());
            });

            // Add the "Back to User Actions" button to the VBox
            searchResultsBox.getChildren().add(backToUserActionsButton);
        } else {
            resultsText.append("No books found matching the search criteria.");
        }

        // Set the content of the TextArea
        searchResultsTextArea.setText(resultsText.toString());

        // Clear existing content and add the VBox with buttons to the main UI
        root.getChildren().clear();
        root.getChildren().add(searchResultsBox);
    }
    
private void displayBookDetails(Book book) {
        // Create a new stage for book details
        Stage bookDetailsStage = new Stage();
        bookDetailsStage.setTitle("Book Details - " + book.getTitle());

        // Create UI components for book details
        VBox bookDetailsRoot = new VBox(10);
        Label titleLabel = new Label("Title: " + book.getTitle());
        Label authorLabel = new Label("Author: " + book.getAuthor());
        Label publisherLabel = new Label("Publisher: " + book.getPublisher());
        Label releaseYearLabel = new Label("Release Year: " + book.getReleaseYear());
        Label isbnLabel = new Label("ISBN: " + book.getISBN());
        Label numCopiesLabel = new Label("Number of Copies: " + book.getNumCopies());
        Label categoryLabel = new Label("Category: " + book.getCategory());
        Label averageRatingLabel = new Label("Average Rating: " + book.getAverageRating());

        // Assuming 'comments' is a List<String> in the Book class
        Label commentsLabel = new Label("Comments: " + String.join(", ", book.getComments()));

        // Assuming 'ratings' is a List<Integer> in the Book class
        Label ratingsLabel = new Label("Ratings: " + book.getRatings().toString());

        bookDetailsRoot.getChildren().addAll(titleLabel, authorLabel, categoryLabel, averageRatingLabel, releaseYearLabel, isbnLabel, numCopiesLabel, publisherLabel, ratingsLabel, commentsLabel);

        // Create scene and set it to the stage
        Scene bookDetailsScene = new Scene(bookDetailsRoot, 300, 200);
        bookDetailsStage.setScene(bookDetailsScene);

        // Show the stage
        bookDetailsStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
