package control;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * Method to handle the pagination of a TableView
 * @param <T> the TableView's type
 */
public class TablePagination<T> {
    private Pagination pagination;
    private TableView<T> tableView;
    private ObservableList<T> data;
    private FilteredList<T> filteredData;
    public static final int ROWS_PER_PAGE = 15;

    /**
     * Constructor for the TablePagination class
     * @param tableView the TableView displaying the informations
     * @param data the data containing the informations
     */
    public TablePagination(TableView<T> tableView, ObservableList<T> data) {
        this.tableView = tableView;
        this.data = data;
        initializePagination();
    }
    
    /**
     * Constructor for the TablePagination class when we can search in the table
     * @param tableView the TableView displaying the informations
     * @param data the data containing the informations
     * @param filteredData the data containing the filtered informations
     */
    public TablePagination(TableView<T> tableView, ObservableList<T> data, FilteredList<T> filteredData) {
        this.tableView = tableView;
        this.data = data;
        this.filteredData = filteredData;
        initializePaginationSearch();
    }
    
    /**
     * Getter method to get the pagination
     * @return the pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Method to initialize the pagination
     */
    private void initializePagination() {
        pagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
        pagination.setPageFactory(this::createPage);

        // Update table view and pagination when the data changes
        data.addListener((ListChangeListener<T>) change -> updatePagination());

        // Initial update
        updatePagination();
    }

    /**
	 * Method to create pages
	 * @param pageIndex the index of the page
	 * @return the BorderPane containing the usersTable with the correct items 
	 */
	private Node createPage(int pageIndex) {
		// We calculate the first index and the last index a the page 
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        
        // We use a sublist and set it to the table view
        // It allows us to display only the users between the corresponding index
        tableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(tableView);
    }

	/**
	 * Method to update the pagination of our table
	 */
    public void updatePagination() {
    	// We calculate the number of page necessary to display all the information
        int totalPage = (int) (Math.ceil(data.size() * 1.0 / ROWS_PER_PAGE));
        
        // We set this number to the number of page and go to the first page
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        
        // We change the table to match the new pagination
        changeTableView(0, ROWS_PER_PAGE);
        
        // If we change page, the table will change
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
    }
    
    /**
     * Method to initialize the pagination when we can search in the table
     */
    private void initializePaginationSearch() {
        pagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
        pagination.setPageFactory(this::createPage);

        // Update table view and pagination when the data changes
        data.addListener((ListChangeListener<T>) change -> updatePaginationSearch());
        
        // Initial update
        updatePaginationSearch();
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
        SortedList<T> sortedData = new SortedList<>(FXCollections.observableArrayList(data.subList(Math.min(fromIndex, minIndex), minIndex)));
        
        // We link the element to allow the update of borrowsTable
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }
    
    /**
	 * Method to update the pagination of our table when we can search in the table
	 */
    public void updatePaginationSearch() {
    	// We calculate the number of page necessary to display all the information
    	int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / ROWS_PER_PAGE));
        
    	// We set this number to the number of page and go to the first page
    	pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        
        // We change the table to match the new pagination
        changeTableViewSearch(0, ROWS_PER_PAGE);
        
        // If we change page, the table will change
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> changeTableView(newValue.intValue(), ROWS_PER_PAGE));
    }

    /**
	 * Method to change with a certain index and limit in order to display the right element when we can search in the table
	 * @param index represent the index of the current page
	 * @param limit correspond the limit of element to display
	 */
    private void changeTableViewSearch(int index, int limit) {
    	// We calculate the starting index and check if we don't try to access an element outside the list limits
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, data.size());

        // Ensure that we don't take more elements than are available in filteredData
        int minIndex = Math.min(toIndex, filteredData.size());
        
        // We use a SortedList to keep the order of the element of the subList
        SortedList<T> sortedData = new SortedList<>(FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        
        // We link the element to allow the update of usersTable
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }
}
