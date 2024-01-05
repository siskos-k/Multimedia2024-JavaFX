import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLoginScreen extends Application {

    private Library library;

    public UserLoginScreen(Library library) {
        this.library = library;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Login");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("User Login");
        root.getChildren().add(titleLabel);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        root.getChildren().add(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        root.getChildren().add(passwordField);

        Label messageLabel = new Label();
        root.getChildren().add(messageLabel);

        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (loginAsUser(username, password)) {
                messageLabel.setText("Login successful!");
                User cur = library.getUserByUsername(username);
                performUserActions(cur, primaryStage);
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });
        root.getChildren().add(loginButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean loginAsUser(String username, String password) {
        for (User user : library.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // User found, login successful
            }
        }
        return false; // User not found, login failed
    }

    private void performUserActions(User user, Stage primaryStage) {
        // Launch the UserActionsScreen with the current user and library
        UserActionsScreen userActionsScreen = new UserActionsScreen(user, library);
        userActionsScreen.start(new Stage());

        // Close the login screen
        primaryStage.close();
    }
}
