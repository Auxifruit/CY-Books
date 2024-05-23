package control.problemControl;

import presentation.borrowPresentation.ProblemsTable;
import abstraction.Problem;
import abstraction.db.DataBaseManage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;


/**
 * The class to handle the event of the button creating an problem
 */
public class CreateProblemButtonHandler implements EventHandler<ActionEvent> {
	private ProblemsTable problemsTable;
    private int borrowsID;

    /**
     * CreateProblemButtonHandler constructor
     * @param problemsTable the class containing the data and the table for the problems
     * @param borrowsID the borrow's ID we want the problem to refer to
     */
    public CreateProblemButtonHandler(ProblemsTable problemsTable, int borrowsID) {
        this.problemsTable = problemsTable;
        this.borrowsID = borrowsID;
    }

    /**
     * Method to handle the creation of an problem
     */
    @Override
    public void handle(ActionEvent event) {
    	Alert enterProblemAlert = new Alert(Alert.AlertType.INFORMATION);
		enterProblemAlert.setTitle("Problem input");
		enterProblemAlert.setHeaderText("Please enter a problem for the borrow.");
		
		TextField text = new TextField();
		text.setPromptText("Problem");
		
		// We set the content of our alert to the ChoiceBox to be able to choose an user
		enterProblemAlert.getDialogPane().setContent(text);
		enterProblemAlert.showAndWait();
    	
    	// We check if all the text was entered
        if (text.getText().isEmpty()) {
            Alert errorCreateProblemAlert = new Alert(Alert.AlertType.WARNING, "You need type a problem.", ButtonType.OK);
            errorCreateProblemAlert.setTitle("Empty field warning");
            errorCreateProblemAlert.showAndWait();
        } else {
	        Problem problemToCreate = new Problem(borrowsID, text.getText());
	        
	    	// We add our problem in our database
	        try {
				DataBaseManage.addProblemInTable(problemToCreate);
				
				problemsTable.getData().add(problemToCreate);
	            
				problemsTable.updateData();
	            	            
	            Alert createProblemAlert = new Alert(Alert.AlertType.CONFIRMATION, "The problem has been created.", ButtonType.OK);
	            createProblemAlert.setTitle("Problem created confirmation");
	            createProblemAlert.showAndWait();
			} catch (SQLException e) {
				System.err.println("Fail to add a problem in the database");
				Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while creating the problem.", ButtonType.OK);
				errorAlert.setTitle("Problem creation error");
		        errorAlert.showAndWait();
			}
	        
	        // We reset the text in our text fields
            text.clear();
	    }
    }
}