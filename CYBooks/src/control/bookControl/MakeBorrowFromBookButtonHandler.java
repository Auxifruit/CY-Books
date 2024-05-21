package control.bookControl;

import java.sql.SQLException;
import java.time.LocalDate;

import abstraction.Book;
import abstraction.Borrow;
import abstraction.User;
import abstraction.db.DBConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import presentation.BorrowsTable;
import presentation.UserProfile;

/**
 * The class to handle the event of the button modifying an user
 */
public class MakeBorrowFromBookButtonHandler implements EventHandler<ActionEvent> {
	
	private TableView<Book> booksTable;
	
	private BorrowsTable borrowsTable;
	private UserProfile userProfile;

	/**
	 * MakeBorrowFromBookButtonHandler constructor
	 * @param booksTable the TableView with all the books
	 * @param borrowsTable the class containing the data and the table for the borrows
	 * @param userProfile the class containing the data and the table for the user's profile
	 */
	public MakeBorrowFromBookButtonHandler(TableView<Book> booksTable, BorrowsTable borrowsTable, UserProfile userProfile) {
		this.borrowsTable = borrowsTable;
		this.booksTable = booksTable;
		this.userProfile = userProfile;
	}

	@Override
	public void handle(ActionEvent event) {
		// We get the selected book
		Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
		
		// We ask which user want to borrow the book by it's id
		TextInputDialog enterUsersId = new TextInputDialog();
		enterUsersId.setTitle("Enter ID");
		enterUsersId.setContentText("Enter an existing user's ID.");
		enterUsersId.showAndWait();
		
		String usersIDString = enterUsersId.getEditor().getText();

		// We check if the input in an Integer
		if(isNumeric(usersIDString)) {
			int usersIDInt = Integer.parseInt(usersIDString);
			
			// We check is the user exist
			if(User.isExisting(usersIDInt)) {
				try {
					// We count the user's number of borrows to see if he can borrow another book
					int numberOfBorrow = DBConnect.countUsersBorrow(usersIDInt);
					
					if(numberOfBorrow < User.getMaxBorrowNumber()) {
						// We count the number of time the book is borrowed to check if it can be borrowed
						int countBookBorrowed = DBConnect.countBookBorrowed(selectedBook.getIdentifier());
						
						if(countBookBorrowed < Book.getCanBeBorrow()) {
							Borrow newBorrow = new Borrow(usersIDInt, selectedBook.getIdentifier(), LocalDate.now());
							DBConnect.addBorrowInTable(newBorrow);
							
							borrowsTable.updateData();
							userProfile.updateData();
							
							Alert createBorrowAlert = new Alert(Alert.AlertType.CONFIRMATION, "The borrow has been created.", ButtonType.OK);
							createBorrowAlert.setTitle("Borrow creation confirmation");
							createBorrowAlert.showAndWait();
							
						}
						else {
							Alert alreadyBorrowAlert = new Alert(Alert.AlertType.ERROR, "The book is already borrow.", ButtonType.OK);
							alreadyBorrowAlert.setTitle("Already borrow error");
							alreadyBorrowAlert.showAndWait();
						}
					}
					else {
						Alert cantBorrowMoreAlertError = new Alert(Alert.AlertType.ERROR, "The user can't borrow another book.", ButtonType.OK);
						cantBorrowMoreAlertError.setTitle("Can't borrow more error");
						cantBorrowMoreAlertError.showAndWait();
					}
					
				} catch (SQLException e) {
					System.err.println("Fail to make the borrow.");
					Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while making the borrow.", ButtonType.OK);
					errorAlert.showAndWait();
				}				
			}
			else {
				Alert noIdAlert = new Alert(Alert.AlertType.ERROR, "The entered ID doesn't exist, please enter an existing user's ID.", ButtonType.OK);
				noIdAlert.setTitle("No ID found error");
				noIdAlert.showAndWait();
			}
		}
		else {
			Alert idTypeErrorAlert = new Alert(Alert.AlertType.ERROR, "Please enter a number for the user's ID.", ButtonType.OK);
			idTypeErrorAlert.setTitle("User's ID error");
			idTypeErrorAlert.showAndWait();
		}
	}
	
	/**
	 * Method to check if a String is an Integer or not
	 * @param numberString the String we want to check
	 * @return true if the String is an Integer and false if not 
	 */
	public static boolean isNumeric(String numberString) {
	    if (numberString == null) {
	        return false;
	    }
	    try {
	        Integer.parseInt(numberString);
	    } catch (NumberFormatException nfe) {
	    	// If an exception was caught the String was not an Integer
	        return false;
	    }
	    return true;
	}

}
