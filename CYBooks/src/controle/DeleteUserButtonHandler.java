package controle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Optional;

import abstraction.User2;
import abstraction.UserFile;

/**
 * The class to handle the event of the button deleting an user
 */
public class DeleteUserButtonHandler implements EventHandler<ActionEvent> {
    private ObservableList<User2> data;
    private TableView<User2> usersTable = new TableView<User2>();

    /**
     * DeleteUserButtonHandler constructor
     * @param data the list of all the users
     * @param usersTable the table of all the users
     */
    public DeleteUserButtonHandler(ObservableList<User2> data, TableView<User2> usersTable) {
        this.data = data;
        this.usersTable = usersTable;
        
    }

    @Override
    public void handle(ActionEvent event) {
    	// Set the "yes" / "cancel" button for the alert
    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert deleteUserAlert = new Alert(AlertType.WARNING, "Are you sure to delete this user ?",
    			yesButton, cancelButton);
    	
    	deleteUserAlert.setTitle("Delete user warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = deleteUserAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		// We get the selected user to delete
    		User2 userToDelete = usersTable.getSelectionModel().getSelectedItem();
    		
    		// We remove it from the list of all the users, the data and the text file
    		User2.getAllUser().remove(userToDelete);
	    	data.remove(userToDelete);
	    	try {
				UserFile.deleteUserInAFileTXT(userToDelete);
			} catch (IOException e1) {
				System.out.println("File not found");
			}

	    	Alert deletedUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been deleted", ButtonType.OK);
    		deletedUserAlert.setTitle("User deleted");
    		deletedUserAlert.showAndWait();
    	}
    }
}