package presentation;

import java.sql.SQLException;
import java.util.Map;

import abstraction.API;
import abstraction.Book;
import abstraction.db.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MostBorrowedBook {
	private VBox booksTableVBox;
	
	private Pagination booksTablePagination;
	public final static int ROWS_PER_PAGE = 15;
	
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
	 * Method to create a pane for the books table
	 * @return the pane for the books table
	 */
	protected void createBooksTablePane() {
		booksTablePagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
		booksTablePagination.setPageFactory(this::createPage);
	    
		// VBox containing all the node for the books table expect the main label
		VBox searchTableVBox = new VBox(20);
		
	    Label labelBookTable = new Label("MOST BORROWED BOOKS FOR THE LAST 30 DAYS :");
		labelBookTable.setFont(new Font("Arial", 24));
		labelBookTable.setUnderline(true);
		labelBookTable.setStyle("-fx-font-weight: bold;");
	    
	    // We update the number of pages necessary to display all the data
	    changingNumberOfPages();
	    
	    // We update the tableView to display 15 books starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
        
        // If we go to page n, it will display 15 books starting from index n
        booksTablePagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
	    
	    // VBox containing the nodes for the books table
        booksTableVBox = new VBox(20);
        booksTableVBox.setPadding(new Insets(10, 10, 10, 10));
        booksTableVBox.getChildren().addAll(labelBookTable, booksTable);
        booksTableVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to create pages
	 * @param pageIndex the index of the page
	 * @return the BorderPane containing the booksTable with the correct items 
	 */
	private Node createPage(int pageIndex) {
		// We calculate the first index and the last index a the page 
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        
        // We use a sublist and set it to the table view
        // It allows us to display only the books between the corresponding index
        booksTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(booksTable);
    }
	
	/**
	 * Method to change with a certain index and limit in order to display the right element
	 * @param index represent the index of the current page
	 * @param limit correspond the limit of element to display
	 */
	private void changeTableView(int index, int limit) {

		// We calculate the starting index and check if we don't try to access an element outside the list limits
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, data.size());

        // Ensure that we don't take more elements than are available in filteredData
        int minIndex = Math.min(toIndex, data.size());
        
        // We use a SortedList to keep the order of the element of the subList
        SortedList<Book> sortedData = new SortedList<>(FXCollections.observableArrayList(data.subList(Math.min(fromIndex, minIndex), minIndex)));
        
        // We link the element to allow the update of booksTable
        sortedData.comparatorProperty().bind(booksTable.comparatorProperty());

        booksTable.setItems(sortedData);
    }
	
	/**
	 * Method to change the number of pages of a pagination depending of the size of our data
	 */
	private void changingNumberOfPages() {
		// We calculate the numbers of pages needed
	    int totalPage = (int) (Math.ceil(data.size() * 1.0 / ROWS_PER_PAGE));
	    
	    // We set the numbers of page for the pagination and go to the first page
	    booksTablePagination.setPageCount(totalPage);
	    booksTablePagination.setCurrentPageIndex(0);
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
			Map<String, Integer> bookList = DBConnect.mostBorrowBookLastThirtyDays();
			
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
