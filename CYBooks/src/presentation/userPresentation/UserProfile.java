package presentation.userPresentation;

import abstraction.Borrow;
import abstraction.User;
import control.TablePagination;
import control.borrowControl.DeleteBorrowFromUserProfileButtonHandler;

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

/**
 * The class containing the pane to display the user's profile
 */
public class UserProfile {
	private VBox usersProfileVBox;
	
	private TablePagination<Borrow> usersBorrowTablePagination;
	
	private User userToDisplay;
	
	private Label usersIDValue = new Label("");
	private Label usersFirstnameValue = new Label("");
	private Label usersLastnameValue = new Label("");
	private Label usersEmailValue = new Label("");
	private Label usersStatusValue = new Label("");
	
	private CheckBox lateBorrowCheckBox;
	private CheckBox onGoingBorrowCheckBox;
	private CheckBox finishedBorrowCheckBox;
	
	private TableView<Borrow> usersBorrowTable;
	private TableColumn<Borrow, Integer> idCol;
	private TableColumn<Borrow, String> booksIdentifierCol;
	private TableColumn<Borrow, String> borrowsDateCol;
	private TableColumn<Borrow, String> borrowsReturnDateCol;
	private TableColumn<Borrow, String> borrowsEffectiveReturnDateCol;
	private TableColumn<Borrow, Long> borrowsDurationCol;
	private TableColumn<Borrow, Boolean> borrowsLate;
	private ObservableList<Borrow> data = FXCollections.observableArrayList();

	/**
     * Constructor of the UserCreation class
     */
    public UserProfile() {
    	this.userToDisplay = null;
    	lateBorrowCheckBox = new CheckBox("Late borrows");
        onGoingBorrowCheckBox = new CheckBox("On going borrows");
        finishedBorrowCheckBox = new CheckBox("Finished borrows");
    	initializeTable();
        createUserProfilePane();
    }
    
    /**
     * Getter method to get the user to display
     * @return the class' user to display
     */
    public User getUserToDisplay() {
    	return userToDisplay;
    }
    
    /**
     * Setter method to set the user to display
     * @param user the new user to display
     */
    public void setUserToDisplay(User userToDisplay) {
    	this.userToDisplay = userToDisplay;
    }

	/**
	 * Getter to get the data containing the user's borrows
	 * @return the data containing the user's borrows
	 */
	public ObservableList<Borrow> getData() {
        return data;
    }

	/**
	 * Getter to get the table view displaying the user's borrows
	 * @return the table view displaying the user's borrows
	 */
    public TableView<Borrow> getUsersBorrowsTable() {
        return usersBorrowTable;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the user's profile
	 * @return the the VBox containing all the element for the user's profile
	 */
    public VBox getUsersProfileVBox() {
    	return usersProfileVBox;
    }
    
    /**
	 * Method to create a pane to display the user's profile
	 * @return the pane to display the user's profile
	 */
	protected void createUserProfilePane() {
		usersBorrowTablePagination = new TablePagination<Borrow>(usersBorrowTable, data);
		
		// VBox containing all the old user's informations
	    VBox usersInfosContainer = new VBox(15);
	    
		Label usersProfileLabel = new Label("USER'S PROFILE :");
	    usersProfileLabel.setFont(new Font("Arial", 24));
	    usersProfileLabel.setUnderline(true);
	    usersProfileLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label usersInfosLabel = new Label("User's information :");
	    usersInfosLabel.setFont(new Font("Arial", 16));
	    usersInfosLabel.setUnderline(true);
	    usersInfosLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox usersIDAndValue = new HBox();
	    HBox usersFirsnameAndValue = new HBox();
	    HBox usersLastnameAndValue = new HBox();
	    HBox usersEmailAndValue = new HBox();
	    HBox usersStatusAndValue = new HBox();
	    
	    Label usersID = new Label("- ID : ");
	    Label usersFirstname = new Label("- First name : ");
	    Label usersLastname = new Label("- Last name : ");
	    Label usersEmail = new Label("- E-mail : ");
	    Label usersStatus = new Label("- Status : ");
	    
	    usersID.setStyle("-fx-font-weight: bold;");
	    usersFirstname.setStyle("-fx-font-weight: bold;");
	    usersLastname.setStyle("-fx-font-weight: bold;");
	    usersEmail.setStyle("-fx-font-weight: bold;");
	    usersStatus.setStyle("-fx-font-weight: bold;");
	    
	    // We had in each HBox the info's name and its value
      	usersIDAndValue.getChildren().addAll(usersID, usersIDValue);
	    usersFirsnameAndValue.getChildren().addAll(usersFirstname, usersFirstnameValue);
	    usersLastnameAndValue.getChildren().addAll(usersLastname, usersLastnameValue);
	    usersEmailAndValue.getChildren().addAll(usersEmail, usersEmailValue);
	    usersStatusAndValue.getChildren().addAll(usersStatus, usersStatusValue);
	    
	    // We add the node useful for the old user's information
	    usersInfosContainer.getChildren().addAll(usersInfosLabel, usersIDAndValue, usersFirsnameAndValue, usersLastnameAndValue, usersEmailAndValue, usersStatusAndValue);
        
        Label usersBorrowLabel = new Label("User's borrow :");
        usersBorrowLabel.setFont(new Font("Arial", 16));
        usersBorrowLabel.setUnderline(true);
        usersBorrowLabel.setStyle("-fx-font-weight: bold;");
        
        // Button to delete a borrow
	    Button deleteBorrowButton = new Button("Delete borrow");
	    deleteBorrowButton.setOnAction(new DeleteBorrowFromUserProfileButtonHandler(this));
	    
	    // If no borrow is selected, the button is disable
	    deleteBorrowButton.disableProperty().bind(Bindings.isEmpty(usersBorrowTable.getSelectionModel().getSelectedItems()));
	    
	    // If we check it we display the late borrows and if not we display all the borrows
	    ChangeListener<Boolean> borrowCheckChange = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				filterData();
				usersBorrowTablePagination.updatePagination();
			}
	    };
	    
	    // We link the ChangeListener with all of our CheckBoxes
	    lateBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    onGoingBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    finishedBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    
	    // The HBox containing all of our CheckBoxes
	    HBox checkBoxContainer = new HBox(10);
	    checkBoxContainer.getChildren().addAll(lateBorrowCheckBox, onGoingBorrowCheckBox, finishedBorrowCheckBox);
	    
	    // The VBox containing all we need for the user's borrow
	    VBox usersBorrowContainer = new VBox(10);
	    usersBorrowContainer.getChildren().addAll(usersBorrowLabel, checkBoxContainer, usersBorrowTablePagination.getPagination(), deleteBorrowButton);
	    
	    // VBox containing the nodes for the user modification
	    usersProfileVBox = new VBox(25);
	    usersProfileVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersProfileVBox.getChildren().addAll(usersProfileLabel, usersInfosContainer, usersBorrowContainer);
	    usersProfileVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to initialize the TableView usersBorrowTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllUsersBorrow();
		
		usersBorrowTable = new TableView<>();
		usersBorrowTable.setPlaceholder(new Label("No borrows to display"));
		
		// Column for the borrow's ID
		idCol = new TableColumn<>("Borrow's ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("id"));
				
		// Column for the book's Identifier
	    booksIdentifierCol = new TableColumn<>("Book's Identifier");
	    booksIdentifierCol.setMinWidth(150);
	    booksIdentifierCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("booksIdentifier"));

	    // Column for the borrow's date
	    borrowsDateCol = new TableColumn<>("Borrow's date");
	    borrowsDateCol.setMinWidth(100);
	    borrowsDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("borrowDate"));

	    // Column for the borrow's return date
	    borrowsReturnDateCol = new TableColumn<>("Borrow's return date");
	    borrowsReturnDateCol.setMinWidth(100);
	    borrowsReturnDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("returnDate"));
	    
	    // Column for the borrow's effective return date
	    borrowsEffectiveReturnDateCol = new TableColumn<>("Borrow's effective return date");
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
	    usersBorrowTable.getColumns().addAll(idCol, booksIdentifierCol, borrowsDateCol, borrowsReturnDateCol, borrowsEffectiveReturnDateCol, borrowsDurationCol, borrowsLate);
	    
	    // The columns take up all the table space
	    usersBorrowTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of user and 
	    usersBorrowTable.setItems(data);
	}
	
	/**
	 * Method to filter the user's borrows
	 */
	private void filterData() {
	    boolean late = lateBorrowCheckBox.isSelected();
	    boolean onGoing = onGoingBorrowCheckBox.isSelected();
	    boolean finished = finishedBorrowCheckBox.isSelected();

	    if(late == true && onGoing == false && finished == false) {
	    	dataWithUsersLateBorrows();
	    }
	    else if(late == false && onGoing == true && finished == false) {
	    	dataWithUsersOnGoingBorrows();
	    }
	    else if(late == false && onGoing == false && finished == true) {
	    	dataWithUsersFinishedBorrows();
	    }
	    else if(late == true && onGoing == true && finished == false) {
	    	dataWithUsersLateOnGoingBorrows();
	    }
	    else if(late == true && onGoing == false && finished == true) {
	    	dataWithUsersLateFinishedBorrows();
	    }
	    else if(late == true && onGoing == true && finished == true) {
	    	dataWithUsersLateBorrows();
	    }
	    else {
	    	dataWithAllUsersBorrow();
	    }
	} 
	
	/**
	 * Method to initialize the ObservableList data with all the user's borrows
	 */
	private void dataWithAllUsersBorrow() {
		// We reset the data
		data.clear();
		
        if (userToDisplay != null) {
        	
	    	// We add all the user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId()) {
	    			data.add(b);
	    		}
			}
        }
	}
	
	/**
	 * Method to initialize the ObservableList data with the late user's borrow
	 */
	private void dataWithUsersLateBorrows() {
		// We reset the data
		data.clear();

		if(userToDisplay != null) {
			// We add only the late borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId() && b.isLate()) {
	    			data.add(b);
	    		}
			}
		}
	}
	
	/**
	 * Method to initialize the ObservableList data with the on going user's borrow
	 */
	private void dataWithUsersOnGoingBorrows() {
		// We reset the data
		data.clear();
		
        if (userToDisplay != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId() && b.getEffectiveReturnDate().equals("")) {
	    			data.add(b);
	    		}
			}
        }
	}
	
	/**
	 * Method to initialize the ObservableList data with the finished user's borrow
	 */
	private void dataWithUsersFinishedBorrows() {
		// We reset the data
		data.clear();
		
        if (userToDisplay != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId() && !(b.getEffectiveReturnDate().equals(""))) {
	    			data.add(b);
	    		}
			}
        }
	}
	
	/**
	 * Method to initialize the ObservableList data with the late and on going user's borrow
	 */
	private void dataWithUsersLateOnGoingBorrows() {
		// We reset the data
		data.clear();
		
        if (userToDisplay != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId() && b.getEffectiveReturnDate().equals("") && b.isLate()) {
	    			b.checkBorrowLate();
	    			data.add(b);
	    		}
			}
        }
	}
	
	/**
	 * Method to initialize the ObservableList data with the late and finished user's borrow
	 */
	private void dataWithUsersLateFinishedBorrows() {
		// We reset the data
		data.clear();
		
        if (userToDisplay != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && (b.getUsersID() == userToDisplay.getId() && (!(b.getEffectiveReturnDate().equals("")) && b.isLate()))) {
	    			data.add(b);
	    		}
			}
        }
	}
	
	/**
	 * Method to update the ObservableList data
	 */
	public void updateData() {
		if(userToDisplay != null) {
			//We reset the checkBoxes
			lateBorrowCheckBox.setSelected(false);
			onGoingBorrowCheckBox.setSelected(false);
			finishedBorrowCheckBox.setSelected(false);
			
			// To update the labels
        	updateInfo();
			
			// We clear the data
			data.clear();
			
			for(Borrow b : Borrow.getAllBorrow()) {
		    	if(!(b.equals(null)) && b.getUsersID() == userToDisplay.getId()) {
		    		data.add(b);
		    	}
		    }
			
			// We set the new data
			getUsersBorrowsTable().setItems(data);
			
			// We update the pagination to display the right elements
			usersBorrowTablePagination.updatePagination();
		}
	}
	
	/**
	 * Method to update the label with an user's informations
	 */
	public void updateInfo() {
		usersIDValue.setText(String.valueOf(userToDisplay.getId()));
       	usersFirstnameValue.setText(userToDisplay.getFirstname());
       	usersLastnameValue.setText(userToDisplay.getLastname());
      	usersEmailValue.setText(userToDisplay.getEmail());
      	usersStatusValue.setText(userToDisplay.getStatus());
	}
	
}
