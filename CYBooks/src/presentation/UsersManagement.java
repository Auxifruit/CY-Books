package presentation;

import java.io.IOException;
import abstraction.User2;
import abstraction.UserFile;
import controle.CreateUserButtonHandler;
import controle.ModificationUserButtonHandler;
import controle.DeleteUserButtonHandler;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * The class of the users management application
 */
public class UsersManagement extends Application {
	private Pane usersTablePane;
	private Pane usersCreationPane;
	private Pane usersModificationPane;
	
	private BorderPane usersBorderPane;
	
	private TableView<User2> usersTable = new TableView<>();
	private TableColumn<User2, Integer> idCol;
	private TableColumn<User2, String> firstNameCol;
	private TableColumn<User2, String> lastNameCol;
	private TableColumn<User2, String> emailCol;
	private final ObservableList<User2> data = FXCollections.observableArrayList(User2.getAllUser());
	
	/**
	 * Method to start the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// HBox containing all the button to change scene
		HBox hboxButtonChangeScene = new HBox(10);
		
		// Buttons to change scene
		Button goToUsersTableButton = new Button("Users table");
		goToUsersTableButton.setOnAction(e -> {
			usersBorderPane.setCenter(usersTablePane);
	    });
		
		Button goToUserCreationButton = new Button("Create user");
	    goToUserCreationButton.setOnAction(e -> {
	    	usersBorderPane.setCenter(usersCreationPane);
	    });
	    
	    Button goToUserModificationButton = new Button("Modify user");
	    goToUserModificationButton.setOnAction(e -> {
	    	usersBorderPane.setCenter(usersModificationPane);
	    });

	    hboxButtonChangeScene.getChildren().addAll(goToUsersTableButton, goToUserCreationButton, goToUserModificationButton);
	    
		// Users Table pane
	    usersTablePane = new Pane();
		
	    // We initialize the values of all the columns
	    initializeCol();
		
		stage.setTitle("Users table view");
	    stage.setWidth(800);
	    
	    final Label label = new Label("Users");
	    label.setFont(new Font("Arial", 20));
	    
	    // Button to delete an user
	    Button deleteUserButton = new Button("Delete user");
	    deleteUserButton.setOnAction(new DeleteUserButtonHandler(data, usersTable));
	    
	    // If no user is selected, the button is disable
	    deleteUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // Allow the search in the table view
	    TextField filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    FilteredList<User2> filteredData = new FilteredList<>(data, b -> true);
	    filteredField.textProperty().addListener((observalble, oldValue, newValue) -> {
	    	filteredData.setPredicate(user ->  {
	    		if(newValue == null || newValue.isEmpty()) {
	    			return true;
	    		}
	    		
	    		// The string we entered
	    		String lowerCaseFilter = newValue.toLowerCase();
	    		
	    		// We check if it's correspond to the user's ID
	    		if(String.valueOf(user.getId()).toLowerCase().indexOf(lowerCaseFilter)  != -1) {
	    			return true;
	    		}
	    		// We check if it's correspond to the user's firstname
	    		else if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		// We check if it's correspond to the user's lastname
	    		else if(user.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		// We check if it's correspond to the user's e-mail
	    		else if(user.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		// It's correspond to nothing
	    		else {
	    			return false;
	    		}
	    		
	    	});
	    });
	    
	    // We get the result in a sorted list, compare it the the table and update the values
	    SortedList<User2> sortedData = new SortedList<>(filteredData);
	    sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
	    usersTable.setItems(sortedData);
	    
	    // VBox containing the nodes for the users table
	    VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(label, filteredField, usersTable, deleteUserButton);
	    
	    usersTablePane.getChildren().add(vbox);
	    
	    // Users Creation pane
	    usersCreationPane = new VBox();
	    
	    // HBox containing the input for the user's information
	    HBox userInfoInput = new HBox(10);
	    
	    TextField firstnameText = new TextField();
	    TextField lastnameText = new TextField();
	    TextField emailText = new TextField();
	    Label userCreationLabel = new Label("User creation");
	    
	    firstnameText.setPromptText("Firstname");
	    lastnameText.setPromptText("Lastname");
	    emailText.setPromptText("E-mail");
	    
	    userInfoInput.getChildren().addAll(firstnameText, lastnameText, emailText);
	    
	    // Button for creating an user
	    Button createUserButton = new Button("Create new user");
	    createUserButton.setOnAction(new CreateUserButtonHandler(data, firstnameText, lastnameText, emailText));

	    usersCreationPane.getChildren().addAll(userCreationLabel, userInfoInput, createUserButton);

	    // Users Modification pane
	    usersModificationPane = new VBox();

	    Label userModificationLabel = new Label("User modification");
	    Label oldUsersFirstname = new Label("First name : ");
	    Label oldUsersLastname = new Label("Last name : ");
	    Label oldUsersEmail = new Label("E-mail : ");

	    // If the selected user changed we update the values of the labels
	    usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	oldUsersFirstname.setText("Firstname : " + usersTable.getSelectionModel().getSelectedItem().getFirstname());
	        	oldUsersLastname.setText("Lastname : " + usersTable.getSelectionModel().getSelectedItem().getLastname());
	        	oldUsersEmail.setText("E-mail : " + usersTable.getSelectionModel().getSelectedItem().getEmail());
	        }
	    });
	    
	    // VBox containing all the old user's informations
	    VBox userInfos = new VBox(10);
	    userInfos.getChildren().addAll(userModificationLabel, oldUsersFirstname, oldUsersLastname, oldUsersEmail);
	    
	    // HBox containing all the new user's informations
	    HBox newUserInfosInput = new HBox(10);
	    
	    TextField newFirstnameText = new TextField();
	    TextField newLastnameText = new TextField();
	    TextField newEmailText = new TextField();
	    
	    newFirstnameText.setPromptText("New firstname");
	    newLastnameText.setPromptText("New lastname");
	    newEmailText.setPromptText("New E-mail");
	    
	    // Button to modify an user
	    Button modifyUserButton = new Button("Modify");
	    modifyUserButton.setOnAction(new ModificationUserButtonHandler(oldUsersFirstname, oldUsersLastname, oldUsersEmail, newFirstnameText, newLastnameText, newEmailText, usersTable));

	    // If no user is selected, the button is disable
	    modifyUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    newUserInfosInput.getChildren().addAll(newFirstnameText, newLastnameText, newEmailText, modifyUserButton);
	    
	    usersModificationPane.getChildren().addAll(userInfos, newUserInfosInput);
	    
	    // Main scene of the application
	    usersBorderPane.setCenter(usersTablePane);
	    usersBorderPane.setBottom(hboxButtonChangeScene);
	    stage.setScene(new Scene(usersBorderPane));
	    stage.show();
	}
	
	/**
	 * Method to initialize the table's column with the user's values
	 * @throws IOException
	 */
	public void initializeCol() throws IOException {
		usersBorderPane = new BorderPane();
		
		// We load the values of the users from the text file
	    try {
	    	UserFile.readUsersFromAFileTXT();
	    	
	    	// We add the users to our data
	    	for(User2 u : User2.getAllUser()) {
	    		if(!(u.equals(null))) {
	    			data.add(u);
	    		}
			}
	    } catch (Exception e) {
			System.err.println("File not found. (initializeCol)");
		}
		
	    // Column for the user's ID
		idCol = new TableColumn<>("ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<User2, Integer>("id"));
		
		// Column for the user's firstname
	    firstNameCol = new TableColumn<>("Firstname");
	    firstNameCol.setMinWidth(100);
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<User2, String>("firstname"));

	    // Column for the user's lastnale
	    lastNameCol = new TableColumn<>("Lastname");
	    lastNameCol.setMinWidth(100);
	    lastNameCol.setCellValueFactory(new PropertyValueFactory<User2, String>("lastname"));

	    // Column for the user's e-mail
	    emailCol = new TableColumn<>("E-mail");
	    emailCol.setMinWidth(200);
	    emailCol.setCellValueFactory(new PropertyValueFactory<User2, String>("email"));

	    // We set the item of our table to our list of user and add the column to our table
	    usersTable.setItems(data);
	    usersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol);
	    
	    // The columns take up all the table space
	    usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public static void main(String[] args) {						
		launch(args);
	}
}
