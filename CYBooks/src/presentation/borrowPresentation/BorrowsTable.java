package presentation.borrowPresentation;

import abstraction.Borrow;
import abstraction.db.DataBaseBorrow;
import control.borrowControl.DeleteBorrowFromBorrowsTableButtonHandler;
import control.borrowControl.ReturnBorrowButtonHandler;
import control.borrowControl.CancelBorrowButtonHandler;
import control.TablePagination;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.SQLException;

/**
 * The class containing the pane and the TableView to display all the borrows
 */
public class BorrowsTable {
	private VBox borrowsTableVBox;
	
	private VBox borrowTableButtonsCheckBoxes;
		
	private TablePagination<Borrow> borrowTablePagination;
	
	private CheckBox lateBorrowCheckBox;
	private CheckBox onGoingBorrowCheckBox;
	private CheckBox finishedBorrowCheckBox;
	
	private TableView<Borrow> borrowsTable;
	private TableColumn<Borrow, Integer> idCol;
	private TableColumn<Borrow, Integer> usersIdCol;
	private TableColumn<Borrow, String> booksIdentifierCol;
	private TableColumn<Borrow, String> borrowsDateCol;
	private TableColumn<Borrow, String> borrowsReturnDateCol;
	private TableColumn<Borrow, String> borrowsEffectiveReturnDateCol;
	private TableColumn<Borrow, Long> borrowsDurationCol;
	private TableColumn<Borrow, Boolean> borrowsLate;
	private final ObservableList<Borrow> data = FXCollections.observableArrayList();
	
	/**
	 * Constructor of the BorrowsTable class
	 */
	public BorrowsTable() {
		lateBorrowCheckBox = new CheckBox("Late borrows");
        onGoingBorrowCheckBox = new CheckBox("On going borrows");
        finishedBorrowCheckBox = new CheckBox("Finished borrows");
        
        initializeData();
        initializeTable();
        createBorrowsTablePane();
    }
	
	/**
	 * Getter to get the data containing the borrows
	 * @return the data containing the borrows
	 */
	public ObservableList<Borrow> getData() {
        return data;
    }

	/**
	 * Getter to get the table view displaying the borrows
	 * @return the table view displaying the borrows
	 */
    public TableView<Borrow> getBorrowsTable() {
        return borrowsTable;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the borrows table
	 * @return the the VBox containing all the element for the borrows table
	 */
    public VBox getBorrowsTableVBox() {
    	return borrowsTableVBox;
    }

    /**
     * Getter for the late borrow filter CheckBox
     * @return the CheckBox for filter the late borrow
     */
	public CheckBox getLateBorrowCheckBox() {
		return lateBorrowCheckBox;
	}
	
	/**
     * Getter for the on going borrow filter CheckBox
     * @return the CheckBox for filter the on going borrow
     */
	public CheckBox getOnGoingBorrowCheckBox() {
		return onGoingBorrowCheckBox;
	}

	/**
     * Getter for the finished borrow filter CheckBox
     * @return the CheckBox for filter the finished borrow
     */
	public CheckBox getFinishedBorrowCheckBox() {
		return finishedBorrowCheckBox;
	}

	/**
	 * Method to create a pane for the users table
	 * @return the pane for the users table
	 */
	private void createBorrowsTablePane() {
		borrowTablePagination = new TablePagination<Borrow>(borrowsTable, data);
		
	    Label labelUserTable = new Label("BORROWS TABLE :");
		labelUserTable.setFont(new Font("Arial", 24));
		labelUserTable.setUnderline(true);
		labelUserTable.setStyle("-fx-font-weight: bold;");
		
		// Button to delete a borrow
	    Button deleteBorrowButton = new Button("Delete borrow");
	    deleteBorrowButton.setOnAction(new DeleteBorrowFromBorrowsTableButtonHandler(this));
	    // If no borrow is selected, the button is disable
	    deleteBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getSelectionModel().getSelectedItems()));
	    
	    // Button to validate the borrow's return
	    Button returnBorrowButton = new Button("Validate borrow's return");
	    returnBorrowButton.setOnAction(new ReturnBorrowButtonHandler(borrowsTable));
	    // If no borrow is selected, the button is disable
	    returnBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getSelectionModel().getSelectedItems()));
	    
	    // Button to cancel the borrow's return
	    Button cancelReturnBorrowButton = new Button("Cancel borrow's return");
	    cancelReturnBorrowButton.setOnAction(new CancelBorrowButtonHandler(borrowsTable));
	    // If no borrow is selected, the button is disable
	    cancelReturnBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getSelectionModel().getSelectedItems()));
        
        // If we check it we display the late borrows and if not we display all the borrows
	    ChangeListener<Boolean> borrowCheckChange = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				filterData();
				borrowTablePagination.updatePagination();
			}
	    };
	    
	    // We link the ChangeListener with all of our CheckBoxes
	    lateBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    onGoingBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    finishedBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    
	    // The HBox containing all of our CheckBoxes
	    HBox checkBoxContainer = new HBox(10);
	    checkBoxContainer.getChildren().addAll(lateBorrowCheckBox, onGoingBorrowCheckBox, finishedBorrowCheckBox);
	    
	    // The HBox containing the delete, problem and return button
	    HBox buttonsContainer = new HBox(10);
	    buttonsContainer.getChildren().addAll(deleteBorrowButton, returnBorrowButton, cancelReturnBorrowButton);
	    
	    // VBox containing the borrowsTable and the buttons
	 	VBox tableViewVBox = new VBox(20);
	    tableViewVBox.getChildren().addAll(borrowTablePagination.getPagination(), buttonsContainer);
	    
	    // VBox containing the borrowsTable, the buttons and the checkboxes
	    borrowTableButtonsCheckBoxes = new VBox(20);
	    borrowTableButtonsCheckBoxes.getChildren().addAll(checkBoxContainer, tableViewVBox);
	    
	    // VBox containing the nodes for the users table
        borrowsTableVBox = new VBox(20);
        borrowsTableVBox.setPadding(new Insets(10, 10, 10, 10));
        borrowsTableVBox.getChildren().addAll(labelUserTable, borrowTableButtonsCheckBoxes);
        borrowsTableVBox.setAlignment(Pos.TOP_CENTER);
    }

	/**
	 * Method to initialize the TableView userTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllBorrow();
		
		borrowsTable = new TableView<>();
		borrowsTable.setPlaceholder(new Label("No borrows to display"));
		
		// Column for the borrow's ID
		idCol = new TableColumn<>("Borrow's ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("id"));
		
		// Column for the user's ID
		usersIdCol = new TableColumn<>("User's ID");
		usersIdCol.setMinWidth(100);
		usersIdCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("usersID"));
		
		// Column for the book's Identifier
	    booksIdentifierCol = new TableColumn<>("Book's Identifier");
	    booksIdentifierCol.setMinWidth(150);
	    booksIdentifierCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("booksIdentifier"));

	    // Column for the borrow's date
	    borrowsDateCol = new TableColumn<>("Date");
	    borrowsDateCol.setMinWidth(100);
	    borrowsDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("borrowDate"));

	    // Column for the borrow's return date
	    borrowsReturnDateCol = new TableColumn<>("Return date");
	    borrowsReturnDateCol.setMinWidth(100);
	    borrowsReturnDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("returnDate"));
	    
	    // Column for the borrow's effective return date
	    borrowsEffectiveReturnDateCol = new TableColumn<>("Effective return date");
	    borrowsEffectiveReturnDateCol.setMinWidth(100);
	    borrowsEffectiveReturnDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("effectiveReturnDate"));
	   
	    // Column for the borrow's effective return date
	    borrowsDurationCol = new TableColumn<>("Duration in days");
	    borrowsDurationCol.setMinWidth(100);
	    borrowsDurationCol.setCellValueFactory(new PropertyValueFactory<Borrow, Long>("duration"));
	    
	    // Column for the borrow's statue
	    borrowsLate = new TableColumn<>("Late ?");
	    // Get the statue for each element
	    borrowsLate.setCellValueFactory(cellData -> {
            SimpleBooleanProperty activeProperty = new SimpleBooleanProperty(cellData.getValue().isLate());
            activeProperty.addListener((observable, oldValue, newValue) -> {
                cellData.getValue().setLate(newValue);
            });
            return activeProperty;
        });
	    
	    // for the column for the borrow's statue we use check boxes to display the values
	    borrowsLate.setCellFactory(column -> {
            return new CheckBoxTableCell<>();
        });
	    borrowsLate.setMinWidth(50);
	    borrowsLate.setCellValueFactory(new PropertyValueFactory<Borrow, Boolean>("late"));

	    // We add the column to our table
	    borrowsTable.getColumns().addAll(idCol, usersIdCol, booksIdentifierCol, borrowsDateCol, borrowsReturnDateCol, borrowsEffectiveReturnDateCol, borrowsDurationCol, borrowsLate);
	    
	    // The columns take up all the table space
	    borrowsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of user and 
	    borrowsTable.setItems(data);
	}
	
	/**
	 * Method to initialize the data with all the borrows
	 */
	private void initializeData() {
		// We load the values of the borrows from the database
	    try {
	    	DataBaseBorrow.readBorrowsTable();
	    } catch (SQLException e) {
			System.err.println("Error BDD. (dataWithAllValues)");
		}
	    Borrow.checkAllborrowsLate();
	}
	
	/**
	 * Method to filter the user's borrows
	 */
	private void filterData() {
	    boolean late = lateBorrowCheckBox.isSelected();
	    boolean onGoing = onGoingBorrowCheckBox.isSelected();
	    boolean finished = finishedBorrowCheckBox.isSelected();

	    if(late == true && onGoing == false && finished == false) {
	    	dataWithLateBorrows();
	    }
	    else if(late == false && onGoing == true && finished == false) {
	    	dataWithOnGoingBorrows();
	    }
	    else if(late == false && onGoing == false && finished == true) {
	    	dataWithFinishedBorrows();
	    }
	    else if(late == true && onGoing == true && finished == false) {
	    	dataWithLateOnGoingBorrows();
	    }
	    else if(late == true && onGoing == false && finished == true) {
	    	dataWithLateFinishedBorrows();
	    }
	    else if(late == true && onGoing == true && finished == true) {
	    	dataWithLateBorrows();
	    }
	    else {
	    	dataWithAllBorrow();
	    }
	} 
	
	/**
	 * Method to initialize the ObservableList data with all the user's borrows
	 */
	private void dataWithAllBorrow() {
		// We reset the data
		data.clear();
	    	
    	// We add all the user's borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null))) {
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the late user's borrow
	 */
	private void dataWithLateBorrows() {
		// We reset the data
		data.clear();

		// We add only the late borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null)) && b.isLate()) {
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the on going user's borrow
	 */
	private void dataWithOnGoingBorrows() {
		// We reset the data
		data.clear();
		        	
    	// We add the on going user's borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null)) && b.getEffectiveReturnDate().equals("")) {
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the finished user's borrow
	 */
	private void dataWithFinishedBorrows() {
		// We reset the data
		data.clear();
		
    	// We add the on going user's borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null)) && !(b.getEffectiveReturnDate().equals(""))) {
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the late and on going user's borrow
	 */
	private void dataWithLateOnGoingBorrows() {
		// We reset the data
		data.clear();
		
    	// We add the on going user's borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null)) && b.getEffectiveReturnDate().equals("") && b.isLate()) {
    			b.checkBorrowLate();
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the late and finished user's borrow
	 */
	private void dataWithLateFinishedBorrows() {
		// We reset the data
		data.clear();
		
    	// We add the on going user's borrows to our data
    	for(Borrow b : Borrow.getAllBorrow()) {
    		if(!(b.equals(null)) && (!(b.getEffectiveReturnDate().equals("")) && b.isLate())) {
    			data.add(b);
    		}
		}
	}
	
	/**
	 * Method to update the ObservableList data
	 */
	public void updateData() {
		//We reset the checkBoxes
		lateBorrowCheckBox.setSelected(false);
		onGoingBorrowCheckBox.setSelected(false);
		finishedBorrowCheckBox.setSelected(false);
		
		// We clear the data
		data.clear();
		
		for(Borrow b : Borrow.getAllBorrow()) {
	    	if(!(b.equals(null))) {
	    		data.add(b);
	    	}
	    }
		
		// We set the new data
		getBorrowsTable().setItems(data);
		
		// We update the pagination to display the right elements
		borrowTablePagination.updatePagination();
	}
	
}
