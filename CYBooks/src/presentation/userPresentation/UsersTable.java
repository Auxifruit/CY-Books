package presentation.userPresentation;

import abstraction.Status;
import abstraction.User;
import abstraction.db.DataBaseUser;
import control.userControl.DeleteUserButtonHandler;
import control.TablePagination;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.SQLException;

/**
 * The class containing the pane and the TableView to display all the users
 */
public class UsersTable {
	private VBox usersTableVBox;
	
	private TablePagination<User> usersTablePagination;
	
	private TableView<User> usersTable;
	private TableColumn<User, Integer> idCol;
	private TableColumn<User, String> firstNameCol;
	private TableColumn<User, String> lastNameCol;
	private TableColumn<User, String> emailCol;
	private TableColumn<User, Status> statusCol;
	private final ObservableList<User> data = FXCollections.observableArrayList();
	private FilteredList<User> filteredData;
	private TextField filteredField;

	/**
	 * Constructor for the UsersTable class
	 */
    public UsersTable() {
    	initializeData();
        initializeTable();
        createUsersTablePane();
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
	 * Getter to get the VBox containing all the element for the user table
	 * @return the the VBox containing all the element for the user table
	 */
    public VBox getUsersTableVBox() {
    	return usersTableVBox;
    }
    
	/**
	 * Method to create a pane for the users table
	 * @return the pane for the users table
	 */
	protected void createUsersTablePane() {	    
		// VBox containing all the node for the users table expect the main label
		VBox searchTableVBox = new VBox(20);
		
	    Label labelUserTable = new Label("USERS TABLE :");
		labelUserTable.setFont(new Font("Arial", 24));
		labelUserTable.setUnderline(true);
		labelUserTable.setStyle("-fx-font-weight: bold;");
	    
	    // Button to delete an user
	    Button deleteUserButton = new Button("Delete user");
	    deleteUserButton.setOnAction(new DeleteUserButtonHandler(this));
	    
	    // If no user is selected, the button is disable
	    deleteUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // Allow the search in the table view
	    filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    	    
	    // At start all data are correct
	    filteredData = new FilteredList<>(data, b -> true);

	    usersTablePagination = new TablePagination<User>(usersTable, data, filteredData);

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
	    	usersTablePagination.updatePaginationSearch();
	    });
	    
	    searchTableVBox.getChildren().addAll(filteredField, usersTablePagination.getPagination(), deleteUserButton);
	    
	    usersTablePagination.updatePaginationSearch();
	    
	    // VBox containing the nodes for the users table
	    usersTableVBox = new VBox(20);
	    usersTableVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersTableVBox.getChildren().addAll(labelUserTable, searchTableVBox);
	    usersTableVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to initialize the TableView userTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllValues();
		
		usersTable = new TableView<>();
		usersTable.setPlaceholder(new Label("No users to display"));

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
	    
	    // Column for the user's status
	    statusCol = new TableColumn<>("Status");
	    statusCol.setMinWidth(50);
	    statusCol.setCellValueFactory(new PropertyValueFactory<User, Status>("status"));

	    // We add the column to our table
	    usersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, statusCol);
	    
	    // The columns take up all the table space
	    usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of user and 
	    usersTable.setItems(data);
	}
	
	/**
	 * Method to initialize all the user's informations
	 */
	private void initializeData() {
		// We load the values of the users from the database
	    try {
	    	DataBaseUser.readUsersTable();
	    	User.checkAllUserStatus();
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
		
		// We check all user' status
        User.checkAllUserStatus();
	    	
    	// We add the users to our data
    	for(User u : User.getAllUser()) {
    		if(!(u.equals(null))) {
    			data.add(u);
    		}
		}
	}
	
	/**
   	 * Method to update the ObservableList data
   	 */
   	public void updateData() {
   		// We reset the TextField for the search
        filteredField.clear();
        
        // We check if one user' status changed
        User.checkAllUserStatus();
        
   		// We clear the data
   		data.clear();
   		
   		for(User u : User.getAllUser()) {
   	    	if(!(u.equals(null))) {
   	    		data.add(u);
   	    	}
   	    }
   		
   		// We set the new data
   		getUsersTable().setItems(data);
   		
   		usersTablePagination.updatePaginationSearch();
        
   	}
}
