package presentation.bookPresentation;

import abstraction.Book;
import control.TablePagination;
import control.bookControl.MakeBorrowFromBookButtonHandler;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The class containing the pane and the table for the books
 */
public class BooksTable {
	private VBox booksTableVBox;
	
	private List<Book> bookList;
	
	private TablePagination<Book> booksTablePagination;
	
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
	    
	    booksTablePagination = new TablePagination<Book>(booksTable, data, filteredData);
	    
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
	    	// We update the pagination to display the right elements after filtering it
	    	booksTablePagination.updatePaginationSearch();
	    });
	    
	    searchTableVBox.getChildren().addAll(filteredField, booksTablePagination.getPagination(), makeBorrowFromBookButton);
	    
	    // We update the pagination to display the right elements
	    booksTablePagination.updatePaginationSearch();
	    
	    // VBox containing the nodes for the books table
        booksTableVBox = new VBox(20);
        booksTableVBox.setPadding(new Insets(10, 10, 10, 10));
        booksTableVBox.getChildren().addAll(labelBookTable, searchTableVBox);
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
		
		booksTablePagination.updatePaginationSearch();
	}
    
}
