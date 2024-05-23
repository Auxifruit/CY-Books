package control.problemControl;

import abstraction.Problem;
import abstraction.db.DataBaseProblem;
import presentation.borrowPresentation.ProblemsTable;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;

/**
 * The class to handle the event of the button deleting an problem
 */
public class DeleteProblemButtonHandler implements EventHandler<ActionEvent> {
	private ProblemsTable problemsTable;

    /**
     * DeleteProblemButtonHandler constructor
     * @param problemsTable the class containing the data and the table for the problems
     */
    public DeleteProblemButtonHandler(ProblemsTable problemsTable) {
        this.problemsTable = problemsTable;
    }

    /**
     * Method to handle the deletion of an problem
     */
    @Override
    public void handle(ActionEvent event) {
    	// Set the "yes" / "cancel" button for the alert
    	ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    	ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	Alert deleteProblemAlert = new Alert(AlertType.WARNING, "Are you sure to delete this problem ?",
    			yesButton, cancelButton);
    	
    	deleteProblemAlert.setTitle("Delete problem warning");
    	
    	// We get the result of the button, if it's "yes" or "cancel"
    	Optional<ButtonType> result = deleteProblemAlert.showAndWait();
    	
    	if(result.get().equals(yesButton)) {
    		// We get the selected problem we want to delete
    		Problem problemToDelete = problemsTable.getProblemsTable().getSelectionModel().getSelectedItem();		
    		
	    	try { 	
	    		// We remove the problem from our database	    		
	    		DataBaseProblem.deleteProblemInTable(problemToDelete);
	    		Problem.getAllProblems().remove(problemToDelete);

		    	problemsTable.updateData();
	            
	            Alert deletedProblemAlert = new Alert(AlertType.CONFIRMATION, "The problem has been deleted.", ButtonType.OK);
	    		deletedProblemAlert.setTitle("Problem deleted confirmation");
	    		deletedProblemAlert.showAndWait();
	    		
			} catch (SQLException e) {
				System.err.println("Fail to delete an problem from the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the problem.", ButtonType.OK);
				errorAlert.setTitle("Problem deletion error");
				errorAlert.showAndWait();
			}	
    	}
    }
}