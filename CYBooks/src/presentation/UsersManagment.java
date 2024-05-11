package presentation;

import java.util.Optional;
import abstraction.User2;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ButtonBar;

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
	    
	    deleteUserButton.setOnAction(e -> {
	    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
	    	Alert deleteUserAlert = new Alert(AlertType.WARNING, "Are you sure to delete this user ?",
	    			yesButton, cancelButton);
	    	
	    	deleteUserAlert.setTitle("Delete user warning");
	    	Optional<ButtonType> result = deleteUserAlert.showAndWait();
	    	
	    	if(result.get().equals(yesButton)) {
	    		User2.getAllUser().remove(usersTable.getSelectionModel().getSelectedItem());
		    	data.remove(usersTable.getSelectionModel().getSelectedItem());

		    	Alert deletedUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been deleted", ButtonType.OK);
	    		deletedUserAlert.setTitle("User deleted");
	    		deletedUserAlert.showAndWait();
	    	}
	    });
	    
	    TextField filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    FilteredList<User2> filteredData = new FilteredList<>(data, b -> true);
	    filteredField.textProperty().addListener((observalble, oldValue, newValue) -> {
	    	filteredData.setPredicate(user ->  {
	    		if(newValue == null || newValue.isEmpty()) {
	    			return true;
	    		}
	    		
	    		String lowerCaseFilter = newValue.toLowerCase();
	    		
	    		if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
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
	    
	    final VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(label, filteredField, usersTable, deleteUserButton);
	    
	    usersTablePane.getChildren().add(vbox);
	    
	    // Users Creation
	    usersCreationPane = new VBox();
	    HBox userInfoInput = new HBox(10);
	    Button createUserButton = new Button("Create new user");
	    TextField firstnameText = new TextField();
	    TextField lastnameText = new TextField();
	    TextField emailText = new TextField();
	    Label userCreationLabel = new Label("User creation");
	    
	    firstnameText.setPromptText("Firstname");
	    lastnameText.setPromptText("Lastname");
	    emailText.setPromptText("E-mail");
	    
	    userInfoInput.getChildren().addAll(firstnameText, lastnameText, emailText);
	    
	    createUserButton.setOnAction(e -> {
	    	if((firstnameText.getText().equals(null) || firstnameText.getText().isEmpty()) || (lastnameText.getText().equals(null) || lastnameText.getText().isEmpty()) || (emailText.getText().equals(null) || emailText.getText().isEmpty())) {
	    		Alert errorCreateUserAlert = new Alert(AlertType.WARNING, "You need to fill all the field", ButtonType.OK);
	    		errorCreateUserAlert.setTitle("Empty field(s)");
	    		errorCreateUserAlert.showAndWait();
	    	}
	    	else {
	    		Alert createUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been created", ButtonType.OK);
	    		createUserAlert.setTitle("User createed");
	    		createUserAlert.showAndWait();
	    		
	    		data.add(new User2(firstnameText.getText(), lastnameText.getText(), emailText.getText()));
	    		
	    		firstnameText.setText("");
	    	    lastnameText.setText("");
	    	    emailText.setText("");
	    	}
	    });

	    usersCreationPane.getChildren().addAll(userCreationLabel, userInfoInput, createUserButton);

	    // Users Modification
	    usersModificationPane = new VBox();
	    Label userModificationLabel = new Label("User modification");
	    Label usersFirstname = new Label("Firstname : " + usersTable.getSelectionModel().getSelectedItem().getFirstname());
	    Label usersLastname = new Label("Lastname : " + usersTable.getSelectionModel().getSelectedItem().getLastname());
	    Label usersEmail = new Label("E-mail : " + usersTable.getSelectionModel().getSelectedItem().getEmail());
	    
	    VBox userInfos = new VBox(10);
	    userInfos.getChildren().addAll(userModificationLabel, usersFirstname, usersLastname, usersEmail);
	    
	    HBox newUserInfosInput = new HBox(10);
	    Button modifyUserButton = new Button("Modify user");
	    TextField newFirstnameText = new TextField();
	    TextField newLastnameText = new TextField();
	    TextField newEmailText = new TextField();
	    
	    newUserInfosInput.getChildren().addAll(newFirstnameText, newLastnameText, newEmailText, modifyUserButton);
	    
	    newFirstnameText.setPromptText("New firstname");
	    newLastnameText.setPromptText("New lastname");
	    newEmailText.setPromptText("New E-mail");
	    
	    usersModificationPane.getChildren().addAll(userInfos, newUserInfosInput);
	    
	    modifyUserButton.setOnAction(e -> {
	    	String newFirstname = newFirstnameText.getText();
    		String newlastname = newLastnameText.getText();
    		String newEmail = newEmailText.getText();
    		if((newFirstname.equals(null) || newFirstname.isEmpty()) && (newlastname.equals(null) || newlastname.isEmpty()) && (newEmail.equals(null) || newEmail.isEmpty())) {
    			Alert errormodificationUserAlert = new Alert(AlertType.WARNING, "You need to fill at least one field", ButtonType.OK);
	    		errormodificationUserAlert.setTitle("Empty fields");
	    		errormodificationUserAlert.showAndWait();
    		}
    		else {
    			ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	    	Alert modificationUserAlert  = new Alert(AlertType.WARNING, "Are you sure to modify this user ?",
    	    			yesButton, cancelButton);
    	    	
    	    	modificationUserAlert.setTitle("Modify user warning");
    	    	Optional<ButtonType> result = modificationUserAlert.showAndWait();
    	    	
    	    	if(result.get().equals(yesButton)) {	    		
    	    		if(!(newFirstname.equals(null) || newFirstname.isEmpty())) {
    	    			usersTable.getSelectionModel().getSelectedItem().setFirstname(newFirstname);
    	    			usersFirstname.setText("Firstname : " + newFirstname);
    	    		}
    	    		if(!(newlastname.equals(null) || newlastname.isEmpty())) {
    	    			usersTable.getSelectionModel().getSelectedItem().setLastname(newlastname);
    	    			usersLastname.setText("Lastname : " + newlastname);
    	    		}
    	    		if(!(newEmail.equals(null) || newEmail.isEmpty())) {
    	    			usersTable.getSelectionModel().getSelectedItem().setEmail(newEmail);
    	    			usersEmail.setText("E-mail : " + newEmail);
    	    		}
    	    	}
    	    	
    	    	modificationUserAlert = new Alert(AlertType.CONFIRMATION, "The user has been modified", ButtonType.OK);
    	    	modificationUserAlert.setTitle("User modified");
        		modificationUserAlert.showAndWait();
    		}
	    });
	    
	    // Scene
	    usersBorderPane.setCenter(usersTablePane);
	    usersBorderPane.setBottom(hboxButton);
	    stage.setScene(new Scene(usersBorderPane));
	    stage.show();
	}
	
	public void initializeCol() {
		usersBorderPane = new BorderPane();
		
	    User2 user1 = new User2("Mark", "Evans", "Mark@gmail.com");
		User2 user2 = new User2("Axel", "Blaze", "Axel@gmail.com");
		User2 user3 = new User2("Shawn", "Froste", "Shawn@gmail.com");
		User2 user4 = new User2("Jude", "Sharp", "Jude@gmail.com");
		
		data.addAll(user1, user2, user3, user4);
		
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
