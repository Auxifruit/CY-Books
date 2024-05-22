package control.userControl;

import abstraction.Borrow;
import abstraction.User;
import abstraction.db.DBConnect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import presentation.UsersTable;

import java.sql.SQLException;
import java.util.Optional;


/**
 * The class to handle the event of the button deleting an user
 */
public class DeleteUserButtonHandler implements EventHandler<ActionEvent> {
	private UsersTable usersTable;

    /**
     * DeleteUserButtonHandler constructor
     * @param usersTable the class containing the data and the table for the users
     */
    public DeleteUserButtonHandler(UsersTable usersTable) {
        this.usersTable = usersTable;
    }

    /**
     * Method to handle the deletion of an user
     */
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
    		// We get the selected user we want to delete
    		User userToDelete = usersTable.getUsersTable().getSelectionModel().getSelectedItem();		
    		
	    	try {
	    		// We remove the user's borrows from our database
	    		DBConnect.deleteBorrowInTableByUsersID(userToDelete.getId());
	    		Borrow.removeBorrowByUsersID(userToDelete.getId());	    	
	    		
	    		// We remove the user from our database	    		
	    		DBConnect.deleteUserInTable(userToDelete);
	    		User.getAllUser().remove(userToDelete);

		    	usersTable.updateData();
	            
	            Alert deletedUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been deleted.", ButtonType.OK);
	    		deletedUserAlert.setTitle("User deleted confirmation");
	    		deletedUserAlert.showAndWait();
	    		
			} catch (SQLException e) {
				System.err.println("Fail to delete an user from the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the user.", ButtonType.OK);
				errorAlert.setTitle("User deletion error");
				errorAlert.showAndWait();
			}	
    	}
    }
}