package controle;

import presentation.UsersTable;
import abstraction.User;
import abstraction.db.DBConnect;

import java.sql.SQLException;
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
     * @param pagination the pagination containing the users' data
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
            Alert errorCreateUserAlert = new Alert(Alert.AlertType.WARNING, "You need to fill all the fields if you want to create an user", ButtonType.OK);
            errorCreateUserAlert.setTitle("Empty field(s)");
            errorCreateUserAlert.showAndWait();
        } else {
            // We create the user that will be added to our data base
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
	            
	            	            
	            Alert createUserAlert = new Alert(Alert.AlertType.CONFIRMATION, "The user has been created", ButtonType.OK);
	            createUserAlert.setTitle("User created");
	            createUserAlert.showAndWait();
			} catch (SQLException e) {
				System.err.println("Fail to add an user in the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while creating the user.", ButtonType.OK);
		        errorAlert.showAndWait();
			}

            // We reset the text in our text fields
            firstnameText.clear();
            lastnameText.clear();
            emailText.clear();
        }
    }
}