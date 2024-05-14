package controle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import abstraction.User2;
import abstraction.UserFile;

import java.io.IOException;
import java.util.Optional;

/**
 * The class to handle the event of the button modifying an user
 */
public class ModificationUserButtonHandler implements EventHandler<ActionEvent> {
	private Label oldUsersFirstname;
	private Label oldUsersLastname;
	private Label oldUsersEmail;
	
	private TextField newFirstnameText;
    private TextField newLastnameText;
    private TextField newEmailText;
    
    private TableView<User2> usersTable = new TableView<User2>();

    /**
     * ModificationUserButtonHandler constructor
     * @param oldUsersFirstname the old user's first name
     * @param oldUsersLastname the old user's last name
     * @param oldUsersEmail the old user's e-mail
     * @param newFirstnameText the new user's first name
     * @param newLastnameText the new user's last name
     * @param newEmailText the new user's e-mail
     * @param usersTable the table of all the users
     */
    public ModificationUserButtonHandler(Label oldUsersFirstname, Label oldUsersLastname, Label oldUsersEmail, TextField newFirstnameText, TextField newLastnameText, TextField newEmailText, TableView<User2> usersTable) {
    	this.oldUsersFirstname = oldUsersFirstname;
    	this.oldUsersLastname = oldUsersLastname;
    	this.oldUsersEmail = oldUsersEmail;
        this.newFirstnameText = newFirstnameText;
        this.newLastnameText = newLastnameText;
        this.newEmailText = newEmailText;
        this.usersTable = usersTable;
    }

    @Override
    public void handle(ActionEvent event) {
    	// We get the new user's informations
    	String newFirstname = newFirstnameText.getText();
		String newlastname = newLastnameText.getText();
		String newEmail = newEmailText.getText();
		
		// We check if at least one field is filled
		if((newFirstname.equals(null) || newFirstname.isEmpty()) && (newlastname.equals(null) || newlastname.isEmpty()) && (newEmail.equals(null) || newEmail.isEmpty())) {
			Alert errormodificationUserAlert = new Alert(AlertType.WARNING, "You need to fill at least one field", ButtonType.OK);
    		errormodificationUserAlert.setTitle("Empty fields");
    		errormodificationUserAlert.showAndWait();
		}
		else {
			// Set the "yes" / "cancel" button for the alert
			ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to modify this user ?",
	    			yesButton, cancelButton);
	    	
	    	modificationUserAlert.setTitle("Modify user warning");
	    	
	    	// We get the result of the button, if it's "yes" or "cancel"
	    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
	    	
	    	if(result.get().equals(yesButton)) {
	    		// We get the selected user to modify
	    		User2 userToModify = usersTable.getSelectionModel().getSelectedItem();
	    		
	    		// For each information we check if we have a new value or not
	    		// If we have a new value we update the information
	    		if(!(newlastname.equals(null) || newlastname.isEmpty())) {
	    			userToModify.setLastname(newlastname);
	    			oldUsersLastname.setText(newlastname);
	    		}
	    		if(!(newFirstname.equals(null) || newFirstname.isEmpty())) {
	    			userToModify.setFirstname(newFirstname);
	    			oldUsersFirstname.setText(newFirstname);
	    		}
	    		if(!(newEmail.equals(null) || newEmail.isEmpty())) {
	    			userToModify.setEmail(newEmail);
	    			oldUsersEmail.setText(newEmail);
	    		}
	    		
	    		try {
	    			// We modify the user's informations in the text file
					UserFile.modifyUserInAFileTXT(userToModify, newlastname, newFirstname, newEmail);
				} catch (IOException e) {
					System.err.println("File not found.");
				}
	    	}
	    	
	    	modificationUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been modified", ButtonType.OK);
	    	modificationUserAlert.setTitle("User modified");
    		modificationUserAlert.showAndWait();
		}
    }
}