import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryManagementSystem extends Application {

    private static Library library; // Declare library as a static variable

    public static void main(String[] args) {
        library = new Library(); // Initialize the library in the main method
        Admin medialabAdmin = new Admin("a", "a");
        User medialabUser = new User("u", "u", "u", "u", "u", "u");
        library.addUser(medialabUser);

        library.addAdmin(medialabAdmin);
        library.addRandomUsers(5);
    
        library.addSampleBooksAndRatings();
        library.addSpecificBorrowings();


        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

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
        
      
        Scene scene = new Scene(root, 300, 200);
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

    // ... (existing methods)
}
