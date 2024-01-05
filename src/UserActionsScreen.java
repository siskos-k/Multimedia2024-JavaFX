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

    public UserActionsScreen(User user, Library library) {
        this.user = user;
        this.library = library;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Actions");

        VBox root = new VBox(10);
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
                new Label("Search Results:"), searchResultsTextArea, exitButton);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBorrowedBooksListView() {
        ObservableList<String> borrowedBooks = FXCollections.observableArrayList();
        for (Borrowing borrowing : library.getAllBorrowings()) {
            if (borrowing.getUser().getUsername().equals(user.getUsername())) {
                Book borrowedBook = borrowing.getBook();
                // Check if the borrowed book exists in the current library's books
                if (library.getBooks().contains(borrowedBook)) {
                    borrowedBooks.add(borrowedBook.getTitle());
                }
            }
        }
        borrowedBooksListView.setItems(borrowedBooks);
    }


    private void borrowBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Borrow a Book");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the ISBN of the book to borrow:");

        // Traditional way to get the response value.
        String bookISBNToBorrow = dialog.showAndWait().orElse("");

        Book bookToBorrow = library.findBookByISBN(bookISBNToBorrow);

        if (bookToBorrow != null) {
            library.borrowBook(user, bookToBorrow);
            library.serializeUsers();
//            library.serializeBooks();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Borrow Book");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Book borrowed successfully!");
            successAlert.showAndWait();

            // Update the Borrowed Books ListView
            updateBorrowedBooksListView();
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
            // Check if the user has borrowed the specified book
            if (user.hasBorrowedBook(bookToRate)) {
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
                errorAlert.setContentText("You can only add ratings and comments for books you have borrowed.");
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

        // Display search results in the TextArea
        StringBuilder resultsText = new StringBuilder();
        if (!searchResults.isEmpty()) {
            resultsText.append("Search results:\n");
            for (Book result : searchResults) {
                resultsText.append("Title: ").append(result.getTitle())
                        .append(", Author: ").append(result.getAuthor())
                        .append(", Release Year: ").append(result.getReleaseYear())
                        .append(", ISBN: ").append(result.getISBN())
                        .append("\n");
            }
        } else {
            resultsText.append("No books found matching the search criteria.");
        }
        searchResultsTextArea.setText(resultsText.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
