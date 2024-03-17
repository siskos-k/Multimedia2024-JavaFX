import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryManagementSystem extends Application {

    private static Library library; // Declare library as a static variable

    
    
    // Οι χρήστες είναι καλά παιδιά και δε θα κάνουν πολλές φορές rate το ίδιο βιβλίο
    
    public static void main(String[] args) {
        library = new Library(); // Initialize the library in the main method
        Admin medialabAdmin = new Admin("medialab", "medialab_2024");
        library.addAdmin(medialabAdmin);
//        
        library.deserializeUsers();
        library.deserializeBooks();
        library.deserializeBorrowings();
        library.serializeUsers();
        library.serializeBooks();
        library.serializeBorrowings();
//        
//        initialization without serialization
//        User medialabUser = new User("u", "u", "u", "u", "u", "u");
//        library.addUser(medialabUser);
//        library.addRandomUsers(5);
//        library.addSampleBooksAndRatings();
//        library.addSpecificBorrowings();
//      library.serializeUsers();
//      library.serializeBooks();
//      library.serializeBorrowings();
//        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to MultiMediaLibrary!");
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        HBox titleBox = new HBox(welcomeLabel);
        titleBox.setAlignment(Pos.CENTER);
        root.getChildren().add(titleBox);

        List<Book> topRatedBooks = library.getTopRatedBooks(5);

        VBox booksListVBox = new VBox(5);

        Label topRatedTitleLabel = new Label("Top Rated Books:");
        topRatedTitleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        booksListVBox.getChildren().add(topRatedTitleLabel);

        for (Book book : topRatedBooks) {
            double roundedRating = Math.round(book.getAverageRating() * 100.0) / 100.0;
            Label bookLabel = new Label(book.getTitle() + " (Rating: " + String.format("%.2f", roundedRating) + ")");
            bookLabel.setStyle("-fx-alignment: CENTER-LEFT;"); // Set the alignment to left
            booksListVBox.getChildren().add(bookLabel);
        }

        root.getChildren().add(booksListVBox);

        Button userButton = new Button("Log in as User");
        userButton.setOnAction(e -> showUserLogin(primaryStage));
        root.getChildren().add(userButton);

        Button adminButton = new Button("Log in as Admin");
        adminButton.setOnAction(e -> showAdminLogin());
        root.getChildren().add(adminButton);

        Button newUserButton = new Button("Create new user account");
        newUserButton.setOnAction(e -> showCreateUser());
        root.getChildren().add(newUserButton);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());
        root.getChildren().add(exitButton);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showUserLogin(Stage primaryStage) {
        UserLoginScreen userLoginScreen = new UserLoginScreen(library);
        userLoginScreen.start(new Stage());

        // Optional: Close the main stage (LibraryManagementSystem) after navigating to user login
        primaryStage.close();
    }

    private void showAdminLogin() {
        AdminLoginScreen adminLoginScreen = new AdminLoginScreen(library);
        adminLoginScreen.start(new Stage());
    }

    private void showCreateUser() {
        CreateNewUserScreen createNewUserScreen = new CreateNewUserScreen(library);
        createNewUserScreen.start(new Stage());
    }
}
