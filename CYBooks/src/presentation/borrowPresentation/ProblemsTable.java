package presentation.borrowPresentation;

import abstraction.Borrow;
import abstraction.Problem;
import abstraction.db.DataBaseProblem;
import control.problemControl.CreateProblemButtonHandler;
import control.problemControl.DeleteProblemButtonHandler;
import control.TablePagination;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.sql.SQLException;


public class ProblemsTable {
	private VBox problemsTableVBox;
	
	private TablePagination<Problem> problemsTablePagination;
	
	private Borrow borrowToDisplay;
	
	private Button createProblemButton;
	
	private TableView<Problem> problemsTable;
	private TableColumn<Problem, Integer> idCol;
	private TableColumn<Problem, String> textCol;
	private final ObservableList<Problem> data = FXCollections.observableArrayList();
	
	public ProblemsTable() {
		this.borrowToDisplay = null;
    	initializeData();
        initializeTable();
        createProblemsTablePane();
    }
	
	public void setBorrowToDisplay(Borrow borrowToDisplay) {
		this.borrowToDisplay = borrowToDisplay;
		createProblemButton.setOnAction(new CreateProblemButtonHandler(this, borrowToDisplay.getId()));
	}
	
	/**
	 * Getter to get the data containing the problems
	 * @return the data containing the problems
	 */
    public ObservableList<Problem> getData() {
        return data;
    }

    /**
	 * Getter to get the table view displaying the problems
	 * @return the table view displaying the problems
	 */
    public TableView<Problem> getProblemsTable() {
        return problemsTable;
    }

    /**
	 * Getter to get the pagination for the table view of problems
	 * @return the pagination for the table view of problems
	 */
    /*
    public Pagination getProblemsTablePagination() {
        return ;
    }*/
    
    /**
	 * Getter to get the VBox containing all the element for the problem table
	 * @return the the VBox containing all the element for the problem table
	 */
    public VBox getProblemsTableVBox() {
    	return problemsTableVBox;
    }
    
	/**
	 * Method to create a pane for the problems table
	 * @return the pane for the problems table
	 */
	protected void createProblemsTablePane() {
		problemsTablePagination = new TablePagination<>(problemsTable, data);
	    
		// VBox containing all the node for the problems table expect the main label
		VBox searchTableVBox = new VBox(20);
		
	    Label labelProblemTable = new Label("BORROW'S TABLE PROBLEMS TABLE :");
		labelProblemTable.setFont(new Font("Arial", 24));
		labelProblemTable.setUnderline(true);
		labelProblemTable.setStyle("-fx-font-weight: bold;");
	    
	    // Button to delete an problem
	    Button deleteProblemButton = new Button("Delete problem");
	    deleteProblemButton.setOnAction(new DeleteProblemButtonHandler(this));
	    
	    // If no problem is selected, the button is disable
	    deleteProblemButton.disableProperty().bind(Bindings.isEmpty(problemsTable.getSelectionModel().getSelectedItems()));
	    
	    // Button to delete an problem
	    createProblemButton = new Button("Create problem");
	    
	    // HBox containing the buttons to delete and create a Problem
	    HBox buttonsContainer = new HBox(10);
	    buttonsContainer.getChildren().addAll(deleteProblemButton, createProblemButton);
	    
	    searchTableVBox.getChildren().addAll(problemsTablePagination.getPagination(), buttonsContainer);
	    
	    // VBox containing the nodes for the problems table
	    problemsTableVBox = new VBox(20);
	    problemsTableVBox.setPadding(new Insets(10, 10, 10, 10));
	    problemsTableVBox.getChildren().addAll(labelProblemTable, searchTableVBox);
	    problemsTableVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to initialize the TableView problemTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllValues();
		
		problemsTable = new TableView<>();
		problemsTable.setPlaceholder(new Label("No problems to display"));
		
		// Column for the problem's id
		idCol = new TableColumn<>("Problem's ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Problem, Integer>("id"));
	    
	    // Column for the problem's text
	    textCol = new TableColumn<>("Problem");
	    textCol.setMinWidth(200);
	    textCol.setCellValueFactory(new PropertyValueFactory<Problem, String>("text"));

	    // We add the column to our table
	    problemsTable.getColumns().addAll(idCol, textCol);
	    
	    // The columns take up all the table space
	    problemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of problem and 
	    problemsTable.setItems(data);
	}
	
	/**
	 * Method to initialize all the problem's informations
	 */
	private void initializeData() {
		// We load the values of the problems from the database
	    try {
	    	DataBaseProblem.readProblemsTable();
	    } catch (SQLException e) {
			System.err.println("Error BDD. (dataWithAllValues)");
		}
	}
	
	/**
	 * Method to initialize the ObservableList data
	 */
	private void dataWithAllValues() {
		// We reset the data
		data.clear();
		
        if (borrowToDisplay != null) {
        	
	    	// We add all the borrow's problem to our data
	    	for(Problem p : Problem.getAllProblems()) {
	    		if(!(p.equals(null)) && p.getBorrowsID() == borrowToDisplay.getId()) {
	    			data.add(p);
	    		}
			}
        }
	}
	
	/**
   	 * Method to update the ObservableList data and the TableView
   	 */
   	public void updateData() {
   		// We clear the data
   		data.clear();
   		
   		if (borrowToDisplay != null) {
        	
	    	// We add all the borrow's problem to our data
	    	for(Problem p : Problem.getAllProblems()) {
	    		if(!(p.equals(null)) && p.getBorrowsID() == borrowToDisplay.getId()) {
	    			data.add(p);
	    		}
			}
        }
   		
   		// We set the new data
   		getProblemsTable().setItems(data);
   		
   		problemsTablePagination.updatePagination();
   	}
}
