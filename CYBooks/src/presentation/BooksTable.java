package presentation;

import java.util.List;

import abstraction.Book;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import control.bookControl.MakeBorrowFromBookButtonHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BooksTable {
	private VBox booksTableVBox;
	
	private List<Book> bookList;
	
	private Pagination booksTablePagination;
	public final static int ROWS_PER_PAGE = 15;
	
	private TableView<Book> booksTable;
	private TableColumn<Book, Integer> idCol;
	private TableColumn<Book, String> titleCol;
	private TableColumn<Book, String> authorCol;
	private TableColumn<Book, String> publishedDateCol;
	private TableColumn<Book, String> formatCol;
	private TableColumn<Book, String> typeCol;
	private TableColumn<Book, String> publisherCol;
	private final ObservableList<Book> data = FXCollections.observableArrayList();
	private FilteredList<Book> filteredData;
	private TextField filteredField;

	/**
	 * Constructor of the BooksTable class
	 * @param bookList the list with all the books found
	 */
    public BooksTable(List<Book> bookList) {
    	this.bookList = bookList;
    	initializeTable();
        createBooksTablePane();
    }

	/**
	 * Getter to get the data containing the books
	 * @return the data containing the books
	 */
    public ObservableList<Book> getData() {
        return data;
    }

    /**
	 * Getter to get the table view displaying the books
	 * @return the table view displaying the books
	 */
    public TableView<Book> getBooksTable() {
        return booksTable;
    }

    /**
	 * Getter to get the pagination for the table view of books
	 * @return the pagination for the table view of books
	 */
    public Pagination getBooksTablebooksTablePagination() {
        return booksTablePagination;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the book table
	 * @return the the VBox containing all the element for the book table
	 */
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
		
	    Label labelBookTable = new Label("BOOKS TABLE :");
		labelBookTable.setFont(new Font("Arial", 24));
		labelBookTable.setUnderline(true);
		labelBookTable.setStyle("-fx-font-weight: bold;");
	    
	    // Button to delete an book
	    Button makeBorrowFromBookButton = new Button("Make a borrow from this book");
	    makeBorrowFromBookButton.setOnAction(new MakeBorrowFromBookButtonHandler(booksTable));
	    
	    // If no book is selected, the button is disable
	    makeBorrowFromBookButton.disableProperty().bind(Bindings.isEmpty(booksTable.getSelectionModel().getSelectedItems()));
		
	    // Allow the search in the table view
	    filteredField = new TextField();
	    filteredField.setPromptText("Search");
	    
	    // At start all data are correct
	    filteredData = new FilteredList<>(data, b -> true);
	    
	    // If we change the value of the search
	    filteredField.textProperty().addListener((observalble, oldValue, newValue) -> {
	    	// Used to check if the input is the same as some values of one use
	    	filteredData.setPredicate(book ->  {
	    		if(newValue == null || newValue.isEmpty()) {
	    			return true;
	    		}
	    		
	    		// Splitting the search string into parts
	            String[] parts = newValue.toLowerCase().split(" ");
	            
	            // Check if any part matches ID, first name, last name, or email
	            for (String part : parts) {
	                if (book.getIdentifier().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getTitle().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getAuthor().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getPublishedDate().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getFormat().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getType().toLowerCase().contains(part)) {
	                    continue;
	                } else if (book.getPublisher().toLowerCase().contains(part)) {
	                    continue;
	                } else {
	                    // If no part matches, return false
	                    return false;
	                }
	            }
	            
	            // If all parts match, return true
	            return true;
	    		
	    	});
	    	changeTableView(booksTablePagination.getCurrentPageIndex(), ROWS_PER_PAGE);
	    });
	    
	    searchTableVBox.getChildren().addAll(filteredField, booksTablePagination, makeBorrowFromBookButton);
	    
	    // We update the number of pages necessary to display all the data
	    changingNumberOfPages();
	    
	    // We update the tableView to display 15 books starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
        
        // If we go to page n, it will display 15 books starting from index n
        booksTablePagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
	    
	    // VBox containing the nodes for the books table
        booksTableVBox = new VBox(20);
        booksTableVBox.setPadding(new Insets(10, 10, 10, 10));
        booksTableVBox.getChildren().addAll(labelBookTable, searchTableVBox);
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
        int minIndex = Math.min(toIndex, filteredData.size());
        
        // We use a SortedList to keep the order of the element of the subList
        SortedList<Book> sortedData = new SortedList<>(FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        
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

	    // Column for the book's e-mail
	    publishedDateCol = new TableColumn<>("Published date");
	    publishedDateCol.setMinWidth(80);
	    publishedDateCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publishedDate"));
	    
	    // Column for the book's e-mail
	    formatCol = new TableColumn<>("Format");
	    formatCol.setMinWidth(200);
	    formatCol.setCellValueFactory(new PropertyValueFactory<Book, String>("format"));
	    
	    // Column for the book's e-mail
	    typeCol = new TableColumn<>("Type");
	    typeCol.setMinWidth(100);
	    typeCol.setCellValueFactory(new PropertyValueFactory<Book, String>("type"));
	    
	    // Column for the book's e-mail
	    publisherCol = new TableColumn<>("Editor");
	    publisherCol.setMinWidth(80);
	    publisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));

	    // We add the column to our table
	    booksTable.getColumns().addAll(idCol, titleCol, authorCol, publishedDateCol, formatCol, typeCol, publisherCol);
	    
	    // The columns take up all the table space
	    booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    
	    // We set the item of our table to our list of book and 
	    booksTable.setItems(data);
	}
	
	/**
	 * Method to initialize the ObservableList data
	 */
	private void dataWithAllValues() {
	    for(Book b : bookList) {
	    	if(!(b.equals(null))) {
	    		data.add(b);
	    	}
	    }
	}
	
	/**
	 * Method to update the ObservableList data
	 */
	public void updateData(List<Book> bookList) {
		// We reset the TextField for the search
		filteredField.clear();
		
		// We clear the data
		data.clear();
		
		for(Book b : bookList) {
	    	if(!(b.equals(null))) {
	    		data.add(b);
	    	}
	    }
		
		// We set the new data
		getBooksTable().setItems(data);
		
		// We update the number of pages necessary to display all the data
	    changingNumberOfPages();
	    
	    // We update the tableView to display 15 users starting from index 0
        changeTableView(0, ROWS_PER_PAGE);
	}
    
}
