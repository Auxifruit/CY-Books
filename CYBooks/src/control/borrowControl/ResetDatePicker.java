package control.borrowControl;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;

/**
 * Class to handle the event of the button reseting the DatePickers' values
 */
public class ResetDatePicker implements EventHandler<ActionEvent> {
	private DatePicker newBorrowsDatePicker;
    private DatePicker newReturnDatePicker;
    private DatePicker newEffectiveReturnDatePicker;
	
    /**
     * Constructor of the ResetDatePicker class
     * @param newBorrowsDatePicker the DatePicker for the borrow's date
     * @param newReturnDatePicker the DatePicker for the borrow's return date
     * @param newEffectiveReturnDatePicker the DatePicker for the borrow's effective return date
     */
    public ResetDatePicker(DatePicker newBorrowsDatePicker, DatePicker newReturnDatePicker, DatePicker newEffectiveReturnDatePicker) {
    	this.newBorrowsDatePicker = newBorrowsDatePicker;
    	this.newReturnDatePicker = newReturnDatePicker;
    	this.newEffectiveReturnDatePicker = newEffectiveReturnDatePicker;
    }
    
	@Override
	public void handle(ActionEvent arg0) {
		// Set the "yes" / "cancel" button for the alert
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to reset the DatePickers' values ?",
    			yesButton, cancelButton);
    	
    	modificationUserAlert.setTitle("Reset DatePickers' values warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		
    		// We reset the value of our DatePickers
    		newBorrowsDatePicker.setValue(null);
    		newReturnDatePicker.setValue(null);
    		newEffectiveReturnDatePicker.setValue(null);
    	}

	}

}
