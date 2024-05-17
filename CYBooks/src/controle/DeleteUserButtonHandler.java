package controle;

import abstraction.User;
import abstraction.db.DBConnect;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import presentation.UsersManagement;
import java.sql.SQLException;
import java.util.Optional;


/**
 * The class to handle the event of the button deleting an user
 */
public class DeleteUserButtonHandler implements EventHandler<ActionEvent> {
    private ObservableList<User> data;
    private TableView<User> usersTable;
    private Pagination pagination;

    /**
     * DeleteUserButtonHandler constructor
     * @param data the list of all the users
     * @param usersTable the table of all the users
     */
    public DeleteUserButtonHandler(ObservableList<User> data, TableView<User> usersTable, Pagination pagination) {
        this.data = data;
        this.usersTable = usersTable;
        this.pagination = pagination;
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
    		User userToDelete = usersTable.getSelectionModel().getSelectedItem();		
    		
	    	try {
	    		DBConnect.deleteUserInTable(userToDelete);
	    		
	    		// We remove it from the list of all the users, the data and the text file
	    		User.getAllUser().remove(userToDelete);
		    	data.remove(userToDelete);
		    	usersTable.setItems(data);
		    	
		    	// We update the pagination to see if we need to remove a new page or not
		    	pagination.setPageCount((int) Math.ceil((double) data.size() / UsersManagement.ROWS_PER_PAGE));
	            pagination.setCurrentPageIndex(0);
	            
	            Alert deletedUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been deleted", ButtonType.OK);
	    		deletedUserAlert.setTitle("User deleted");
	    		deletedUserAlert.showAndWait();
	    		
			} catch (SQLException e) {
				System.err.println("Fail to delete an user from the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the user.", ButtonType.OK);
				errorAlert.showAndWait();
			}	
    	}
    }
}