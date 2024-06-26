package control.borrowControl;

import abstraction.Borrow;
import abstraction.db.DataBaseBorrow;

import java.time.LocalDate;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;

/**
 * Class for the return of a borrow
 */
public class ReturnBorrowButtonHandler implements EventHandler<ActionEvent> {
	private TableView<Borrow> borrowsTable;
	
	 /**
     * ReturnBorrowButtonHandler constructor
     * @param borrowsTable the table of all the borrows
     */
    public ReturnBorrowButtonHandler(TableView<Borrow> borrowsTable) {
        this.borrowsTable = borrowsTable;
    }
    
    /**
     * Method to handle the return of a borrow
     */
	@Override
	public void handle(ActionEvent event) {
		// We get the selected borrow we want to validate the return
		Borrow borrowToValidate = borrowsTable.getSelectionModel().getSelectedItem();
		
		if(borrowToValidate.getEffectiveReturnDateLocalDate() == null) {
			// Set the "yes" / "cancel" button for the alert
	    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    	Alert returnBorrowAlert = new Alert(AlertType.WARNING, "Are you sure to validate the return of this borrow ?",
	    			yesButton, cancelButton);
	    	
	    	returnBorrowAlert.setTitle("Validate borrow warning");
	    	
	    	// We get the result of the button, if it's "yes" or "cancel"
	    	Optional<ButtonType> result = returnBorrowAlert.showAndWait();
	    	
	    	if(result.get().equals(yesButton)) {
	    		// We set the effective return date to today's date
	    		borrowToValidate.setEffectiveReturnDate(LocalDate.now());
	    		
		    	try {
		    		borrowToValidate.checkBorrowLate();
		    		
		    		// We modify the Borrow's effective return date in the text file
		    		DataBaseBorrow.modifyBorrowInTable(borrowToValidate, borrowToValidate.getDateBorrow(), borrowToValidate.getReturnDate(), LocalDate.now().toString(), borrowToValidate.getDuration());
		    		
		    		borrowsTable.getSelectionModel().select(null);
		    		
		            Alert validatedBorrowAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been validated.", ButtonType.OK);
		            validatedBorrowAlert.setTitle("Borrow validation confirmation");
		            validatedBorrowAlert.showAndWait();
				} catch (SQLException e) {
					System.err.println("Fail to validate a borrow from the database");
					Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while validating the borrow.", ButtonType.OK);
					errorAlert.showAndWait();
				}
	    	}
		}
		else {
			Alert alradyValidatedBorrowAlert = new Alert(AlertType.WARNING, "The borrow has already been validated, please choose another one.", ButtonType.OK);
			alradyValidatedBorrowAlert.setTitle("Borrow already validated warning");
			alradyValidatedBorrowAlert.showAndWait();
		}
	}

}
