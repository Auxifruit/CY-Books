package presentation;

import abstraction.Borrow;
import abstraction.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The class containing the pane to display the user's profil
 */
public class UserProfil {
	private VBox usersProfilVBox;
	
	private Pagination usersBorrowTablePagination;
	public final static int ROWS_PER_PAGE = 10;
	
	private TableView<User> usersTable;
	private User u;
	
	private CheckBox lateBorrowCheckBox;
	private CheckBox onGoingBorrowCheckBox;
	private CheckBox finishedBorrowCheckBox;
	
	private TableView<Borrow> usersBorrowTable;
	private TableColumn<Borrow, Integer> idCol;
	private TableColumn<Borrow, String> booksISBNCol;
	private TableColumn<Borrow, String> borrowsDateCol;
	private TableColumn<Borrow, String> borrowsReturnDateCol;
	private TableColumn<Borrow, String> borrowsEffectiveReturnDateCol;
	private TableColumn<Borrow, Boolean> borrowsLate;
	private ObservableList<Borrow> data = FXCollections.observableArrayList();

	/**
     * Constructor of the UserCreation class
     * @param data the list containing all the informations
     * @param usersTable the table view displaying the informations
     * @param usersTablePagination the pagination of the table view
     */
    public UserProfil(TableView<User> usersTable) {
    	this.usersTable = usersTable;
    	lateBorrowCheckBox = new CheckBox("Late borrows");
        onGoingBorrowCheckBox = new CheckBox("On going borrows");
        finishedBorrowCheckBox = new CheckBox("Finished borrows");
    	initializeTable();
        createUserProfilPane();
    }
    
    /**
	 * Method to create a pane to display the user's profil
	 * @return the pane to display the user's profil
	 */
	protected void createUserProfilPane() {
		usersBorrowTablePagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
		usersBorrowTablePagination.setPageFactory(this::createPage);
		
		// VBox containing all the old user's informations
	    VBox usersInfosContainer = new VBox(15);
	    
		Label usersProfilLabel = new Label("USER'S PROFIL :");
	    usersProfilLabel.setFont(new Font("Arial", 24));
	    usersProfilLabel.setUnderline(true);
	    usersProfilLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label usersInfosLabel = new Label("User's information :");
	    usersInfosLabel.setUnderline(true);
	    usersInfosLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox usersIDAndValue = new HBox();
	    HBox usersFirsnameAndValue = new HBox();
	    HBox usersLastnameAndValue = new HBox();
	    HBox usersEmailAndValue = new HBox();
	    
	    Label usersID = new Label("� ID : ");
	    Label usersFirstname = new Label("� First name : ");
	    Label usersLastname = new Label("� Last name : ");
	    Label usersEmail = new Label("� E-mail : ");
	    
	    usersID.setStyle("-fx-font-weight: bold;");
	    usersFirstname.setStyle("-fx-font-weight: bold;");
	    usersLastname.setStyle("-fx-font-weight: bold;");
	    usersEmail.setStyle("-fx-font-weight: bold;");
	    
	    Label usersIDValue = new Label("");
	    Label usersFirstnameValue = new Label("");
	    Label usersLastnameValue = new Label("");
	    Label usersEmailValue = new Label("");

	    // If the selected user changed we update the values of the labels
	    usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	u = newSelection;
	        	
	        	usersIDValue.setText(String.valueOf(newSelection.getId()));
	           	usersFirstnameValue.setText(newSelection.getFirstname());
	           	usersLastnameValue.setText(newSelection.getLastname());
	          	usersEmailValue.setText(newSelection.getEmail());
	          	
	          	dataWithAllUsersBorrow();
	          	usersBorrowTable.setItems(data);
	          	
	          	changingNumberOfPages();
	        }
	    });
	    
      	usersIDAndValue.getChildren().addAll(usersID, usersIDValue);
	    usersFirsnameAndValue.getChildren().addAll(usersFirstname, usersFirstnameValue);
	    usersLastnameAndValue.getChildren().addAll(usersLastname, usersLastnameValue);
	    usersEmailAndValue.getChildren().addAll(usersEmail, usersEmailValue);
	    
	    // We add the node useful for the old user's information
	    usersInfosContainer.getChildren().addAll(usersInfosLabel, usersIDAndValue, usersFirsnameAndValue, usersLastnameAndValue, usersEmailAndValue);
	    
	    // We change the number of pages
	    changingNumberOfPages();
	    
	    // We update the tableView to display the wanted numbers of borrows starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
        
        Label usersBorrowLabel = new Label("User's borrow :");
        usersBorrowLabel.setUnderline(true);
        usersBorrowLabel.setStyle("-fx-font-weight: bold;");
	    
	    // If we check it we display the late borrows and if not we display all the borrows
	    ChangeListener<Boolean> borrowCheckChange = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				filterData();
		        changingNumberOfPages();
		        changeTableView(usersBorrowTablePagination.getCurrentPageIndex(), ROWS_PER_PAGE);
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
	    usersBorrowContainer.getChildren().addAll(usersBorrowLabel, checkBoxContainer, usersBorrowTablePagination);
	    
	    // VBox containing the nodes for the user modification
	    usersProfilVBox = new VBox(25);
	    usersProfilVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersProfilVBox.getChildren().addAll(usersProfilLabel, usersInfosContainer, usersBorrowContainer);
	    usersProfilVBox.setAlignment(Pos.TOP_CENTER);
	}
	
	/**
	 * Method to create pages
	 * @param pageIndex the index of the page
	 * @return the BorderPane containing the usersTable with the correct items 
	 */
	private Node createPage(int pageIndex) {
		// We calculate the first index and the last index a the page 
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        
        // We use a sublist and set it to the table view
        // It allows us to display only the users between the corresponding index
        usersBorrowTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(usersBorrowTable);
    }
	
	/**
	 * Method to change with a certain index and limit in order to display the right element
	 * @param index represent the index of the on going page
	 * @param limit correspond the limit of element to display
	 */
	private void changeTableView(int index, int limit) {

		// We calculate the starting index and check if we don't try to access an element outside the list limits
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, data.size());

        // Ensure that we don't take more elements than are available in filteredData
        int minIndex = Math.min(toIndex, data.size());
        
        // We use a SortedList to keep the order of the element of the subList
        SortedList<Borrow> sortedData = new SortedList<>(FXCollections.observableArrayList(data.subList(Math.min(fromIndex, minIndex), minIndex)));
        
        // We link the element to allow the update of usersTable
        sortedData.comparatorProperty().bind(usersBorrowTable.comparatorProperty());

        usersBorrowTable.setItems(sortedData);
    }
	
	/**
	 * Method to change the number of pages of a pagination depending of the size of our data
	 */
	private void changingNumberOfPages() {
		// We calculate the numbers of pages needed
	    int totalPage = (int) (Math.ceil(data.size() * 1.0 / ROWS_PER_PAGE));
	    
	    // We set the numbers of page for the pagination and go to the first page
	    usersBorrowTablePagination.setPageCount(totalPage);
	    usersBorrowTablePagination.setCurrentPageIndex(0);
	}

	/**
	 * Method to initialize the TableView userTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllUsersBorrow();
		
		usersBorrowTable = new TableView<>();
		
		// Column for the borrow's ID
		idCol = new TableColumn<>("Borrow's ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("id"));
				
		// Column for the book's ISBN
	    booksISBNCol = new TableColumn<>("Book's ISBN");
	    booksISBNCol.setMinWidth(100);
	    booksISBNCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("booksISBN"));

	    // Column for the borrow's date
	    borrowsDateCol = new TableColumn<>("Borrow's date");
	    borrowsDateCol.setMinWidth(200);
	    borrowsDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("borrowDate"));

	    // Column for the borrow's return date
	    borrowsReturnDateCol = new TableColumn<>("Borrow's return date");
	    borrowsReturnDateCol.setMinWidth(200);
	    borrowsReturnDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("returnDate"));
	    
	    // Column for the borrow's effective return date
	    borrowsEffectiveReturnDateCol = new TableColumn<>("Borrow's effective return date");
	    borrowsEffectiveReturnDateCol.setMinWidth(200);
	    borrowsEffectiveReturnDateCol.setCellValueFactory(new PropertyValueFactory<Borrow, String>("effectiveReturnDate"));
	    
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
	    usersBorrowTable.getColumns().addAll(idCol, booksISBNCol, borrowsDateCol, borrowsReturnDateCol, borrowsEffectiveReturnDateCol, borrowsLate);
	    
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
		
        if (u != null) {
        	
	    	// We add all the user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == u.getId()) {
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

		if(u != null) {
			// We add only the late borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == u.getId() && b.isLate()) {
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
		
        if (u != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == u.getId() && b.getEffectiveReturnDate().equals("")) {
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
		
        if (u != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == u.getId() && !(b.getEffectiveReturnDate().equals(""))) {
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
		
        if (u != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && b.getUsersID() == u.getId() && b.getEffectiveReturnDate().equals("") && b.isLate()) {
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
		
        if (u != null) {
        	
	    	// We add the on going user's borrows to our data
	    	for(Borrow b : Borrow.getAllBorrow()) {
	    		if(!(b.equals(null)) && (b.getUsersID() == u.getId() && (!(b.getEffectiveReturnDate().equals("")) && b.isLate()))) {
	    			data.add(b);
	    		}
			}
        }
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
	 * Getter to get the pagination for the table view of the user's borrows
	 * @return the pagination for the table view of the user's borrows
	 */
    public Pagination getUsersBorrowsTablePagination() {
        return usersBorrowTablePagination;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the user's profil
	 * @return the the VBox containing all the element for the user's profil
	 */
    public VBox getUsersProfilTableVBox() {
    	return usersProfilVBox;
    }
	
}
