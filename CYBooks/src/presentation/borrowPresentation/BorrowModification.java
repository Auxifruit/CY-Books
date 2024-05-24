package presentation.borrowPresentation;

import abstraction.Borrow;
import control.borrowControl.ModificationBorrowButtonHandler;
import control.borrowControl.ResetDatePicker;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * The class containing the pane to modify a borrow
 */
public class BorrowModification {
	private VBox borrowsModificationVBox;
	
    private TableView<Borrow> borrowsTable;
    
    /**
	 * Constructor of the BorrowModification class
	 * @param borrowsTable the table view displaying the informations
	 */
    public BorrowModification(TableView<Borrow> borrowsTable) {
        this.borrowsTable = borrowsTable;
        createUserModificationPane();
    }
    
    /**
	 * Getter to get the VBox containing all the element for the modification of a borrow
	 * @return the the VBox containing all the element for the modification of a borrow
	 */
	public VBox getBorrowsModificationVBox() {
		return borrowsModificationVBox;
	}

    /**
	 * Method to create a pane to modify a borrow
	 * @return the pane to modify a borrow
	 */
	protected void createUserModificationPane() {
		// VBox containing all the old borrow's informations
	    VBox borrowsOldInfos = new VBox(15);
	    
		Label borrowModificationLabel = new Label("BORROW MODIFICATION :");
	    borrowModificationLabel.setFont(new Font("Arial", 24));
	    borrowModificationLabel.setUnderline(true);
	    borrowModificationLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label oldBorrowLabel = new Label("Previous borrow's information :");
	    oldBorrowLabel.setFont(new Font("Arial", 16));
	    oldBorrowLabel.setUnderline(true);
	    oldBorrowLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox oldBorrowDateAndLabel = new HBox();
	    HBox oldBorrowReturnDateAndLabel = new HBox();
	    HBox oldBorrowEffectiveReturnDateAndLabel = new HBox();
	    
	    Label oldBorrowDate = new Label("- Date : ");
	    Label oldBorrowReturnDate = new Label("- Return date : ");
	    Label oldBorrowEffectiveReturnDate = new Label("- Effective return date : ");
	    
	    oldBorrowDate.setStyle("-fx-font-weight: bold;");
	    oldBorrowReturnDate.setStyle("-fx-font-weight: bold;");
	    oldBorrowEffectiveReturnDate.setStyle("-fx-font-weight: bold;");
	    
	    Label oldBorrowDateLabel = new Label("");
	    Label oldBorrowReturnDateLabel = new Label("");
	    Label oldBorrowEffectiveReturnDateLabel = new Label("");

	    // If the selected borrow changed we update the values of the labels
	    borrowsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	Borrow borrowToDisplay = newSelection;
	        	
	        	oldBorrowDateLabel.setText(borrowToDisplay.getDateBorrow());
	        	oldBorrowReturnDateLabel.setText(borrowToDisplay.getReturnDate());
	        	oldBorrowEffectiveReturnDateLabel.setText(borrowToDisplay.getEffectiveReturnDate());
	        }
	        else {
	        	oldBorrowDateLabel.setText("");
	        	oldBorrowReturnDateLabel.setText("");
	        	oldBorrowEffectiveReturnDateLabel.setText("");
	        }
	    });
	    
	    oldBorrowDateAndLabel.getChildren().addAll(oldBorrowDate, oldBorrowDateLabel);
	    oldBorrowReturnDateAndLabel.getChildren().addAll(oldBorrowReturnDate, oldBorrowReturnDateLabel);
	    oldBorrowEffectiveReturnDateAndLabel.getChildren().addAll(oldBorrowEffectiveReturnDate, oldBorrowEffectiveReturnDateLabel);
	    
	    // We add the node useful for the old borrow's information
	    borrowsOldInfos.getChildren().addAll(oldBorrowLabel, oldBorrowDateAndLabel, oldBorrowReturnDateAndLabel, oldBorrowEffectiveReturnDateAndLabel);
	    
	    // HBox containing all the new borrow's informations
	    HBox newUserInfosInput = new HBox(50);
	    
	    Label newBorrowLabel = new Label("New borrow's information :");
	    newBorrowLabel.setFont(new Font("Arial", 16));
	    newBorrowLabel.setUnderline(true);
	    newBorrowLabel.setStyle("-fx-font-weight: bold;");
	    
	    // Will allow to choose a date
	    DatePicker newDatePicker = new DatePicker();
	    DatePicker newReturnDatePicker = new DatePicker();
	    DatePicker newEffectiveReturnDatePicker = new DatePicker();
	    
	    // Will display which DatePicker referencing to which data
	    Tooltip newDateTooltip = new Tooltip("New date for the borrow");
	    newDateTooltip.setShowDelay(Duration.seconds(0));
	    Tooltip newReturnDateTooltip = new Tooltip("New return date for the borrow");
	    newReturnDateTooltip.setShowDelay(Duration.seconds(0));
	    Tooltip newEffectiveReturnDateTooltip = new Tooltip("New effective return date for the borrow");
	    newEffectiveReturnDateTooltip.setShowDelay(Duration.seconds(0));
	    
	    newDatePicker.setTooltip(newDateTooltip);
	    newReturnDatePicker.setTooltip(newReturnDateTooltip);
	    newEffectiveReturnDatePicker.setTooltip(newEffectiveReturnDateTooltip);
	    
	    // Button to modify a borrow
	    Button modifyBorrowButton = new Button("Modify");
	    modifyBorrowButton.setOnAction(new ModificationBorrowButtonHandler(oldBorrowDateLabel, oldBorrowReturnDateLabel, oldBorrowEffectiveReturnDateLabel, newDatePicker, newReturnDatePicker, newEffectiveReturnDatePicker, borrowsTable));

	    // Button to modify a borrow
	    Button resetDatePickerButton = new Button("Reset values");
	    resetDatePickerButton.setOnAction(new ResetDatePicker(newDatePicker, newReturnDatePicker, newEffectiveReturnDatePicker));

	    // HBox containing the modify and reset button
	    HBox buttonContainers = new HBox(10);
	    buttonContainers.getChildren().addAll(modifyBorrowButton, resetDatePickerButton);
	    
	    Label userModificationExplanation = new Label("Select at least one date in order to modify a borrow.");
	    
	    // If no borrow is selected, the button is disable
	    modifyBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getSelectionModel().getSelectedItems()));
	    
	    // We add the node useful for the new borrow's information
	    newUserInfosInput.getChildren().addAll(newDatePicker, newReturnDatePicker, newEffectiveReturnDatePicker, buttonContainers);
	    
	    // VBox containing all the old borrow's informations
	    VBox borrowsNewInfos = new VBox(15);
	    borrowsNewInfos.getChildren().addAll(newBorrowLabel, userModificationExplanation, newUserInfosInput);
	    
	    // VBox containing the nodes for the borrow modification
	    borrowsModificationVBox = new VBox(25);
	    borrowsModificationVBox.setPadding(new Insets(10, 10, 10, 10));
	    borrowsModificationVBox.getChildren().addAll(borrowModificationLabel, borrowsOldInfos, borrowsNewInfos);
	    borrowsModificationVBox.setAlignment(Pos.TOP_CENTER);
	}

}
