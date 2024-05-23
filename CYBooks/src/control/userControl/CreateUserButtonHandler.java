package control.userControl;

import presentation.userPresentation.UsersTable;
import abstraction.GeneralUtils;
import abstraction.User;
import abstraction.db.DataBaseManage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;


/**
 * The class to handle the event of the button creating an user
 */
public class CreateUserButtonHandler implements EventHandler<ActionEvent> {
	private UsersTable usersTable;
    private TextField firstnameText;
    private TextField lastnameText;
    private TextField emailText;

    /**
     * CreateUserButtonHandler constructor
     * @param usersTable the class containing the data and the table for the users
     * @param firstnameText the new user's first name
     * @param lastnameText the new user's last name
     * @param emailText the new user's first name
     */
    public CreateUserButtonHandler(UsersTable usersTable, TextField firstnameText, TextField lastnameText, TextField emailText) {
        this.usersTable = usersTable;
        this.firstnameText = firstnameText;
        this.lastnameText = lastnameText;
        this.emailText = emailText;
    }

    /**
     * Method to handle the creation of an user
     */
    @Override
    public void handle(ActionEvent event) {
    	// We check if all the field are filled
        if (firstnameText.getText().isEmpty() || lastnameText.getText().isEmpty() || emailText.getText().isEmpty()) {
            Alert errorCreateUserAlert = new Alert(Alert.AlertType.WARNING, "You need to fill all the fields if you want to create an user.", ButtonType.OK);
            errorCreateUserAlert.setTitle("Empty field(s) warning");
            errorCreateUserAlert.showAndWait();
        } else {
            // We verify is the e-mail format is correct
            if(GeneralUtils.isValidEmail(emailText.getText()) == true) {
            	// We instantiate the user that will be added to our data base
                User userToCreate = new User(lastnameText.getText(), firstnameText.getText(), emailText.getText());
                
            	// We add our user in our database
                try {
    				DataBaseManage.addUserInTable(userToCreate);
    				
    				usersTable.getData().add(userToCreate);
    	            
    				usersTable.updateData();
    	            	            
    	            Alert createUserAlert = new Alert(Alert.AlertType.CONFIRMATION, "The user has been created.", ButtonType.OK);
    	            createUserAlert.setTitle("User created confirmation");
    	            createUserAlert.showAndWait();
    			} catch (SQLException e) {
    				System.err.println("Fail to add an user in the database");
    				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while creating the user.", ButtonType.OK);
    				errorAlert.setTitle("User creation error");
    		        errorAlert.showAndWait();
    			}

                // We reset the text in our text fields
                firstnameText.clear();
                lastnameText.clear();
                emailText.clear();
            }
            else {
            	Alert emailAlert = new Alert(Alert.AlertType.ERROR, "Please enter a valid E-mail.", ButtonType.OK);
            	emailAlert.setTitle("E-mail format error");
            	emailAlert.showAndWait();
            }
        }
    }
}