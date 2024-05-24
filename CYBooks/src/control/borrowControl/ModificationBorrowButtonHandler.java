package control.borrowControl;

import abstraction.Borrow;
import abstraction.db.DataBaseBorrow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;
import java.util.Optional;

import java.sql.SQLException;

/**
 * Class for the modification of a borrow
 */
public class ModificationBorrowButtonHandler implements EventHandler<ActionEvent> {
	private Label oldBorrowsDate;
	private Label oldBorrowsReturnDate;
	private Label oldBorrowsEffectiveReturnDate;
	
	private DatePicker newBorrowsDatePicker;
    private DatePicker newReturnDatePicker;
    private DatePicker newEffectiveReturnDatePicker;
    
    private TableView<Borrow> borrowsTable = new TableView<Borrow>();

    /**
     * ModificationUserButtonHandler constructor
     * @param oldBorrowsDate the old borrow's date
     * @param oldBorrowsReturnDate the old borrow's return date
     * @param oldBorrowsEffectiveReturnDate the old borrow's effective return date
     * @param newBorrowsDatePicker the new borrow's date
     * @param newReturnDatePicker the new borrow's return date
     * @param newEffectiveReturnDatePicker the new borrow's effective return date
     * @param borrowsTable the table of all the borrows
     */
    public ModificationBorrowButtonHandler(Label oldBorrowsDate, Label oldBorrowsReturnDate, Label oldBorrowsEffectiveReturnDate, DatePicker newBorrowsDatePicker, DatePicker newReturnDatePicker, DatePicker newEffectiveReturnDatePicker, TableView<Borrow> borrowsTable) {
    	this.oldBorrowsDate = oldBorrowsDate;
    	this.oldBorrowsReturnDate = oldBorrowsReturnDate;
    	this.oldBorrowsEffectiveReturnDate = oldBorrowsEffectiveReturnDate;
        this.newBorrowsDatePicker = newBorrowsDatePicker;
        this.newReturnDatePicker = newReturnDatePicker;
        this.newEffectiveReturnDatePicker = newEffectiveReturnDatePicker;
        this.borrowsTable = borrowsTable;
    }

    /**
     * Method to handle the modification of a borrow
     */
    @Override
    public void handle(ActionEvent event) {
    	// We get the new borrow's informations
    	LocalDate newBorrowDate = newBorrowsDatePicker.getValue();
    	LocalDate newReturnDate = newReturnDatePicker.getValue();
    	LocalDate newEffectiveReturnDate = newEffectiveReturnDatePicker.getValue();	
		
		// We check if at least one field is filled
		if(newBorrowDate == null && newReturnDate == null && newEffectiveReturnDate == null) {
			Alert errormodificationUserAlert = new Alert(AlertType.WARNING, "You need to fill at least one field if you want to modify a borrow.", ButtonType.OK);
    		errormodificationUserAlert.setTitle("Empty fields warning");
    		errormodificationUserAlert.showAndWait();
		}
		else {
			// We get the selected borrow to modify
			Borrow borrowToModify = borrowsTable.getSelectionModel().getSelectedItem();	
			
			LocalDate date = borrowToModify.getDateBorrowLocalDate();
			LocalDate returnDate = borrowToModify.getReturnDateLocalDate();
			LocalDate effectiveReturnDate = borrowToModify.getEffectiveReturnDateLocalDate();
			
			// For each information we check if we have a new value or not
			// If we have a new value we update the information
			if(newBorrowDate != null) {
				date = newBorrowDate;
			}
			if(newReturnDate != null) {
				returnDate = newReturnDate;
			}
			if(newEffectiveReturnDate != null) {
				effectiveReturnDate = newEffectiveReturnDate;
			}
			
			if(validDate(date, returnDate, effectiveReturnDate)) {
				// Set the "yes" / "cancel" button for the alert
				ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to modify this borrow ?",
		    			yesButton, cancelButton);
		    	
		    	modificationUserAlert.setTitle("Modify borrow warning");
		    	
		    	// We get the result of the button, if it's "yes" or "cancel"
		    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
		    	
		    	if(result.get().equals(yesButton)) {
		    		// We update the borrow's values
		    		borrowToModify.setDateBorrow(date);
		    		oldBorrowsDate.setText(date.toString());
					
					borrowToModify.setReturnDate(returnDate);
					oldBorrowsReturnDate.setText(returnDate.toString());
		    		if(effectiveReturnDate == null) {
		    			try {
		    				// Check if the new borrow is late
		    				borrowToModify.checkBorrowLate();
		    				
		    				// Check if the new borrw's duration changed
		    				borrowToModify.updateDuration();
		    				
				    		// We modify the borrow's informations in the database
		    				DataBaseBorrow.modifyBorrowInTable(borrowToModify, date.toString(), returnDate.toString(), borrowToModify.getDuration());
			    			
				    		modificationUserAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been modified.", ButtonType.OK);
					    	modificationUserAlert.setTitle("Borrow modified confirmation");
				    		modificationUserAlert.showAndWait();
				    		
						} catch (SQLException e) {
							System.err.println("Fail to modify a borrow in the database");
							Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while modifying the borrow.", ButtonType.OK);
							errorAlert.setTitle("Borrow modification error");
					        errorAlert.showAndWait();
						}
		    		}
		    		else {
		    			borrowToModify.setEffectiveReturnDate(effectiveReturnDate);
						oldBorrowsEffectiveReturnDate.setText(effectiveReturnDate.toString());
						try {
							// Check if the new borrow's late
		    				borrowToModify.checkBorrowLate();
		    				
		    				// Check if the new borrw's duration changed
		    				borrowToModify.updateDuration();

				    		// We modify the borrow's informations in the database
		    				DataBaseBorrow.modifyBorrowInTable(borrowToModify, date.toString(), returnDate.toString(), effectiveReturnDate.toString(), borrowToModify.getDuration());
				    		
				    		modificationUserAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been modified.", ButtonType.OK);
					    	modificationUserAlert.setTitle("Borrow modified confirmation");
				    		modificationUserAlert.showAndWait();
				    		
						} catch (SQLException e) {
							System.err.println("Fail to modify a borrow in the database");
							Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while modifying the borrow.", ButtonType.OK);
							errorAlert.setTitle("Borrow modification error");
					        errorAlert.showAndWait();
						}
		    		}
		    		
		    		// We reset the value of our DatePickers
		    		newBorrowsDatePicker.setValue(null);
		    		newReturnDatePicker.setValue(null);
		    		newEffectiveReturnDatePicker.setValue(null);
		    	}
			}
			else {
				Alert emailAlert = new Alert(Alert.AlertType.ERROR, "The date are not compatible, the borrow's date must be before the return date and the effective return date.", ButtonType.OK);
	        	emailAlert.setTitle("Date order warning");
	        	emailAlert.showAndWait();
			}
		}
    }
    
    /**
     * Method to check if the entered dates are logic
     * @param date the borrow's date
     * @param returnDate the borrow's return date
     * @param effectiveReturnDate the borrow's effective return date
     * @return true if the dates are logic and false if not
     */
    public static boolean validDate(LocalDate date, LocalDate returnDate, LocalDate effectiveReturnDate) {
    	if((effectiveReturnDate == null && date.isBefore(returnDate)) || date.equals(returnDate)) {
    		return true;
    	}
    	if(date.equals(effectiveReturnDate) && date.isBefore(returnDate)) {
    		return true;
    	}
    	if(date.isBefore(returnDate) && date.isBefore(effectiveReturnDate)) {
    		return true;
    	}
    	
    	return false;
    }
}