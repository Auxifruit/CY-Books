package presentation;

import java.io.IOException;
import abstraction.User2;
import abstraction.UserFile;
import controle.CreateUserButtonHandler;
import controle.ModificationUserButtonHandler;
import controle.DeleteUserButtonHandler;
import javafx.application.Application;
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

public class UsersManagment extends Application {
	private Pane usersTablePane;
	private Pane usersCreationPane;
	private Pane usersModificationPane;
	
	private BorderPane usersBorderPane;
	
	private TableView<User2> usersTable = new TableView<User2>();
	private TableColumn<User2, Integer> idCol;
	private TableColumn<User2, String> firstNameCol;
	private TableColumn<User2, String> lastNameCol;
	private TableColumn<User2, String> emailCol;
	private final ObservableList<User2> data =
		    FXCollections.observableArrayList(
		    	User2.getAllUser()
		    );

	@Override
	public void start(Stage stage) throws Exception {
		// Button to switch scene
		HBox hboxButton = new HBox(10);
		
		Button goToUsersTableButton = new Button("Users table scene");
		goToUsersTableButton.setOnAction(e -> {
			usersBorderPane.setCenter(usersTablePane);
	    });
		
		Button goToUserCreationButton = new Button("Create user scene");
	    goToUserCreationButton.setOnAction(e -> {
	    	usersBorderPane.setCenter(usersCreationPane);
	    });
	    
	    Button goToUserModificationButton = new Button("Users modification scene");
	    goToUserModificationButton.setOnAction(e -> {
	    	usersBorderPane.setCenter(usersModificationPane);
	    });

	    hboxButton.getChildren().addAll(goToUsersTableButton, goToUserCreationButton, goToUserModificationButton);
	    
		// Users Table
	    usersTablePane = new Pane();
		initializeCol();
		
		stage.setTitle("Users table view");
	    stage.setWidth(800);
	    
	    final Label label = new Label("Users");
	    label.setFont(new Font("Arial", 20));
	    
	    Button deleteUserButton = new Button("Delete user");
	    deleteUserButton.setOnAction(new DeleteUserButtonHandler(data, usersTable));
	    
	    TextField filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    FilteredList<User2> filteredData = new FilteredList<>(data, b -> true);
	    filteredField.textProperty().addListener((observalble, oldValue, newValue) -> {
	    	filteredData.setPredicate(user ->  {
	    		if(newValue == null || newValue.isEmpty()) {
	    			return true;
	    		}
	    		
	    		String lowerCaseFilter = newValue.toLowerCase();
	    		if(String.valueOf(user.getId()).toLowerCase().indexOf(lowerCaseFilter)  != -1) {
	    			return true;
	    		}
	    		else if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		else if(user.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		else if(user.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	    			return true;
	    		}
	    		else {
	    			return false;
	    		}
	    		
	    	});
	    });
	    
	    SortedList<User2> sortedData = new SortedList<>(filteredData);
	    sortedData.comparatorProperty().bind(usersTable.comparatorProperty());
	    usersTable.setItems(sortedData);
	    
	    VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(label, filteredField, usersTable, deleteUserButton);
	    
	    usersTablePane.getChildren().add(vbox);
	    
	    // Users Creation
	    usersCreationPane = new VBox();
	    HBox userInfoInput = new HBox(10);
	    
	    TextField firstnameText = new TextField();
	    TextField lastnameText = new TextField();
	    TextField emailText = new TextField();
	    Label userCreationLabel = new Label("User creation");
	    
	    firstnameText.setPromptText("Firstname");
	    lastnameText.setPromptText("Lastname");
	    emailText.setPromptText("E-mail");
	    
	    userInfoInput.getChildren().addAll(firstnameText, lastnameText, emailText);
	    
	    Button createUserButton = new Button("Create new user");
	    createUserButton.setOnAction(new CreateUserButtonHandler(data, firstnameText, lastnameText, emailText));

	    usersCreationPane.getChildren().addAll(userCreationLabel, userInfoInput, createUserButton);

	    // Users Modification
	    usersModificationPane = new VBox();

	    Label userModificationLabel = new Label("User modification");
	    Label oldUsersFirstname = new Label();
	    Label oldUsersLastname = new Label();
	    Label oldUsersEmail = new Label();

	    usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	oldUsersFirstname.setText("Firstname : " + usersTable.getSelectionModel().getSelectedItem().getFirstname());
	        	oldUsersLastname.setText("Lastname : " + usersTable.getSelectionModel().getSelectedItem().getLastname());
	        	oldUsersEmail.setText("E-mail : " + usersTable.getSelectionModel().getSelectedItem().getEmail());
	        }
	    });
	    
	    VBox userInfos = new VBox(10);
	    userInfos.getChildren().addAll(userModificationLabel, oldUsersFirstname, oldUsersLastname, oldUsersEmail);
	    
	    HBox newUserInfosInput = new HBox(10);
	    
	    TextField newFirstnameText = new TextField();
	    TextField newLastnameText = new TextField();
	    TextField newEmailText = new TextField();
	    
	    Button modifyUserButton = new Button("Modify user");
	    modifyUserButton.setOnAction(new ModificationUserButtonHandler(oldUsersFirstname, oldUsersLastname, oldUsersEmail, newFirstnameText, newLastnameText, newEmailText, usersTable));
	    
	    newUserInfosInput.getChildren().addAll(newFirstnameText, newLastnameText, newEmailText, modifyUserButton);
	    
	    newFirstnameText.setPromptText("New firstname");
	    newLastnameText.setPromptText("New lastname");
	    newEmailText.setPromptText("New E-mail");
	    
	    usersModificationPane.getChildren().addAll(userInfos, newUserInfosInput);
	    
	    // Scene
	    usersBorderPane.setCenter(usersTablePane);
	    usersBorderPane.setBottom(hboxButton);
	    stage.setScene(new Scene(usersBorderPane));
	    stage.show();
	}
	
	public void initializeCol() throws IOException {
		usersBorderPane = new BorderPane();
		
	    try {
	    	UserFile.readUsersFromAFileTXT();
	    	
	    	for(User2 u : User2.getAllUser()) {
	    		if(!(u.equals(null))) {
	    			data.add(u);
	    		}
			}
	    } catch (Exception e) {
			System.err.println("File not found. (initializeCol)");
		}
		
		idCol = new TableColumn<>("ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<User2, Integer>("id"));
		
	    firstNameCol = new TableColumn<>("First Name");
	    firstNameCol.setMinWidth(100);
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<User2, String>("firstname"));

	    lastNameCol = new TableColumn<>("Last Name");
	    lastNameCol.setMinWidth(100);
	    lastNameCol.setCellValueFactory(new PropertyValueFactory<User2, String>("lastname"));

	    emailCol = new TableColumn<>("Email");
	    emailCol.setMinWidth(200);
	    emailCol.setCellValueFactory(new PropertyValueFactory<User2, String>("email"));

	    usersTable.setItems(data);
	    usersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol);
	    usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    usersTable.getSelectionModel().selectFirst();
	}

	public static void main(String[] args) {						
		launch(args);
	}
}
