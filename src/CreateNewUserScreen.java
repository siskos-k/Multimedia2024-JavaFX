import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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

            User newUser = new User(newUserUsername, newUserPassword, newName, newSurname, newAdt, newEmail);
            library.addUser(newUser);
            messageLabel.setText("New user created successfully!");
        });
        root.getChildren().add(createUserButton);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
