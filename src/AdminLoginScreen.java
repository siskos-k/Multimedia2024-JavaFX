import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminLoginScreen extends Application {

    private Library library;

    public AdminLoginScreen(Library library) {
        this.library = library;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Login");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("Admin Login");
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

            if (loginAsAdmin(username, password)) {
                messageLabel.setText("Login successful!");
                // Perform actions after successful login
                // For example, you can get the admin instance and perform actions
                Admin admin = getAdminByUsername(username);
                openAdminActionsScreen(admin);
                primaryStage.close();
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });
        root.getChildren().add(loginButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean loginAsAdmin(String username, String password) {
        for (Admin admin : library.getAdmins()) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return true; // Admin found, login successful
            }
        }
        return false; // Admin not found, login failed
    }

    private Admin getAdminByUsername(String username) {
        for (Admin admin : library.getAdmins()) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null; // Admin not found
    }

    private void performAdminActions(Admin admin) {
        // Add logic for admin actions after successful login
        // For example, open a new window, show admin-specific content, etc.
        System.out.println("Performing actions for admin: " + admin.getUsername());
    }
    private void openAdminActionsScreen(Admin admin) {
        AdminActionsScreen adminActionsScreen = new AdminActionsScreen(admin, library);
        adminActionsScreen.start(new Stage());
    }
}
