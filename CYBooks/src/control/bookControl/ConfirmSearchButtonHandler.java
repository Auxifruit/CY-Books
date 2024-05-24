package control.bookControl;

import abstraction.API;
import abstraction.Book;
import presentation.bookPresentation.BooksTable;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

/**
 * The class to handle the event of the button to search a book
 */
public class ConfirmSearchButtonHandler implements EventHandler<ActionEvent> {
	private BooksTable booksTable;
	
    private TextField titleTextField;
    private TextField authorTextField;
    private TextField typeTextField;
    private TextField dateTextField;
    private TextField formatTextField;
    private TextField identifierTextField;
    
    private ToggleGroup anyOrAllTitle;
    private ToggleGroup anyOrAllAuthor;
    private ToggleGroup anyOrAllType;
    private ToggleGroup anyOrAllDate;
    private ToggleGroup anyOrAllFormat;
    private ToggleGroup anyOrAllIdentifier;

    /**
     * Constructor for ConfirmSearchButtonHandler
     * @param booksTitleLabel book's title
     * @param booksTable the data and TableView with all the books
     * @param authorTextField author's name
     * @param typeTextField book's type
     * @param dateTextField book's published date
     * @param identifierTextField book's identifier
     */
    /**
     * Constructor for ConfirmSearchButtonHandler
     * @param booksTable the TableView containing the books
     * @param titleTextField the TextField containing the book's title
     * @param authorTextField the TextField containing the book's author
     * @param typeTextField the TextField containing the book's type
     * @param dateTextField the TextField containing the book's date
     * @param formatTextField the TextField containing the book's format
     * @param identifierTextField the TextField containing the book's identifier
     * @param anyOrAllTitle the ToggleGroup containing the result of any or all for the book's title
     * @param anyOrAllAuthor the ToggleGroup containing the result of any or all for the book's author
     * @param anyOrAllType the ToggleGroup containing the result of any or all for the book's type
     * @param anyOrAllDate the ToggleGroup containing the result of any or all for the book's date
     * @param anyOrAllFormat the ToggleGroup containing the result of any or all for the book's format
     * @param anyOrAllIdentifier the ToggleGroup containing the result of any or all for the book's identifier
     */
    public ConfirmSearchButtonHandler(BooksTable booksTable, TextField titleTextField, TextField authorTextField, TextField typeTextField, TextField dateTextField, TextField formatTextField, TextField identifierTextField, ToggleGroup anyOrAllTitle, ToggleGroup anyOrAllAuthor, ToggleGroup anyOrAllType, ToggleGroup anyOrAllDate, ToggleGroup anyOrAllFormat, ToggleGroup anyOrAllIdentifier) {
    	this.booksTable = booksTable;
    	
        this.titleTextField = titleTextField;
        this.authorTextField = authorTextField;
        this.typeTextField = typeTextField;
        this.dateTextField = dateTextField;
        this.formatTextField = formatTextField;
        this.identifierTextField = identifierTextField;
        
        this.anyOrAllTitle = anyOrAllTitle;
        this.anyOrAllAuthor = anyOrAllAuthor;
        this.anyOrAllType = anyOrAllType;
        this.anyOrAllDate = anyOrAllDate;
        this.anyOrAllFormat = anyOrAllFormat;
        this.anyOrAllIdentifier = anyOrAllIdentifier;
    }
    
    /**
     * Getter method for the book's title TextField
     * @return the book's title TextField
     */
    public TextField getTitleTextField() {
		return titleTextField;
	}

    /**
     * Getter method for the book's author TextField
     * @return the book's author TextField
     */
	public TextField getAuthorTextField() {
		return authorTextField;
	}

	/**
     * Getter method for the book's type TextField
     * @return the book's type TextField
     */
	public TextField getTypeTextField() {
		return typeTextField;
	}

	/**
     * Getter method for the book's date TextField
     * @return the book's date TextField
     */
	public TextField getDateTextField() {
		return dateTextField;
	}
	
	/**
     * Getter method for the book's format TextField
     * @return the book's format TextField
     */
	public TextField getFormatTextField() {
		return formatTextField;
	}
	
	/**
     * Getter method for the book's identifier TextField
     * @return the book's identifier TextField
     */
	public TextField getIdentifierTextField() {
		return identifierTextField;
	}
	
	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's title
	 * @return the ToggleGroup containing the result of any or all for the book's title
	 */
	public ToggleGroup getAnyOrAllTitle() {
		return anyOrAllTitle;
	}

	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's author
	 * @return the ToggleGroup containing the result of any or all for the book's author
	 */
	public ToggleGroup getAnyOrAllAuthor() {
		return anyOrAllAuthor;
	}

	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's type
	 * @return the ToggleGroup containing the result of any or all for the book's type
	 */
	public ToggleGroup getAnyOrAllType() {
		return anyOrAllType;
	}

	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's date
	 * @return the ToggleGroup containing the result of any or all for the book's date
	 */
	public ToggleGroup getAnyOrAllDate() {
		return anyOrAllDate;
	}

	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's format
	 * @return the ToggleGroup containing the result of any or all for the book's format
	 */
	public ToggleGroup getAnyOrAllFormat() {
		return anyOrAllFormat;
	}
	
	/**
	 * Getter method for the ToggleGroup containing the result of any or all for the book's identifier
	 * @return the ToggleGroup containing the result of any or all for the book's identifier
	 */
	public ToggleGroup getAnyOrAllIdentifier() {
		return anyOrAllIdentifier;
	}

	/**
     * Method to handle the reception of the informations
     */
	@Override
    public void handle(ActionEvent event) {
		// We get the value for each TextField
		String titleText = getTitleTextField().getText();
		String authorText = getAuthorTextField().getText();
		String typeText = getTypeTextField().getText();
		String dateText = getDateTextField().getText();
		String formatText = getFormatTextField().getText();
		String identifierText = getIdentifierTextField().getText();
		
		// We check if at least one field is filled
		if (titleText.isEmpty() && authorText.isEmpty() && typeText.isEmpty() && dateText.isEmpty() && formatText.isEmpty() && identifierText.isEmpty()) {
			Alert errormodificationUserAlert = new Alert(AlertType.WARNING, "You need to fill at least one field", ButtonType.OK);
    		errormodificationUserAlert.setTitle("Empty fields");
    		errormodificationUserAlert.showAndWait();
    	}
		else {
			// We get the value for each RadioButton
			String titleRadio = ((RadioButton) getAnyOrAllTitle().getSelectedToggle()).getText();
			String authorRadio = ((RadioButton) getAnyOrAllAuthor().getSelectedToggle()).getText();
			String typeRadio = ((RadioButton) getAnyOrAllType().getSelectedToggle()).getText();
			String dateRadio = ((RadioButton) getAnyOrAllDate().getSelectedToggle()).getText();
			String formatRadio = ((RadioButton) getAnyOrAllFormat().getSelectedToggle()).getText();
			String identifierRadio = ((RadioButton) getAnyOrAllIdentifier().getSelectedToggle()).getText();

			// We instantiate the Strings value for the query
	    	String title = "dc.title " + titleRadio + " \"" + titleText + "\"";
	    	String author = "dc.creator " + authorRadio + " \""  + authorText + "\"";    	
	    	String type = "dc.type " + typeRadio + " \""  + typeText + "\"";    	
	    	String date = "dc.date " + dateRadio + " \""  + dateText + "\"";    	
	    	String format = "dc.format "  + formatRadio + " \""  + formatText + "\"";    	
	    	String identifier = "dc.identifier "  + identifierRadio + " \""  + identifierText + "\"";
	    	
	    	// query will stock the values
	    	List<String> query = new ArrayList<>();
	    	
	    	// If certain fields are empty we don't add them to the query
	    	if (!titleText.isEmpty()) {
	    		query.add(title);
	    	}
	    	if (!authorText.isEmpty()) {
	    		query.add(author);
	    	}
	    	if (!typeText.isEmpty()) {
	    		query.add(type);
	    	}
	    	if (!dateText.isEmpty()) {
	    		query.add(date);
	    	}
	    	if (!formatText.isEmpty()) {
	    		query.add(format);
	    	}
	    	if (!identifierText.isEmpty()) {
	    		query.add(identifier);
	    	}
	    	
	    	// We reset the TextFields value after the query
	    	getTitleTextField().setText("");
	    	getAuthorTextField().setText("");
	    	getTypeTextField().setText("");
	    	getDateTextField().setText("");
	    	getFormatTextField().setText("");
	    	getIdentifierTextField().setText("");
	    		    	
	    	System.out.println("We searching books...");
	    	
	    	// We get the list of book returned by the API call
	    	List<Book> bookList = API.searchBook(query.toArray(new String[0]), 60);
	    	
	    	System.out.println("Searching finished !");
	    	
	    	booksTable.updateData(bookList);

	    	Alert searchBookAlert = new Alert(Alert.AlertType.CONFIRMATION, "The search is finished, you can check the books table.", ButtonType.OK);
	    	searchBookAlert.setTitle("Books search confirmation");
	    	searchBookAlert.showAndWait();
		}
	}
}
