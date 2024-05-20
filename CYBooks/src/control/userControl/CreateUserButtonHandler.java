package control.userControl;

import presentation.UsersTable;
import abstraction.User;
import abstraction.db.DBConnect;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


/**
 * The class to handle the event of the button creating an user
 */
public class CreateUserButtonHandler implements EventHandler<ActionEvent> {
    private ObservableList<User> data;
    private TableView<User> usersTable;
    private Pagination pagination;
    private TextField firstnameText;
    private TextField lastnameText;
    private TextField emailText;

    /**
     * CreateUserButtonHandler constructor
     * @param data the list of all the users
     * @param usersTable the table of all the users
     * @param pagination the TableView's pagination
     * @param firstnameText the new user's first name
     * @param lastnameText the new user's last name
     * @param emailText the new user's first name
     */
    public CreateUserButtonHandler(ObservableList<User> data, TableView<User> usersTable, Pagination pagination, TextField firstnameText, TextField lastnameText, TextField emailText) {
        this.data = data;
        this.usersTable = usersTable;
        this.pagination = pagination;
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
            if(isValidEmail(emailText.getText()) == true) {
            	// We instantiate the user that will be added to our data base
                User userToCreate = new User(lastnameText.getText(), firstnameText.getText(), emailText.getText());
                
            	// We add our user in our database
                try {
    				DBConnect.addUserInTable(userToCreate);
    				
    				data.add(userToCreate);
    	            usersTable.setItems(data);
    	            
    	            // We update the pagination to see if we need to add a new page or not
    	            int numberOfPages = (int) Math.ceil((double) data.size() / UsersTable.ROWS_PER_PAGE);
    	            pagination.setPageCount(numberOfPages);
    	            
    	            if(numberOfPages > 1) {
    	            	pagination.setCurrentPageIndex(1);
    	            }
    	            pagination.setCurrentPageIndex(0);
    	            
    	            	            
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
            	Alert emailAlert = new Alert(Alert.AlertType.WARNING, "Please enter a valid E-mail.", ButtonType.OK);
            	emailAlert.setTitle("E-mail format warning");
            	emailAlert.showAndWait();
            }
        }
    }
    
    /**
	 * Method to check if a String matches the e-mail format
	 * @param email the string we want to check the format
	 * @return true if String matches the e-mail format and false if not
	 */
	public static boolean isValidEmail(String email) {
		// Regular expression to validate an e-mail
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object containing our regular expression
        Pattern pattern = Pattern.compile(regex);

        // Create an Matcher object to search a motif in our regular expression
        Matcher matcher = pattern.matcher(email);

        // Check if the String matches the e-mail format
        return matcher.matches();
    }
}