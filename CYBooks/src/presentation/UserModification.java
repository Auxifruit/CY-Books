package presentation;

import abstraction.User;
import controle.ModificationUserButtonHandler;
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
	    oldUsersLabel.setUnderline(true);
	    oldUsersLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox oldUsersFirsnameAndValue = new HBox();
	    HBox oldUsersLastnameAndValue = new HBox();
	    HBox oldUsersEmailAndValue = new HBox();
	    
	    Label oldUsersFirstname = new Label("• First name : ");
	    Label oldUsersLastname = new Label("• Last name : ");
	    Label oldUsersEmail = new Label("• E-mail : ");
	    
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
	    usersModificationVBox = new VBox(25);
	    usersModificationVBox.setPadding(new Insets(10, 10, 10, 10));
	    usersModificationVBox.getChildren().addAll(userModificationLabel, usersOldInfos, usersNewInfos);
	    usersModificationVBox.setAlignment(Pos.TOP_CENTER);
	}
	
	/**
	 * Getter to get the VBox containing all the element for the modification of an user
	 * @return the the VBox containing all the element for the modification of an user
	 */
	public VBox getUsersModificationVBox() {
		return usersModificationVBox;
	}
}
