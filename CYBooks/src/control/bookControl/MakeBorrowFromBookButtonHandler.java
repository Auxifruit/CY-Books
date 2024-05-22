package control.bookControl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import abstraction.Book;
import abstraction.Borrow;
import abstraction.Status;
import abstraction.User;
import abstraction.db.DBConnect;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

/**
 * The class to handle the event of the button modifying an user
 */
public class MakeBorrowFromBookButtonHandler implements EventHandler<ActionEvent> {
	
	private TableView<Book> booksTable;

	/**
	 * MakeBorrowFromBookButtonHandler constructor
	 * @param booksTable the TableView with all the books
	 */
	public MakeBorrowFromBookButtonHandler(TableView<Book> booksTable) {
		this.booksTable = booksTable;
	}

	@Override
	public void handle(ActionEvent event) {
		// We get the selected book
		Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
		
		Alert chooseUserAlert = new Alert(Alert.AlertType.INFORMATION);
		chooseUserAlert.setTitle("Chosse user by id");
		chooseUserAlert.setHeaderText("Please choose an user choice by its ID.");
	
		// We create a list to stock the users' id by iterate in the allUsers list
		List<Integer> usersIDList = new ArrayList<>();
		Iterator<User> iterator = User.getAllUser().iterator();
		
		// We get the list of all users
		while(iterator.hasNext()) {
			User u = iterator.next();
			usersIDList.add(u.getId());
		}
		
		// Will be use to get the result of the selection
		ChoiceBox<Integer> userChoice = new ChoiceBox<>(FXCollections.observableArrayList(usersIDList));
		
		// We set the content of our alert to the ChoiceBox to be able to choose an user
		chooseUserAlert.getDialogPane().setContent(userChoice);
		chooseUserAlert.showAndWait();

		// We check if an ID was selected or not
		if(userChoice.getSelectionModel().getSelectedItem() != null) {
			int usersIDInt = userChoice.getSelectionModel().getSelectedItem();
			User u = User.getUserById(usersIDInt);
			
			// We check is the user exist
			if(User.isExisting(u)) {
				if(u.getStatus().equals(Status.PUNCTUAL.getText())) {
					try {
						// We count the user's number of borrows to see if he can borrow another book
						int numberOfBorrow = DBConnect.countUsersBorrow(usersIDInt);
						
						if(numberOfBorrow < User.getMaxBorrowNumber()) {
							// We count the number of time the book is borrowed to check if it can be borrowed
							int countBookBorrowed = DBConnect.countBookBorrowed(selectedBook.getIdentifier());
							
							if(countBookBorrowed < Book.getNumberBorrowPossible()) {
								Borrow newBorrow = new Borrow(usersIDInt, selectedBook.getIdentifier(), LocalDate.now());
								DBConnect.addBorrowInTable(newBorrow);
								
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
							Alert cantBorrowMoreErrorAlert = new Alert(Alert.AlertType.ERROR, "The user can't borrow another book.", ButtonType.OK);
							cantBorrowMoreErrorAlert.setTitle("Can't borrow more error");
							cantBorrowMoreErrorAlert.showAndWait();
						}
						
					} catch (SQLException e) {
						System.err.println("Fail to make the borrow.");
						Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while making the borrow.", ButtonType.OK);
						errorAlert.showAndWait();
					}				
				}
				else {
					Alert cantBorrowMoreErrorAlert = new Alert(Alert.AlertType.ERROR, "The user has late borrow, he can't borrow another book.", ButtonType.OK);
					cantBorrowMoreErrorAlert.setTitle("Can't borrow more error");
					cantBorrowMoreErrorAlert.showAndWait();
				}
			}
				
		}
		else {
			Alert idTypeErrorAlert = new Alert(Alert.AlertType.ERROR, "Please choose an user.", ButtonType.OK);
			idTypeErrorAlert.setTitle("User's selection error");
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
