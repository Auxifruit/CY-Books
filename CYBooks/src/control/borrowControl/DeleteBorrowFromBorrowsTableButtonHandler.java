package control.borrowControl;

import java.sql.SQLException;
import java.util.Optional;

import abstraction.Borrow;
import abstraction.db.DBConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import presentation.BorrowsTable;

/**
 * The class to handle the event of the button deleting a borrow
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
     * Method to handle the deletion of an borrow
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
	    		// We remove it from our database, the list of all the borrows and the data 
	    		DBConnect.deleteBorrowInTable(borrowToDelete);
	    		Borrow.getAllBorrow().remove(borrowToDelete);
	    		
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
