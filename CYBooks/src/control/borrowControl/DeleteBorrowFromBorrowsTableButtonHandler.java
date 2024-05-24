package control.borrowControl;

import abstraction.Borrow;
import abstraction.User;
import abstraction.db.DataBaseBorrow;
import abstraction.db.DataBaseProblem;
import presentation.borrowPresentation.BorrowsTable;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;

/**
 * Class for the deletion of a borrow from the borrow's table
 */
public class DeleteBorrowFromBorrowsTableButtonHandler implements EventHandler<ActionEvent> {
    private BorrowsTable borrowsTable;

    /**
     * DeleteBorrowButtonHandler constructor
     * @param BorrowsTable the class containing the data and the table for the borrows
     */
    public DeleteBorrowFromBorrowsTableButtonHandler(BorrowsTable borrowsTable) {
        this.borrowsTable = borrowsTable;
    }

    /**
     * Method to handle the deletion of an borrow from the borrow's table
     */
    @Override
    public void handle(ActionEvent event) {
    	// Set the "yes" / "cancel" button for the alert
    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert deleteBorrowAlert = new Alert(AlertType.WARNING, "Are you sure to delete this borrow ?",
    			yesButton, cancelButton);
    	
    	deleteBorrowAlert.setTitle("Delete borrow warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = deleteBorrowAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		// We get the selected borrow we want to delete
    		Borrow borrowToDelete = borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItem();		
    		
	    	try {
	    		// We get the user's id to see if the deletion of the borrow affect its status
	    		int usersID = borrowToDelete.getUsersID();
	    		
	    		// We remove it from our database, the list of all the borrows and the data 
	    		DataBaseBorrow.deleteBorrowInTable(borrowToDelete);
	    		Borrow.getAllBorrow().remove(borrowToDelete);
	    		
	    		// We also remove the borrow's problem
	    		DataBaseProblem.deleteProblemInTable(borrowToDelete.getId());
	    		
	    		// We check if the cancellation change the user's status
	    		User.getUserById(usersID).checkUserStatus();
	    		
		    	borrowsTable.updateData();
	            
	            Alert deletedBorrowAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been deleted.", ButtonType.OK);
	    		deletedBorrowAlert.setTitle("Borrow deleted confirmation");
	    		deletedBorrowAlert.showAndWait();
	    		
			} catch (SQLException e) {
				System.err.println("Fail to delete a borrow from the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the borrow.", ButtonType.OK);
				errorAlert.showAndWait();
			}	
    	}
    }

}
