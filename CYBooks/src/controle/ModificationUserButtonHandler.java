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

public class ModificationUserButtonHandler implements EventHandler<ActionEvent> {
	private Label oldUsersFirstname;
	private Label oldUsersLastname;
	private Label oldUsersEmail;
	
	private TextField newFirstnameText;
    private TextField newLastnameText;
    private TextField newEmailText;
    
    private TableView<User2> usersTable = new TableView<User2>();

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
    	String newFirstname = newFirstnameText.getText();
		String newlastname = newLastnameText.getText();
		String newEmail = newEmailText.getText();
		if((newFirstname.equals(null) || newFirstname.isEmpty()) && (newlastname.equals(null) || newlastname.isEmpty()) && (newEmail.equals(null) || newEmail.isEmpty())) {
			Alert errormodificationUserAlert = new Alert(AlertType.WARNING, "You need to fill at least one field", ButtonType.OK);
    		errormodificationUserAlert.setTitle("Empty fields");
    		errormodificationUserAlert.showAndWait();
		}
		else {
			ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to modify this user ?",
	    			yesButton, cancelButton);
	    	
	    	modificationUserAlert.setTitle("Modify user warning");
	    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
	    	
	    	if(result.get().equals(yesButton)) {
	    		User2 userToModify = usersTable.getSelectionModel().getSelectedItem();
	    		if(!(newlastname.equals(null) || newlastname.isEmpty())) {
	    			userToModify.setLastname(newlastname);
	    			oldUsersLastname.setText("Lastname : " + newlastname);
	    		}
	    		if(!(newFirstname.equals(null) || newFirstname.isEmpty())) {
	    			userToModify.setFirstname(newFirstname);
	    			oldUsersFirstname.setText("Firstname : " + newFirstname);
	    		}
	    		if(!(newEmail.equals(null) || newEmail.isEmpty())) {
	    			userToModify.setEmail(newEmail);
	    			oldUsersEmail.setText("E-mail : " + newEmail);
	    		}
	    		
	    		try {
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