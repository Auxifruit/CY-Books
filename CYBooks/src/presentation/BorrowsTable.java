package presentation;


import java.sql.SQLException;

import abstraction.Borrow;
import abstraction.db.DBConnect;
import control.borrowControl.DeleteBorrowButtonHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
 * The class of the users management application
 */
public class BorrowsTable {
	private VBox borrowsTableVBox;
	
	private Pagination borrowsTablePagination;
	public final static int ROWS_PER_PAGE = 15;
	
	private CheckBox lateBorrowCheckBox;
	private CheckBox onGoingBorrowCheckBox;
	private CheckBox finishedBorrowCheckBox;
	
	private TableView<Borrow> borrowsTable;
	private TableColumn<Borrow, Integer> idCol;
	private TableColumn<Borrow, Integer> usersIdCol;
	private TableColumn<Borrow, String> booksISBNCol;
	private TableColumn<Borrow, String> borrowsDateCol;
	private TableColumn<Borrow, String> borrowsReturnDateCol;
	private TableColumn<Borrow, String> borrowsEffectiveReturnDateCol;
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
	 * Method to create a pane for the users table
	 * @return the pane for the users table
	 */
	private void createBorrowsTablePane() {
		borrowsTablePagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
		borrowsTablePagination.setPageFactory(this::createPage);
		
	    Label labelUserTable = new Label("BORROWS TABLE :");
		labelUserTable.setFont(new Font("Arial", 24));
		labelUserTable.setUnderline(true);
		labelUserTable.setStyle("-fx-font-weight: bold;");
		
		// Button to delete an user
	    Button deleteBorrowButton = new Button("Delete borrow");
	    deleteBorrowButton.setOnAction(new DeleteBorrowButtonHandler(data, borrowsTable, borrowsTablePagination));
	    
	    // If no user is selected, the button is disable
	    deleteBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getSelectionModel().getSelectedItems()));
	    
	    // We change the number of pages
	    changingNumberOfPages();
	    
	    // We update the tableView to display 15 users starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
        
        // If we check it we display the late borrows and if not we display all the borrows
	    ChangeListener<Boolean> borrowCheckChange = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				filterData();
		        changingNumberOfPages();
		        changeTableView(borrowsTablePagination.getCurrentPageIndex(), ROWS_PER_PAGE);
			}
	    };
	    
	    // We link the ChangeListener with all of our CheckBoxes
	    lateBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    onGoingBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    finishedBorrowCheckBox.selectedProperty().addListener(borrowCheckChange);
	    
	    // The HBox containing all of our CheckBoxes
	    HBox checkBoxContainer = new HBox(10);
	    checkBoxContainer.getChildren().addAll(lateBorrowCheckBox, onGoingBorrowCheckBox, finishedBorrowCheckBox);
	    
	    // VBox containing the borrowsTable and the delete button
	 	VBox tableViewVBox = new VBox(20);
	    tableViewVBox.getChildren().addAll(borrowsTablePagination, deleteBorrowButton);
	    
	    // VBox containing the nodes for the users table
        borrowsTableVBox = new VBox(20);
        borrowsTableVBox.setPadding(new Insets(10, 10, 10, 10));
        borrowsTableVBox.getChildren().addAll(labelUserTable, checkBoxContainer, tableViewVBox);
        borrowsTableVBox.setAlignment(Pos.TOP_CENTER);
    }
	
	/**
	 * Method to create pages
	 * @param pageIndex the index of the page
	 * @return the BorderPane containing the borrowsTable with the correct items 
	 */
	private Node createPage(int pageIndex) {
		// We calculate the first index and the last index a the page 
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        
        // We use a sublist and set it to the table view
        // It allows us to display only the users between the corresponding index
        borrowsTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(borrowsTable);
    }
	
	/**
	 * Method to change with a certain index and limit in order to display the right element
	 * @param index represent the index of the current page
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
        
        // We link the element to allow the update of borrowsTable
        sortedData.comparatorProperty().bind(borrowsTable.comparatorProperty());

        borrowsTable.setItems(sortedData);
    }
	
	/**
	 * Method to change the number of pages of a pagination depending of the size of our data
	 */
	private void changingNumberOfPages() {
		// We calculate the numbers of pages needed
	    int totalPage = (int) (Math.ceil(data.size() * 1.0 / ROWS_PER_PAGE));
	    
	    // We set the numbers of page for the pagination and go to the first page
	    borrowsTablePagination.setPageCount(totalPage);
	    borrowsTablePagination.setCurrentPageIndex(0);
	}
	
	/**
	 * Method to initialize the TableView userTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllBorrow();
		
		borrowsTable = new TableView<>();
		
		// Column for the borrow's ID
		idCol = new TableColumn<>("Borrow's ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("id"));
		
		// Column for the user's ID
		usersIdCol = new TableColumn<>("User's ID");
		usersIdCol.setMinWidth(100);
		usersIdCol.setCellValueFactory(new PropertyValueFactory<Borrow, Integer>("usersID"));
		
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
	    borrowsTable.getColumns().addAll(idCol, usersIdCol, booksISBNCol, borrowsDateCol, borrowsReturnDateCol, borrowsEffectiveReturnDateCol,borrowsLate);
	    
	    // The columns take up all the table space
	    borrowsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of user and 
	    borrowsTable.setItems(data);
	}
	
	private void initializeData() {
		// We load the values of the borrows from the database
	    try {
	    	DBConnect.readBorrowsTable();
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
    public TableView<Borrow> getUsersTable() {
        return borrowsTable;
    }

    /**
	 * Getter to get the pagination for the table view of borrows
	 * @return the pagination for the table view of borrows
	 */
    public Pagination getBorrowsTablePagination() {
        return borrowsTablePagination;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the borrows table
	 * @return the the VBox containing all the element for the borrows table
	 */
    public VBox getBorrowsTableVBox() {
    	return borrowsTableVBox;
    }

}
