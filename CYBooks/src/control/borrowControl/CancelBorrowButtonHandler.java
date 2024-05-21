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
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class CancelBorrowButtonHandler implements EventHandler<ActionEvent> {
	private TableView<Borrow> borrowsTable;
	
	 /**
     * CancelBorrowButtonHandler constructor
     * @param borrowsTable the table of all the borrows
     */
    public CancelBorrowButtonHandler(TableView<Borrow> borrowsTable) {
        this.borrowsTable = borrowsTable;
    }
    
	@Override
	public void handle(ActionEvent event) {
		// We get the selected borrow we want to validate the return
		Borrow borrowToValidate = borrowsTable.getSelectionModel().getSelectedItem();
		
		if(borrowToValidate.getEffectiveReturnDateLocalDate() != null) {
			// Set the "yes" / "cancel" button for the alert
	    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    	Alert returnBorrowAlert = new Alert(AlertType.WARNING, "Are you sure to cancel the return of this borrow ?",
	    			yesButton, cancelButton);
	    	
	    	returnBorrowAlert.setTitle("Cancel borrow warning");
	    	
	    	// We get the result of the button, if it's "yes" or "cancel"
	    	Optional<ButtonType> result = returnBorrowAlert.showAndWait();
	    	
	    	if(result.get().equals(yesButton)) {
	    		// We set the effective return date to today's date
	    		borrowToValidate.setEffectiveReturnDate(null);
	    		
		    	try {
		    		borrowToValidate.checkBorrowLate();
		    		
		    		// We modify the Borrow's effective return date in the text file
		    		DBConnect.modifyBorrowInTable(borrowToValidate, borrowToValidate.getDateBorrow(), borrowToValidate.getReturnDate(), borrowToValidate.getEffectiveReturnDate());
		    		
		            Alert validatedBorrowAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been canceled.", ButtonType.OK);
		            validatedBorrowAlert.setTitle("Borrow cancelation confirmation");
		            validatedBorrowAlert.showAndWait();
				} catch (SQLException e) {
					System.err.println("Fail to validate a borrow from the database");
					Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while canceling the borrow.", ButtonType.OK);
					errorAlert.showAndWait();
				}
	    	}
		}
		else {
			Alert alradyValidatedBorrowAlert = new Alert(AlertType.WARNING, "The borrow is on going, please choose another one.", ButtonType.OK);
			alradyValidatedBorrowAlert.setTitle("Borrow on going warning");
			alradyValidatedBorrowAlert.showAndWait();
		}
	}

}
