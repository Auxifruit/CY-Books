package controle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import abstraction.User2;
import abstraction.UserFile;

public class CreateUserButtonHandler implements EventHandler<ActionEvent> {
    private ObservableList<User2> data;
    private TextField firstnameText;
    private TextField lastnameText;
    private TextField emailText;

    public CreateUserButtonHandler(ObservableList<User2> data, TextField firstnameText, TextField lastnameText, TextField emailText) {
        this.data = data;
        this.firstnameText = firstnameText;
        this.lastnameText = lastnameText;
        this.emailText = emailText;
    }

    @Override
    public void handle(ActionEvent event) {
        if (firstnameText.getText().isEmpty() || lastnameText.getText().isEmpty() || emailText.getText().isEmpty()) {
            Alert errorCreateUserAlert = new Alert(Alert.AlertType.WARNING, "You need to fill all the fields", ButtonType.OK);
            errorCreateUserAlert.setTitle("Empty field(s)");
            errorCreateUserAlert.showAndWait();
        } else {
            Alert createUserAlert = new Alert(Alert.AlertType.CONFIRMATION, "The user has been created", ButtonType.OK);
            createUserAlert.setTitle("User created");
            createUserAlert.showAndWait();

            User2 userToCreate = new User2(lastnameText.getText(), firstnameText.getText(), emailText.getText());
            data.add(userToCreate);

            UserFile.addUserInAFileTXT(userToCreate);

            firstnameText.clear();
            lastnameText.clear();
            emailText.clear();
        }
    }
}