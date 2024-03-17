import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class AdminActionsScreen extends Application {

    private Admin admin;
    private Library library;

    private ListView<String> allBooksListView;
    private ListView<String> allCategoriesListView;

    public AdminActionsScreen(Admin admin, Library library) {
        this.admin = admin;
        this.library = library;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Actions");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        Label welcomeLabel = new Label("Welcome, Admin " + admin.getUsername() + "!");
        root.getChildren().add(welcomeLabel);

        Label titleLabel = new Label("All Titles:");
        allBooksListView = new ListView<>();
        updateAllBooksListView();

        Button viewAllBooksButton = new Button("View All Books info");
        viewAllBooksButton.setOnAction(e -> viewAllBooks());

        Button addBookButton = new Button("Add a Book");
        addBookButton.setOnAction(e -> addBook());

        Button editBookButton = new Button("Edit a Book");
        editBookButton.setOnAction(e -> editBook());

        Button deleteBookButton = new Button("Delete a Book");
        deleteBookButton.setOnAction(e -> deleteBook());

        allCategoriesListView = new ListView<>();
        updateAllCategoriesListView();

        Button editCategoryButton = new Button("Edit a Category");
        editCategoryButton.setOnAction(e -> editCategory());

        Button deleteCategoryButton = new Button("Delete a Category");
        deleteCategoryButton.setOnAction(e -> deleteCategory());

        Button viewAllBorrowingsButton = new Button("View All Borrowings");
        viewAllBorrowingsButton.setOnAction(e -> viewAllBorrowings());

        Button terminateBorrowingButton = new Button("Terminate Borrowing");
        terminateBorrowingButton.setOnAction(e -> terminateBorrowingByAdmin());

        Button editUserCredentialsButton = new Button("Edit User Credentials");
        editUserCredentialsButton.setOnAction(e -> editUserCredentials());
        
        Button deleteUserButton = new Button("Delete a User");
        deleteUserButton.setOnAction(e -> deleteUser());

        Button exitButton = new Button("Exit Admin Actions");
        exitButton.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(
                titleLabel, allBooksListView, viewAllBooksButton, addBookButton, editBookButton,
                deleteBookButton, allCategoriesListView, editCategoryButton,
                deleteCategoryButton, viewAllBorrowingsButton, terminateBorrowingButton,
                editUserCredentialsButton, deleteUserButton, exitButton
        );

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateAllBooksListView() {
        ObservableList<String> allBooks = FXCollections.observableArrayList();
        for (Book book : library.getBooks()) {
            allBooks.add(book.getTitle());
        }
        allBooksListView.setItems(allBooks);
    }
    private static class EditUserDialog extends Dialog<User> {

        public EditUserDialog(User user) {
            setTitle("Edit User Credentials");
            setHeaderText(null);

            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the user credentials form layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Add form fields
            TextField newUsernameField = new TextField(user.getUsername());
            PasswordField newPasswordField = new PasswordField();
            TextField newNameField = new TextField(user.getName());
            TextField newSurnameField = new TextField(user.getSurname());
            TextField newAdtField = new TextField(user.getAdt());
            TextField newEmailField = new TextField(user.getEmail());

            // Set up the form layout
            grid.add(new Label("New Username:"), 0, 0);
            grid.add(newUsernameField, 1, 0);
            grid.add(new Label("New Password:"), 0, 1);
            grid.add(newPasswordField, 1, 1);
            grid.add(new Label("New Name:"), 0, 2);
            grid.add(newNameField, 1, 2);
            grid.add(new Label("New Surname:"), 0, 3);
            grid.add(newSurnameField, 1, 3);
            grid.add(new Label("New Adt:"), 0, 4);
            grid.add(newAdtField, 1, 4);
            grid.add(new Label("New Email:"), 0, 5);
            grid.add(newEmailField, 1, 5);

            getDialogPane().setContent(grid);

            // Enable/Disable save button based on whether a new username was entered
            Node saveButton = getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            newUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            // Convert the result to a User object when the save button is clicked
            setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    // Perform the actual user update
                    user.setUsername(newUsernameField.getText());
                    user.setPassword(newPasswordField.getText());
                    user.setName(newNameField.getText());
                    user.setSurname(newSurnameField.getText());
                    user.setAdt(newAdtField.getText());
                    user.setEmail(newEmailField.getText());

                    // Return the updated user
                    return user;
                }
                return null;
            });
        }
    }

    private void viewAllBooks() {
        // Implement view all books functionality
        // You can display the books in a new window or in a TextArea within the same window

        // Create a StringBuilder to store book information
        StringBuilder bookInfo = new StringBuilder();

        for (Book book : library.getBooks()) {
            // Append basic information
            bookInfo.append("Title: ").append(book.getTitle()).append("\n");
            bookInfo.append("Author: ").append(book.getAuthor()).append("\n");
            bookInfo.append("Release Year: ").append(book.getReleaseYear()).append("\n");
            bookInfo.append("ISBN: ").append(book.getISBN()).append("\n");
            bookInfo.append("Category: ").append(book.getCategory()).append("\n");
            bookInfo.append("Number of Copies: ").append(book.getNumCopies()).append("\n");

            // Append average rating information
            bookInfo.append("Average Rating: ").append(book.getAverageRating()).append("\n");

            // Display comments and ratings
            List<String> comments = book.getComments();
            List<Integer> ratings = book.getRatings();
            if (!comments.isEmpty()) {
                bookInfo.append("Comments: ").append(String.join(", ", comments)).append("\n");
            }
            if (!ratings.isEmpty()) {
                bookInfo.append("Ratings: ").append(ratings).append("\n");
            }

            // Add a separator between books
            bookInfo.append("\n");
        }

        // Display the information (you can replace this with your preferred UI component)
        TextArea textArea = new TextArea(bookInfo.toString());
        textArea.setEditable(false);

        // Create a new window to display the book information
        Stage bookInfoStage = new Stage();
        bookInfoStage.setTitle("All Books Information");
        bookInfoStage.setScene(new Scene(new VBox(new Label("Book Information"), textArea), 600, 400));
        bookInfoStage.show();
    }

    private void addBook() {
        // Prompt the admin to enter book details
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add a Book");
        titleDialog.setHeaderText("Enter title:");
        String newTitle = titleDialog.showAndWait().orElse("");

        TextInputDialog authorDialog = new TextInputDialog();
        authorDialog.setTitle("Add a Book");
        authorDialog.setHeaderText("Enter author:");
        String newAuthor = authorDialog.showAndWait().orElse("");

        TextInputDialog publisherDialog = new TextInputDialog();
        publisherDialog.setTitle("Add a Book");
        publisherDialog.setHeaderText("Enter publisher:");
        String newPublisher = publisherDialog.showAndWait().orElse("");

        TextInputDialog releaseYearDialog = new TextInputDialog();
        releaseYearDialog.setTitle("Add a Book");
        releaseYearDialog.setHeaderText("Enter release year:");
        int newReleaseYear = Integer.parseInt(releaseYearDialog.showAndWait().orElse("0"));

        TextInputDialog ISBNDialog = new TextInputDialog();
        ISBNDialog.setTitle("Add a Book");
        ISBNDialog.setHeaderText("Enter ISBN:");
        String newISBN = ISBNDialog.showAndWait().orElse("");

        TextInputDialog numCopiesDialog = new TextInputDialog();
        numCopiesDialog.setTitle("Add a Book");
        numCopiesDialog.setHeaderText("Enter number of copies:");
        int newNumCopies = Integer.parseInt(numCopiesDialog.showAndWait().orElse("0"));

        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add a Book");
        categoryDialog.setHeaderText("Enter category:");
        String newCategory = categoryDialog.showAndWait().orElse("");

        // Call the method to add a book
        library.addBookByAdmin(admin, newTitle, newAuthor, newPublisher, newReleaseYear, newISBN, newNumCopies, newCategory);
        System.out.println("Book added successfully!");
        library.serializeUsers();
        library.serializeBooks();
        library.serializeBorrowings();
        updateAllBooksListView(); // Update the displayed books
        updateAllCategoriesListView();
    }

    private void editBook() {
        // Prompt the admin to enter the ISBN of the book to edit
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit a Book");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the ISBN of the book to edit:");

        String bookISBNToEdit = dialog.showAndWait().orElse("");

        // Check if the book exists
        Book bookToEdit = library.findBookByISBN(bookISBNToEdit);

        if (bookToEdit != null) {
            // Prompt the admin to enter the new details for the book
            Dialog<Book> editBookDialog = new EditBookDialog(bookToEdit);
            editBookDialog.initOwner(allBooksListView.getScene().getWindow());
            editBookDialog.setTitle("Edit Book");
            editBookDialog.setHeaderText(null);
            editBookDialog.showAndWait();

            // Update the book list view after editing
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
            updateAllBooksListView();
            
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Book with ISBN " + bookISBNToEdit + " not found.");
            errorAlert.showAndWait();
        }
    }

    private void deleteBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete a Book");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the ISBN of the book to delete:");

        // Traditional way to get the response value.
        String bookISBNToDelete = dialog.showAndWait().orElse("");

        // Call the method to delete a book
        library.deleteBookByAdmin(admin, bookISBNToDelete);
        library.serializeUsers();
        library.serializeBooks();
        library.serializeBorrowings();

        // Update the All Books ListView
        updateAllBooksListView();
        // Save changes to books
    }

    private void viewAllCategoriesInfo() {
        // Create a StringBuilder to store category information
        StringBuilder categoryInfo = new StringBuilder();

        for (String category : library.getAllUniqueCategories()) {
            categoryInfo.append("Category: ").append(category).append("\n");

            // Add a separator between categories
            categoryInfo.append("\n");
        }

        // Display the information (you can replace this with your preferred UI component)
        TextArea textArea = new TextArea(categoryInfo.toString());
        textArea.setEditable(false);

        // Create a new window to display the category information
        Stage categoryInfoStage = new Stage();
        categoryInfoStage.setTitle("All Categories Information");
        categoryInfoStage.setScene(new Scene(new VBox(new Label("Category Information"), textArea), 400, 300));
        categoryInfoStage.show();
    }

    private void updateAllCategoriesListView() {
        ObservableList<String> allCategories = FXCollections.observableArrayList(library.getAllUniqueCategories());
        allCategoriesListView.setItems(allCategories);
    }

    private void viewAllCategories() {
        // Implement view all categories functionality
        viewAllCategoriesInfo();
    }
    private void deleteUser() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Delete a User");
        usernameDialog.setHeaderText("Enter the username of the user to delete:");
        String usernameToDelete = usernameDialog.showAndWait().orElse("");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete the user with username: " + usernameToDelete + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Call the method to delete a user
            library.deleteUser(admin, usernameToDelete);
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
            
            // Update the user list view after deletion (if you have one)
            // updateAllUsersListView();
            
            // Update the All Books ListView
            updateAllBooksListView();

            // Update the All Categories ListView
            updateAllCategoriesListView();

            // Update the viewAllBorrowings information
            viewAllBorrowings();
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
        }
    }
    private void editCategory() {
        // Prompt the admin to enter the old category (can be empty) and the new category
        TextInputDialog oldCategoryDialog = new TextInputDialog();
        oldCategoryDialog.setTitle("Edit a Category");
        oldCategoryDialog.setHeaderText("Enter the old category:");
        String oldCategory = oldCategoryDialog.showAndWait().orElse("");

        TextInputDialog updatedCategoryDialog = new TextInputDialog();
        updatedCategoryDialog.setTitle("Edit a Category");
        updatedCategoryDialog.setHeaderText("Enter the new category:");
        String updatedCategory = updatedCategoryDialog.showAndWait().orElse("");

        // Validate that the old category exists if not empty
        if (!oldCategory.isEmpty() && !library.getAllUniqueCategories().contains(oldCategory)) {
            // Display an error alert for invalid old category
            showErrorAlert("Invalid Old Category", "Old category does not exist.");
            return; // Exit the method
        }

        // Validate that the new category is not empty
        if (updatedCategory.isEmpty()) {
            // Display an error alert for empty new category
            showErrorAlert("Empty New Category", "New category cannot be empty.");
            return; // Exit the method
        }

        // Call the method to add or update a category
        if (oldCategory.isEmpty()) {
            // Adding a new category
        	showErrorAlert("Empty Old Category", "Old category cannot be empty.");
           
        } else {
            // Updating an existing category
            library.UpdateCategory(oldCategory, updatedCategory);
            System.out.println("Category updated successfully!");
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
        }

        // Update the displayed categories
        updateAllCategoriesListView();

        // View all categories information after editing
        viewAllCategoriesInfo();
    }

    // Helper method to show an error alert
    private void showErrorAlert(String title, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }



    private void deleteCategory() {
        // Prompt the admin to enter the category to remove
        TextInputDialog categoryToRemoveDialog = new TextInputDialog();
        categoryToRemoveDialog.setTitle("Remove a Category");
        categoryToRemoveDialog.setHeaderText("Enter the category to remove:");
        String categoryToRemove = categoryToRemoveDialog.showAndWait().orElse("");

        // Validate that the category exists
        if (!library.getAllUniqueCategories().contains(categoryToRemove)) {
            // Display an error alert for invalid category
            showErrorAlert("Invalid Category", "Category does not exist.");
            return; // Exit the method
        }

        // Call the method to remove the category and associated books
        library.removeCategoryAndBooks(categoryToRemove);
        library.serializeUsers();
        library.serializeBooks();
        library.serializeBorrowings();
        System.out.println("Category and associated books removed successfully!");

        // Update the displayed categories
        updateAllCategoriesListView();

        // Update the displayed books
        updateAllBooksListView();
    }


    


    private void viewAllBorrowings() {
        // Create a StringBuilder to store borrowing information
        StringBuilder borrowingInfo = new StringBuilder();
        System.out.println("view all borrowings called");
        System.out.println("These are the available borrowings: "+library.getAllBorrowings());

        for (Borrowing borrowing : library.getAllBorrowings()) {
            // Append basic information
            borrowingInfo.append("Username: ").append(borrowing.getUser().getUsername()).append("\n");
            borrowingInfo.append("Book ISBN: ").append(borrowing.getBook().getISBN()).append("\n");
            borrowingInfo.append("Borrow Date: ").append(borrowing.getBorrowingDate()).append("\n");
            borrowingInfo.append("Return Date: ").append(borrowing.getReturnDate()).append("\n");
            // Add a separator between borrowings
            borrowingInfo.append("\n");
        }

        // Display the information (you can replace this with your preferred UI component)
        TextArea textArea = new TextArea(borrowingInfo.toString());
        textArea.setEditable(false);

        // Create a new window to display the borrowing information
        Stage borrowingInfoStage = new Stage();
        borrowingInfoStage.setTitle("All Borrowings Information");
        borrowingInfoStage.setScene(new Scene(new VBox(new Label("Borrowing Information"), textArea), 400, 300));
        borrowingInfoStage.show();
    }


    private void terminateBorrowingByAdmin() {
        // Implement terminate borrowing by admin functionality
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Terminate Borrowing");
        usernameDialog.setHeaderText("Enter the username of the user:");
        String usernameToTerminate = usernameDialog.showAndWait().orElse("");

        TextInputDialog ISBNDialog = new TextInputDialog();
        ISBNDialog.setTitle("Terminate Borrowing");
        ISBNDialog.setHeaderText("Enter the ISBN of the book to terminate the borrowing:");
        String ISBNToTerminate = ISBNDialog.showAndWait().orElse("");

        // Call the method to terminate borrowing by admin
        Borrowing terminatedBorrowing = library.terminateBorrowingByAdmin(admin, usernameToTerminate, ISBNToTerminate);

        if (terminatedBorrowing != null) {
            // Display a confirmation message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Terminate Borrowing");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Borrowing terminated successfully!");
            successAlert.showAndWait();

            // Update the All Books ListView
            updateAllBooksListView();
            library.serializeUsers();
            library.serializeBooks();
            library.serializeBorrowings();
            // Update the All Categories ListView
            updateAllCategoriesListView();

            // Update the viewAllBorrowings information
            viewAllBorrowings();
        } else {
            // Display an error alert if termination fails
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Borrowing termination failed. Please check the username and ISBN.");
            errorAlert.showAndWait();
        }
    }


    private void editUserCredentials() {
        // Prompt the admin to enter the username of the user to edit
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Edit User Credentials");
        usernameDialog.setHeaderText("Enter the username of the user to edit:");
        String targetUsername = usernameDialog.showAndWait().orElse("");

        // Check if the user exists
        User userToEdit = library.getUserByUsername(targetUsername);

        if (userToEdit != null) {
            // Create the user credentials editing dialog
            Dialog<User> editUserDialog = new EditUserDialog(userToEdit);
            editUserDialog.initOwner(allBooksListView.getScene().getWindow());
            editUserDialog.setTitle("Edit User Credentials");
            editUserDialog.setHeaderText(null);

            // Show the dialog and wait for user input
            Optional<User> result = editUserDialog.showAndWait();

            // If the user clicked Save, update the user credentials
            result.ifPresent(updatedUser -> {
            	library.editUserCredentialsByAdmin(
            		    admin,
            		    userToEdit.getUsername(),
            		    updatedUser.getUsername(),
            		    updatedUser.getPassword(),
            		    updatedUser.getName(),
            		    updatedUser.getSurname(),
            		    updatedUser.getAdt(),
            		    updatedUser.getEmail()
            		);
                System.out.println("User credentials updated successfully!");
                library.serializeUsers();
                library.serializeBooks();
                library.serializeBorrowings();
            });

        } else {
            // Display an error alert for user not found
            showErrorAlert("User Not Found", "User with username " + targetUsername + " not found.");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Inner class for the Edit Book dialog
    private static class EditBookDialog extends Dialog<Book> {

        public EditBookDialog(Book book) {
            setTitle("Edit Book");
            setHeaderText(null);

            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the book form layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Add form fields
            TextField titleField = new TextField(book.getTitle());
            TextField authorField = new TextField(book.getAuthor());
            TextField publisherField = new TextField(book.getPublisher());
            TextField releaseYearField = new TextField(Integer.toString(book.getReleaseYear()));
            TextField numCopiesField = new TextField(Integer.toString(book.getNumCopies()));
            TextField categoryField = new TextField(book.getCategory());

            // Set up the form layout
            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Author:"), 0, 1);
            grid.add(authorField, 1, 1);
            grid.add(new Label("Publisher:"), 0, 2);
            grid.add(publisherField, 1, 2);
            grid.add(new Label("Release Year:"), 0, 3);
            grid.add(releaseYearField, 1, 3);
            grid.add(new Label("Number of Copies:"), 0, 4);
            grid.add(numCopiesField, 1, 4);
            grid.add(new Label("Category:"), 0, 5);
            grid.add(categoryField, 1, 5);

            getDialogPane().setContent(grid);

            // Enable/Disable save button based on whether a title was entered
            Node saveButton = getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            // Do some validation (e.g., title is not empty)
            titleField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            // Convert the result to a Book object when the save button is clicked
            setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    // Perform the actual book update
                    book.setTitle(titleField.getText());
                    book.setAuthor(authorField.getText());
                    book.setPublisher(publisherField.getText());
                    book.setReleaseYear(Integer.parseInt(releaseYearField.getText()));
                    book.setNumCopies(Integer.parseInt(numCopiesField.getText()));
                    book.setCategory(categoryField.getText());

                    // Return the updated book
                    return book;
                }
                return null;
            });
        }
    }
}
