package presentation.userPresentation;

import abstraction.User;
import control.userControl.ResetTextField;
import control.userControl.ModificationUserButtonHandler;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UserModification {
	private VBox usersModificationVBox;
	
    private TableView<User> usersTable;

    public UserModification(TableView<User> usersTable) {
        this.usersTable = usersTable;
        createUserModificationPane();
    }
    
    /**
	 * Getter to get the VBox containing all the element for the modification of an user
	 * @return the the VBox containing all the element for the modification of an user
	 */
	public VBox getUsersModificationVBox() {
		return usersModificationVBox;
	}

    /**
	 * Method to create a pane to modify an user
	 * @return the pane to modify an user
	 */
	protected void createUserModificationPane() {
		// VBox containing all the old user's informations
	    VBox usersOldInfos = new VBox(15);
	    
		Label userModificationLabel = new Label("USER MODIFICATION :");
	    userModificationLabel.setFont(new Font("Arial", 24));
	    userModificationLabel.setUnderline(true);
	    userModificationLabel.setStyle("-fx-font-weight: bold;");
	    
	    Label oldUsersLabel = new Label("Previous user's information :");
	    oldUsersLabel.setFont(new Font("Arial", 16));
	    oldUsersLabel.setUnderline(true);
	    oldUsersLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox oldUsersFirstnameAndLabel = new HBox();
	    HBox oldUsersLastnameAndLabel = new HBox();
	    HBox oldUsersEmailAndLabel = new HBox();
	    
	    Label oldUsersFirstname = new Label("- First name : ");
	    Label oldUsersLastname = new Label("- Last name : ");
	    Label oldUsersEmail = new Label("- E-mail : ");
	    
	    oldUsersFirstname.setStyle("-fx-font-weight: bold;");
	    oldUsersLastname.setStyle("-fx-font-weight: bold;");
	    oldUsersEmail.setStyle("-fx-font-weight: bold;");
	    
	    Label oldUsersFirstnameLabel = new Label("");
	    Label oldUsersLastnameLabel = new Label("");
	    Label oldUsersEmailLabel = new Label("");

	    // If the selected user changed we update the values of the labels
	    usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	oldUsersFirstnameLabel.setText(usersTable.getSelectionModel().getSelectedItem().getFirstname());
	        	oldUsersLastnameLabel.setText(usersTable.getSelectionModel().getSelectedItem().getLastname());
	        	oldUsersEmailLabel.setText(usersTable.getSelectionModel().getSelectedItem().getEmail());
	        }
	    });
	    
	    oldUsersFirstnameAndLabel.getChildren().addAll(oldUsersFirstname, oldUsersFirstnameLabel);
	    oldUsersLastnameAndLabel.getChildren().addAll(oldUsersLastname, oldUsersLastnameLabel);
	    oldUsersEmailAndLabel.getChildren().addAll(oldUsersEmail, oldUsersEmailLabel);
	    
	    // We add the node useful for the old user's information
	    usersOldInfos.getChildren().addAll(oldUsersLabel, oldUsersFirstnameAndLabel, oldUsersLastnameAndLabel, oldUsersEmailAndLabel);
	    
	    // HBox containing all the new user's informations
	    HBox newUserInfosInput = new HBox(50);
	    
	    Label newUserLabel = new Label("New user's information :");
	    newUserLabel.setFont(new Font("Arial", 16));
	    newUserLabel.setUnderline(true);
	    newUserLabel.setStyle("-fx-font-weight: bold;");
	    
	    TextField newFirstnameText = new TextField();
	    TextField newLastnameText = new TextField();
	    TextField newEmailText = new TextField();
	    
	    newEmailText.setPrefWidth(250);
	    
	    newFirstnameText.setPromptText("New first name");
	    newLastnameText.setPromptText("New last name");
	    newEmailText.setPromptText("New E-mail");
	    
	    // Button to modify an user
	    Button modifyUserButton = new Button("Modify");
	    modifyUserButton.setOnAction(new ModificationUserButtonHandler(oldUsersFirstnameLabel, oldUsersLastnameLabel, oldUsersEmailLabel, newFirstnameText, newLastnameText, newEmailText, usersTable));

	    // Button to modify a borrow
	    Button resetTextFieldButton = new Button("Reset values");
	    resetTextFieldButton.setOnAction(new ResetTextField(newFirstnameText, newLastnameText, newEmailText));

	    // HBox containing the modify and reset button
	    HBox buttonContainers = new HBox(10);
	    buttonContainers.getChildren().addAll(modifyUserButton, resetTextFieldButton);
	    
	    Label userModificationExplanation = new Label("Fill in at least one field in order to modify an user.");
	    
	    // If no user is selected, the button is disable
	    modifyUserButton.disableProperty().bind(Bindings.isEmpty(usersTable.getSelectionModel().getSelectedItems()));
	    
	    // We add the node useful for the new user's information
	    newUserInfosInput.getChildren().addAll(newFirstnameText, newLastnameText, newEmailText, buttonContainers);
	    
	    // VBox containing all the old user's informations
	    VBox usersNewInfos = new VBox(15);
	    usersNewInfos.getChildren().addAll(newUserLabel, userModificationExplanation, newUserInfosInput);
	    
	    // VBox containing the nodes for the user modification
	    usersModificationVBox = new VBox(25);
	    usersModificationVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersModificationVBox.getChildren().addAll(userModificationLabel, usersOldInfos, usersNewInfos);
	    usersModificationVBox.setAlignment(Pos.TOP_CENTER);
	}

}
