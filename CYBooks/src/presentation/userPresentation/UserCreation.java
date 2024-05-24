package presentation.userPresentation;

import control.userControl.CreateUserButtonHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The class containing the pane to create an user
 */
public class UserCreation {
	
	private VBox userCreationVBox;
	
    private UsersTable usersTable;

    /**
     * Constructor of the UserCreation class
     * @param usersTable the class containing the data and the table for the users
     */
    public UserCreation(UsersTable usersTable) {
    	this.usersTable = usersTable;
        createUserCreationPane();
    }
    
    /**
	 * Getter to get the VBox containing all the element for the creation of an user
	 * @return the the VBox containing all the element for the creation of an user
	 */
	public VBox getUserCreationVBox() {
		return userCreationVBox;
	}

    /**
	 * Method to create a pane to create an user
	 * @return the pane to create an user
	 */
	protected void createUserCreationPane() {
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
	    
	    Label firstnameLabel = new Label("• First name :");
	    Label lastnameLabel = new Label("• Last name : ");
	    Label emailLabel = new Label("• E-mail : ");
	    
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
	    createUserButton.setOnAction(new CreateUserButtonHandler(usersTable, firstnameText, lastnameText, emailText));
	    
	    // We add all the node necessary to create an user 
	    userInfoInput.getChildren().addAll(userCreationExplanation, firstnameHBox, lastnameHBox, emailHBox, createUserButton);

	    // VBox containing the nodes for the user creation
	    userCreationVBox = new VBox(20);
	    userCreationVBox.setPadding(new Insets(10, 10, 10, 10));
	    userCreationVBox.getChildren().addAll(userCreationLabel, userInfoInput);
	    userCreationVBox.setAlignment(Pos.TOP_CENTER);
	}
	
}
