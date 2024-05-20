package presentation;

import java.sql.SQLException;

import abstraction.User;
import abstraction.db.DBConnect;
import control.userControl.DeleteUserButtonHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UsersTable {
	private VBox usersTableVBox;
	
	private Pagination usersTablePagination;
	public final static int ROWS_PER_PAGE = 15;
	
	private TableView<User> usersTable;
	private TableColumn<User, Integer> idCol;
	private TableColumn<User, String> firstNameCol;
	private TableColumn<User, String> lastNameCol;
	private TableColumn<User, String> emailCol;
	private final ObservableList<User> data = FXCollections.observableArrayList();
	private FilteredList<User> filteredData;

    public UsersTable() {
    	initializeData();
        initializeTable();
        createUsersTablePane();
    }

	/**
	 * Method to create a pane for the users table
	 * @return the pane for the users table
	 */
	protected void createUsersTablePane() {
		usersTablePagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
		usersTablePagination.setPageFactory(this::createPage);
	    
		// VBox containing all the node for the users table expect the main label
		VBox searchTableVBox = new VBox(20);
		
	    Label labelUserTable = new Label("USERS TABLE :");
		labelUserTable.setFont(new Font("Arial", 24));
		labelUserTable.setUnderline(true);
		labelUserTable.setStyle("-fx-font-weight: bold;");
	    
	    // Button to delete an user
	    Button deleteUserButton = new Button("Delete user");
	    deleteUserButton.setOnAction(new DeleteUserButtonHandler(data, usersTable, usersTablePagination));
	    
	    // If no user is selected, the button is disable
	    deleteUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // Allow the search in the table view
	    TextField filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    // At start all data are correct
	    filteredData = new FilteredList<>(data, b -> true);
	    
	    // If we change the value of the search
	    filteredField.textProperty().addListener((observalble, oldValue, newValue) -> {
	    	// Used to check if the input is the same as some values of one use
	    	filteredData.setPredicate(user ->  {
	    		if(newValue == null || newValue.isEmpty()) {
	    			return true;
	    		}
	    		
	    		// Splitting the search string into parts
	            String[] parts = newValue.toLowerCase().split(" ");
	            
	            // Check if any part matches ID, first name, last name, or email
	            for (String part : parts) {
	                if (String.valueOf(user.getId()).toLowerCase().contains(part)) {
	                    continue;
	                } else if (user.getFirstname().toLowerCase().contains(part)) {
	                    continue;
	                } else if (user.getLastname().toLowerCase().contains(part)) {
	                    continue;
	                } else if (user.getEmail().toLowerCase().contains(part)) {
	                    continue;
	                } else {
	                    // If no part matches, return false
	                    return false;
	                }
	            }
	            
	            // If all parts match, return true
	            return true;
	    		
	    	});
	    	changeTableView(usersTablePagination.getCurrentPageIndex(), ROWS_PER_PAGE);
	    });
	    
	    searchTableVBox.getChildren().addAll(filteredField, usersTablePagination, deleteUserButton);
	    
	    // We update the number of pages necessary to display all the data
	    changingNumberOfPages();
	    
	    // We update the tableView to display 15 users starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
        
        // If we go to page n, it will display 15 users starting from index n
        usersTablePagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
	    
	    // VBox containing the nodes for the users table
	    usersTableVBox = new VBox(20);
	    usersTableVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersTableVBox.getChildren().addAll(labelUserTable, searchTableVBox);
	    usersTableVBox.setAlignment(Pos.TOP_CENTER);
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
        usersTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(usersTable);
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
        int minIndex = Math.min(toIndex, filteredData.size());
        
        // We use a SortedList to keep the order of the element of the subList
        SortedList<User> sortedData = new SortedList<>(FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        
        // We link the element to allow the update of usersTable
        sortedData.comparatorProperty().bind(usersTable.comparatorProperty());

        usersTable.setItems(sortedData);
    }
	
	/**
	 * Method to change the number of pages of a pagination depending of the size of our data
	 */
	private void changingNumberOfPages() {
		// We calculate the numbers of pages needed
	    int totalPage = (int) (Math.ceil(data.size() * 1.0 / ROWS_PER_PAGE));
	    
	    // We set the numbers of page for the pagination and go to the first page
	    usersTablePagination.setPageCount(totalPage);
	    usersTablePagination.setCurrentPageIndex(0);
	}

	/**
	 * Method to initialize the TableView userTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllValues();
		
		usersTable = new TableView<>();
		usersTable.setPlaceholder(new Label("No rows to display"));

		// Column for the user's ID
		idCol = new TableColumn<>("ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
		
		// Column for the user's firstname
	    firstNameCol = new TableColumn<>("First name");
	    firstNameCol.setMinWidth(100);
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));

	    // Column for the user's lastname
	    lastNameCol = new TableColumn<>("Last name");
	    lastNameCol.setMinWidth(100);
	    lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));

	    // Column for the user's e-mail
	    emailCol = new TableColumn<>("E-mail");
	    emailCol.setMinWidth(200);
	    emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

	    // We add the column to our table
	    usersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol);
	    
	    // The columns take up all the table space
	    usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of user and 
	    usersTable.setItems(data);
	}
	
	private void initializeData() {
		// We load the values of the users from the database
	    try {
	    	DBConnect.readUsersTable();
	    } catch (SQLException e) {
			System.err.println("Error BDD. (dataWithAllValues)");
		}
	}
	
	/**
	 * Method to initialize the ObservableList data
	 */
	private void dataWithAllValues() {
		// We reset the data
		data.clear();
	    	
    	// We add the users to our data
    	for(User u : User.getAllUser()) {
    		if(!(u.equals(null))) {
    			data.add(u);
    		}
		}
	}

	/**
	 * Getter to get the data containing the users
	 * @return the data containing the users
	 */
    public ObservableList<User> getData() {
        return data;
    }

    /**
	 * Getter to get the table view displaying the users
	 * @return the table view displaying the users
	 */
    public TableView<User> getUsersTable() {
        return usersTable;
    }

    /**
	 * Getter to get the pagination for the table view of users
	 * @return the pagination for the table view of users
	 */
    public Pagination getUsersTablePagination() {
        return usersTablePagination;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the user table
	 * @return the the VBox containing all the element for the user table
	 */
    public VBox getUsersTableVBox() {
    	return usersTableVBox;
    }
}
