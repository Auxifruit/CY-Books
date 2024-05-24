package presentation.bookPresentation;

import abstraction.API;
import abstraction.Book;
import abstraction.db.DataBaseBorrow;
import control.TablePagination;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.Map;

import java.sql.SQLException;

/**
 * The class containing the pane and the table for the most borrowed books of the last 30 days
 */
public class MostBorrowedBook {
	private VBox booksTableVBox;
	
	private TablePagination<Book> booksTablePagination;
	
	private TableView<Book> booksTable;
	private TableColumn<Book, Integer> idCol;
	private TableColumn<Book, String> titleCol;
	private TableColumn<Book, String> authorCol;
	private TableColumn<Book, Integer> numberBorrowedCol;
	private final ObservableList<Book> data = FXCollections.observableArrayList();
	
	/**
	 * Constructor of the BooksTable class
	 * @param bookList the list with all the books found
	 */
    public MostBorrowedBook() {
    	initializeTable();
        createBooksTablePane();
    }
    
    public VBox getBooksTableVBox() {
    	return booksTableVBox;
    }
    
    /**
	 * Method to create a pane for the most borrowed books of the last 30 days
	 * @return the pane for the most borrowed books of the last 30 days
	 */
	protected void createBooksTablePane() {
		booksTablePagination = new TablePagination<Book>(booksTable, data);
		
	    Label labelBookTable = new Label("MOST BORROWED BOOKS FOR THE LAST 30 DAYS :");
		labelBookTable.setFont(new Font("Arial", 24));
		labelBookTable.setUnderline(true);
		labelBookTable.setStyle("-fx-font-weight: bold;");
	    
	    // We update the pagination to display the right elements
	    booksTablePagination.updatePagination();
	    
	    // VBox containing the nodes for the books table
        booksTableVBox = new VBox(20);
        booksTableVBox.setPadding(new Insets(10, 10, 10, 10));
        booksTableVBox.getChildren().addAll(labelBookTable, booksTable);
        booksTableVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to initialize the TableView bookTable
	 */
	private void initializeTable() {
		// We initialize the ObservableList data
		dataWithAllValues();
		
		booksTable = new TableView<>();
		booksTable.setPlaceholder(new Label("No books to display"));

		// Column for the book's ID
		idCol = new TableColumn<>("ID");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("identifier"));
		
		// Column for the book's firstname
	    titleCol = new TableColumn<>("Title");
	    titleCol.setMinWidth(150);
	    titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

	    // Column for the book's lastname
	    authorCol = new TableColumn<>("Author");
	    authorCol.setMinWidth(150);
	    authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));

	    numberBorrowedCol = new TableColumn<>("Number of time borrowed");
	    numberBorrowedCol.setMinWidth(150);
	    numberBorrowedCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("nbBorrowed"));

	    // We add the column to our table
	    booksTable.getColumns().addAll(idCol, titleCol, authorCol, numberBorrowedCol);
	    
	    // The columns take up all the table space
	    booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of book and 
	    booksTable.setItems(data);
	}
	
	/**
	 * Method to initialize the ObservableList data
	 */
	private void dataWithAllValues() {
		// We reset the data
		data.clear();

		try {
			// We get the Map of the most borrowed book of the last 30 days
			Map<String, Integer> bookList = DataBaseBorrow.mostBorrowBookLastThirtyDays();
			
			// We add them to our data
			for(String key : bookList.keySet()) {
		    	Book b = API.getOneBookFromID(key);
		    	b.setNbBorrowed(bookList.get(key));
				data.add(b);
		    	
		    }
		} catch (SQLException e) {
			System.err.println("Fail to get the most borrowed books from the database");
			Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while getting the most borrowed books.", ButtonType.OK);
			errorAlert.setTitle("Most borrowed books error");
			errorAlert.showAndWait();
		}
			    
	}
}
