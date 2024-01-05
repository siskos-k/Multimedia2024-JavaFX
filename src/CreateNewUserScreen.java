import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateNewUserScreen extends Application {

    private Library library;

    public CreateNewUserScreen(Library library) {
        this.library = library;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create New User");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("Create New User");
        root.getChildren().add(titleLabel);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        root.getChildren().add(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        root.getChildren().add(passwordField);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        root.getChildren().add(nameField);

        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");
        root.getChildren().add(surnameField);

        TextField adtField = new TextField();
        adtField.setPromptText("Adt");
        root.getChildren().add(adtField);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        root.getChildren().add(emailField);

        Label messageLabel = new Label();
        root.getChildren().add(messageLabel);

        Button createUserButton = new Button("Create User");
        createUserButton.setOnAction(e -> {
            String newUserUsername = usernameField.getText();
            String newUserPassword = passwordField.getText();
            String newName = nameField.getText();
            String newSurname = surnameField.getText();
            String newAdt = adtField.getText();
            String newEmail = emailField.getText();

            // Check for duplicate email or ADT
            if (isDuplicateEmail(newEmail) || isDuplicateAdt(newAdt)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText(null);
                alert.setContentText("User with the same email or ADT already exists. Please use a different email or ADT.");
                alert.showAndWait();
            } else {
                // If no duplicates, create a new user
                User newUser = new User(newUserUsername, newUserPassword, newName, newSurname, newAdt, newEmail);
                library.addUser(newUser);
                messageLabel.setText("New user created successfully!");
            }
        });

        // Add these helper methods to check for duplicates
       
        root.getChildren().add(createUserButton);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private boolean isDuplicateEmail(String email) {
        return library.getUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean isDuplicateAdt(String adt) {
        return library.getUsers().stream().anyMatch(user -> user.getAdt().equals(adt));
    }
}
