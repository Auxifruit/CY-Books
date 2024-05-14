package controle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import abstraction.User;
import abstraction.UserFile;

/**
 * The class to handle the event of the button creating an user
 */
public class CreateUserButtonHandler implements EventHandler<ActionEvent> {
    private ObservableList<User> data;
    private TextField firstnameText;
    private TextField lastnameText;
    private TextField emailText;

    /**
     * CreateUserButtonHandler constructor
     * @param data the list of all the users
     * @param firstnameText the new user's first name
     * @param lastnameText the new user's last name
     * @param emailText the new user's first name
     */
    public CreateUserButtonHandler(ObservableList<User> data, TextField firstnameText, TextField lastnameText, TextField emailText) {
        this.data = data;
        this.firstnameText = firstnameText;
        this.lastnameText = lastnameText;
        this.emailText = emailText;
    }

    @Override
    public void handle(ActionEvent event) {
    	// We check if all the field are filled
        if (firstnameText.getText().isEmpty() || lastnameText.getText().isEmpty() || emailText.getText().isEmpty()) {
            Alert errorCreateUserAlert = new Alert(Alert.AlertType.WARNING, "You need to fill all the fields", ButtonType.OK);
            errorCreateUserAlert.setTitle("Empty field(s)");
            errorCreateUserAlert.showAndWait();
        } else {
            Alert createUserAlert = new Alert(Alert.AlertType.CONFIRMATION, "The user has been created", ButtonType.OK);
            createUserAlert.setTitle("User created");
            createUserAlert.showAndWait();

            // We create an user and add it in our data
            User userToCreate = new User(lastnameText.getText(), firstnameText.getText(), emailText.getText());
            data.add(userToCreate);

            // We add our user in our text file
            UserFile.addUserInAFileTXT(userToCreate);

            // We reset the text in our text fields
            firstnameText.clear();
            lastnameText.clear();
            emailText.clear();
        }
    }
}