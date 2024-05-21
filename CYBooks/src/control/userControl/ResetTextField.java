package control.userControl;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * The class to handle the event of the button reseting the DatePickers' values
 */
public class ResetTextField implements EventHandler<ActionEvent> {
	private TextField newFirstnameText;
    private TextField newLastnameText;
    private TextField newEmailText;
	
    /**
     * Constructor of the ResetDatePicker class
     * @param newFirstnameText the DatePicker for the borrow's date
     * @param newLastnameText the DatePicker for the borrow's return date
     * @param newEmailText the DatePicker for the borrow's effective return date
     */
    public ResetTextField(TextField newFirstnameText, TextField newLastnameText, TextField newEmailText) {
    	this.newFirstnameText = newFirstnameText;
    	this.newLastnameText = newLastnameText;
    	this.newEmailText = newEmailText;
    }
    
    /**
     * Method to handle the reset of the TextFields
     */
	@Override
	public void handle(ActionEvent arg0) {
		// Set the "yes" / "cancel" button for the alert
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to reset the TextFields' values ?",
    			yesButton, cancelButton);
    	
    	modificationUserAlert.setTitle("Reset TextFields' values warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		
    		// We reset the text in our text fields
    		newFirstnameText.clear();
    		newLastnameText.clear();
    		newEmailText.clear();
    	}

	}

}
