package presentation;

import java.io.IOException;
import abstraction.User;
import abstraction.UserFile;
import controle.CreateUserButtonHandler;
import controle.ModificationUserButtonHandler;
import controle.DeleteUserButtonHandler;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	
	private TableView<User> usersTable = new TableView<>();
	private TableColumn<User, Integer> idCol;
	private TableColumn<User, String> firstNameCol;
	private TableColumn<User, String> lastNameCol;
	private TableColumn<User, String> emailCol;
	private final ObservableList<User> data = FXCollections.observableArrayList(User.getAllUser());
	
	/**
	 * Method to start the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// We initialize the values of all the columns
	    initializeCol();
		
		stage.setTitle("Users table view");
	    stage.setWidth(800);
	    
		// Users Table pane
	    usersTablePane = new VBox();
	    
	    usersTablePane.getChildren().add(createUsersTablePane());
	    
	    // Users Creation pane
	    usersCreationPane = new VBox();
	    
	    usersCreationPane.getChildren().addAll(createUserCreationPane());

	    // Users Modification pane
	    usersModificationPane = new VBox();
	    
	    usersModificationPane.getChildren().addAll(createUserModificationPane());
	    
    	// HBox containing all the button to change scene
 		HBox hboxButtonChangeScene = new HBox(50);
 		hboxButtonChangeScene.setAlignment(Pos.CENTER);
 		hboxButtonChangeScene.setPrefHeight(50);
 		
 		// Buttons to change scene
 		Button goToUsersTableButton = new Button("Users table");
 		goToUsersTableButton.setOnAction(e -> {
 			usersBorderPane.setCenter(usersTablePane);
 	    });
 		
 		Button goToUserCreationButton = new Button("Create an user");
 	    goToUserCreationButton.setOnAction(e -> {
 	    	usersBorderPane.setCenter(usersCreationPane);
 	    });
 	    
 	    Button goToUserModificationButton = new Button("Modify the user");
 	    goToUserModificationButton.setOnAction(e -> {
 	    	usersBorderPane.setCenter(usersModificationPane);
 	    });

 	    hboxButtonChangeScene.getChildren().addAll(goToUsersTableButton, goToUserCreationButton, goToUserModificationButton);
	    
	    // Main scene of the application
	    usersBorderPane.setCenter(usersTablePane);
	    usersBorderPane.setBottom(hboxButtonChangeScene);
	    stage.setScene(new Scene(usersBorderPane));
	    stage.show();
	}

	/**
	 * Method to create a pane for the users table
	 * @return the pane for the users table
	 */
	private Pane createUsersTablePane() {
		// VBox containing all the node for the users table expect the main label
		VBox searchTableButtonVBox = new VBox(20);
		
	    Label labelUserTable = new Label("USERS TABLE :");
		labelUserTable.setFont(new Font("Arial", 24));
		labelUserTable.setUnderline(true);
		labelUserTable.setStyle("-fx-font-weight: bold;");
	    
	    // Button to delete an user
	    Button deleteUserButton = new Button("Delete user");
	    deleteUserButton.setOnAction(new DeleteUserButtonHandler(data, usersTable));
	    
	    // If no user is selected, the button is disable
	    deleteUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // Allow the search in the table view
	    TextField filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    FilteredList<User> filteredData = new FilteredList<>(data, b -> true);
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
	    		// We check if it's correspond to the user's first name
	    		else if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		// We check if it's correspond to the user's last name
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
	    SortedList<User> sortedData = new SortedList<>(filteredData);
	    sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
	    usersTable.setItems(sortedData);
	    
	    searchTableButtonVBox.getChildren().addAll(filteredField, usersTable, deleteUserButton);
	    
	    // VBox containing the nodes for the users table
	    VBox usersTableVBox = new VBox(20);
	    usersTableVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersTableVBox.getChildren().addAll(labelUserTable, searchTableButtonVBox);
	    usersTableVBox.setAlignment(Pos.CENTER);
	    
		return usersTableVBox;
	}
	
	/**
	 * Method to create a pane to create an user
	 * @return the pane to create an user
	 */
	private Pane createUserCreationPane() {
		// HBox containing the input for the user's information
	    VBox userInfoInput = new VBox(25);
	    
	    Label userCreationLabel = new Label("USER CREATION :");
	    userCreationLabel.setFont(new Font("Arial", 24));
	    userCreationLabel.setUnderline(true);
	    userCreationLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label userCreationExplanation = new Label("Fill in all the fields in order to create a new user.");
	    
	    // We create an HBox for each user's informations containing a label and a TextField
	    HBox firstnameHBox = new HBox(28);
	    HBox lastnameHBox = new HBox(27);
	    HBox emailHBox = new HBox(47);
	    
	    Label firstnameLabel = new Label("� First name :");
	    Label lastnameLabel = new Label("� Last name : ");
	    Label emailLabel = new Label("� E-mail : ");
	    
	    firstnameLabel.setStyle("-fx-font-weight: bold;");
	    lastnameLabel.setStyle("-fx-font-weight: bold;");
	    emailLabel.setStyle("-fx-font-weight: bold;");
	    
	    TextField firstnameText = new TextField();
	    TextField lastnameText = new TextField();
	    TextField emailText = new TextField();
	    
	    firstnameText.setPromptText("First name");
	    lastnameText.setPromptText("Last name");
	    emailText.setPromptText("E-mail");
	    
	    emailText.setPrefWidth(250);
	    
	    firstnameHBox.getChildren().addAll(firstnameLabel, firstnameText);
	    lastnameHBox.getChildren().addAll(lastnameLabel, lastnameText);
	    emailHBox.getChildren().addAll(emailLabel, emailText);
	    
	    // Button for creating an user
	    Button createUserButton = new Button("Create new user");
	    createUserButton.setOnAction(new CreateUserButtonHandler(data, firstnameText, lastnameText, emailText));
	    
	    // We add all the node necessary to create an user 
	    userInfoInput.getChildren().addAll(userCreationExplanation, firstnameHBox, lastnameHBox, emailHBox, createUserButton);

	    // VBox containing the nodes for the user creation
	    VBox userCreationVBox = new VBox(20);
	    userCreationVBox.setPadding(new Insets(10, 10, 10, 10));
	    userCreationVBox.getChildren().addAll(userCreationLabel, userInfoInput);
	    userCreationVBox.setAlignment(Pos.CENTER);
	    
		return userCreationVBox;
	}
	
	/**
	 * Method to create a pane to modify an user
	 * @return the pane to modify an user
	 */
	private Node createUserModificationPane() {
		// VBox containing all the old user's informations
	    VBox usersOldInfos = new VBox(15);
	    
		Label userModificationLabel = new Label("USER MODIFICATION :");
	    userModificationLabel.setFont(new Font("Arial", 24));
	    userModificationLabel.setUnderline(true);
	    userModificationLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label oldUsersLabel = new Label("Previous user's information :");
	    oldUsersLabel.setUnderline(true);
	    oldUsersLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox oldUsersFirsnameAndValue = new HBox();
	    HBox oldUsersLastnameAndValue = new HBox();
	    HBox oldUsersEmailAndValue = new HBox();
	    
	    Label oldUsersFirstname = new Label("� First name : ");
	    Label oldUsersLastname = new Label("� Last name : ");
	    Label oldUsersEmail = new Label("� E-mail : ");
	    
	    oldUsersFirstname.setStyle("-fx-font-weight: bold;");
	    oldUsersLastname.setStyle("-fx-font-weight: bold;");
	    oldUsersEmail.setStyle("-fx-font-weight: bold;");
	    
	    Label oldUsersFirstnameValue = new Label("");
	    Label oldUsersLastnameValue = new Label("");
	    Label oldUsersEmailValue = new Label("");

	    // If the selected user changed we update the values of the labels
	    usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	oldUsersFirstnameValue.setText(usersTable.getSelectionModel().getSelectedItem().getFirstname());
	        	oldUsersLastnameValue.setText(usersTable.getSelectionModel().getSelectedItem().getLastname());
	        	oldUsersEmailValue.setText(usersTable.getSelectionModel().getSelectedItem().getEmail());
	        }
	    });
	    
	    oldUsersFirsnameAndValue.getChildren().addAll(oldUsersFirstname, oldUsersFirstnameValue);
	    oldUsersLastnameAndValue.getChildren().addAll(oldUsersLastname, oldUsersLastnameValue);
	    oldUsersEmailAndValue.getChildren().addAll(oldUsersEmail, oldUsersEmailValue);
	    
	    // We add the node useful for the old user's information
	    usersOldInfos.getChildren().addAll(oldUsersLabel, oldUsersFirsnameAndValue, oldUsersLastnameAndValue, oldUsersEmailAndValue);
	    
	    // HBox containing all the new user's informations
	    HBox newUserInfosInput = new HBox(50);
	    
	    Label newUsersLabel = new Label("New user's information :");
	    newUsersLabel.setUnderline(true);
	    newUsersLabel.setStyle("-fx-font-weight: bold;");
	    
	    TextField newFirstnameText = new TextField();
	    TextField newLastnameText = new TextField();
	    TextField newEmailText = new TextField();
	    
	    newEmailText.setPrefWidth(250);
	    
	    newFirstnameText.setPromptText("New first name");
	    newLastnameText.setPromptText("New last name");
	    newEmailText.setPromptText("New E-mail");
	    
	    // Button to modify an user
	    Button modifyUserButton = new Button("Modify");
	    modifyUserButton.setOnAction(new ModificationUserButtonHandler(oldUsersFirstnameValue, oldUsersLastnameValue, oldUsersEmailValue, newFirstnameText, newLastnameText, newEmailText, usersTable));

	    Label userModificationExplanation = new Label("Fill in at least one field in order to modify an user.");
	    
	    // If no user is selected, the button is disable
	    modifyUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // We add the node useful for the new user's information
	    newUserInfosInput.getChildren().addAll(newFirstnameText, newLastnameText, newEmailText, modifyUserButton);
	    
	    // VBox containing all the old user's informations
	    VBox usersNewInfos = new VBox(15);
	    usersNewInfos.getChildren().addAll(newUsersLabel, userModificationExplanation, newUserInfosInput);
	    
	    // VBox containing the nodes for the user modification
	    VBox usersModificationVBox = new VBox(25);
	    usersModificationVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersModificationVBox.getChildren().addAll(userModificationLabel, usersOldInfos, usersNewInfos);
	    usersModificationVBox.setAlignment(Pos.CENTER);
		
	    return usersModificationVBox;
	}

	/**
	 * Method to initialize the table's column with the user's values
	 * @throws IOException
	 */
	private void initializeCol() throws IOException {
		usersBorderPane = new BorderPane();
		
		// We load the values of the users from the text file
	    try {
	    	UserFile.readUsersFromAFileTXT();
	    	
	    	// We add the users to our data
	    	for(User u : User.getAllUser()) {
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
		idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
		
		// Column for the user's firstname
	    firstNameCol = new TableColumn<>("First name");
	    firstNameCol.setMinWidth(100);
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));

	    // Column for the user's lastnale
	    lastNameCol = new TableColumn<>("Last name");
	    lastNameCol.setMinWidth(100);
	    lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));

	    // Column for the user's e-mail
	    emailCol = new TableColumn<>("E-mail");
	    emailCol.setMinWidth(200);
	    emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

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