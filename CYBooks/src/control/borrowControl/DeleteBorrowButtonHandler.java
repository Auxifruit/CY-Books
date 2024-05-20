package control.borrowControl;

import java.sql.SQLException;
import java.util.Optional;

import abstraction.Borrow;
import abstraction.db.DBConnect;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import presentation.BorrowsTable;

/**
 * The class to handle the event of the button deleting a borrow
 */
public class DeleteBorrowButtonHandler implements EventHandler<ActionEvent> {
	private ObservableList<Borrow> data;
    private TableView<Borrow> borrowsTable;
    private Pagination pagination;

    /**
     * DeleteBorrowButtonHandler constructor
     * @param data the list of all the borrows
     * @param borrowsTable the table of all the borrows
     * @param pagination the TableView's pagination
     */
    public DeleteBorrowButtonHandler(ObservableList<Borrow> data, TableView<Borrow> borrowsTable, Pagination pagination) {
        this.data = data;
        this.borrowsTable = borrowsTable;
        this.pagination = pagination;
    }

    /**
     * Method to handle the deletion of an borrow
     */
    @Override
    public void handle(ActionEvent event) {
    	// Set the "yes" / "cancel" button for the alert
    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert deleteBorrowAlert = new Alert(AlertType.WARNING, "Are you sure to delete this borrow ?",
    			yesButton, cancelButton);
    	
    	deleteBorrowAlert.setTitle("Delete borrow warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = deleteBorrowAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		// We get the selected borrow we want to delete
    		Borrow borrowToDelete = borrowsTable.getSelectionModel().getSelectedItem();		
    		
	    	try {
	    		// We remove it from our database, the list of all the borrows and the data 
	    		DBConnect.deleteBorrowInTable(borrowToDelete);
	    		Borrow.getAllBorrow().remove(borrowToDelete);
		    	data.remove(borrowToDelete);

		    	borrowsTable.setItems(data);
		    	
		    	// We update the pagination to see if we need to remove a new page or not
	            int numberOfPages = (int) Math.ceil((double) data.size() / BorrowsTable.ROWS_PER_PAGE);
	            pagination.setPageCount(numberOfPages);
	            
	            if(numberOfPages > 1) {
	            	pagination.setCurrentPageIndex(1);
	            }
	            pagination.setCurrentPageIndex(0);
	            
	            Alert deletedBorrowAlert = new Alert(AlertType.CONFIRMATION, "The borrow has been deleted.", ButtonType.OK);
	    		deletedBorrowAlert.setTitle("Borrow deleted confirmation");
	    		deletedBorrowAlert.showAndWait();
	    		
			} catch (SQLException e) {
				System.err.println("Fail to delete a borrow from the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the borrow.", ButtonType.OK);
				errorAlert.showAndWait();
			}	
    	}
    }

}
