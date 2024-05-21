package control.bookControl;

import java.util.ArrayList;
import java.util.List;

import abstraction.API;
import abstraction.Book;
import presentation.BooksTable;
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
    private TextField languageTextField;
    private TextField identifierTextField;
    
    private ToggleGroup anyOrAllTitle;
    private ToggleGroup anyOrAllAuthor;
    private ToggleGroup anyOrAllType;
    private ToggleGroup anyOrAllDate;
    private ToggleGroup anyOrAllLanguage;
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
    public ConfirmSearchButtonHandler(BooksTable booksTable, TextField titleTextField, TextField authorTextField, TextField typeTextField, TextField dateTextField, TextField languageTextField, TextField identifierTextField, ToggleGroup anyOrAllTitle, ToggleGroup anyOrAllAuthor, ToggleGroup anyOrAllType, ToggleGroup anyOrAllDate, ToggleGroup anyOrAllLanguage, ToggleGroup anyOrAllIdentifier) {
    	this.booksTable = booksTable;
    	
        this.titleTextField = titleTextField;
        this.authorTextField = authorTextField;
        this.typeTextField = typeTextField;
        this.dateTextField = dateTextField;
        this.languageTextField = languageTextField;
        this.identifierTextField = identifierTextField;
        
        this.anyOrAllTitle = anyOrAllTitle;
        this.anyOrAllAuthor = anyOrAllAuthor;
        this.anyOrAllType = anyOrAllType;
        this.anyOrAllDate = anyOrAllDate;
        this.anyOrAllLanguage = anyOrAllLanguage;
        this.anyOrAllIdentifier = anyOrAllIdentifier;
    }
    
    public TextField getTitleTextField() {
		return titleTextField;
	}

	public TextField getAuthorTextField() {
		return authorTextField;
	}

	public TextField getTypeTextField() {
		return typeTextField;
	}

	public TextField getDateTextField() {
		return dateTextField;
	}
	
	public TextField getLanguageTextField() {
		return languageTextField;
	}
	
	public TextField getIdentifierTextField() {
		return identifierTextField;
	}
	
	public ToggleGroup getAnyOrAllTitle() {
		return anyOrAllTitle;
	}

	public ToggleGroup getAnyOrAllAuthor() {
		return anyOrAllAuthor;
	}

	public ToggleGroup getAnyOrAllType() {
		return anyOrAllType;
	}

	public ToggleGroup getAnyOrAllDate() {
		return anyOrAllDate;
	}

	public ToggleGroup getAnyOrAllLanguage() {
		return anyOrAllLanguage;
	}
	
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
		String languageText = getLanguageTextField().getText();
		String identifierText = getIdentifierTextField().getText();
		
		// We check if at least one field is filled
		if (titleText.isEmpty() && authorText.isEmpty() && typeText.isEmpty() && dateText.isEmpty() && languageText.isEmpty() && identifierText.isEmpty()) {
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
			String languageRadio = ((RadioButton) getAnyOrAllLanguage().getSelectedToggle()).getText();
			String identifierRadio = ((RadioButton) getAnyOrAllIdentifier().getSelectedToggle()).getText();

			// We instantiate the Strings value for the query
	    	String title = "dc.title " + titleRadio + " \"" + titleText + "\"";
	    	String author = "dc.creator " + authorRadio + " \""  + authorText + "\"";    	
	    	String type = "dc.type " + typeRadio + " \""  + typeText + "\"";    	
	    	String date = "dc.date " + dateRadio + " \""  + dateText + "\"";    	
	    	String language = "dc.language "  + languageRadio + " \""  + languageText + "\"";    	
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
	    	if (!languageText.isEmpty()) {
	    		query.add(language);
	    	}
	    	if (!identifierText.isEmpty()) {
	    		query.add(identifier);
	    	}
	    	
	    	// We reset the TextFields value after the query
	    	getTitleTextField().setText("");
	    	getAuthorTextField().setText("");
	    	getTypeTextField().setText("");
	    	getDateTextField().setText("");
	    	getLanguageTextField().setText("");
	    	getIdentifierTextField().setText("");
	    		    	
	    	System.out.println("We searching books...");
	    	
	    	List<Book> bookList = API.searchBook(query.toArray(new String[0]), 60);
	    	
	    	System.out.println("Searching finished !");
	    	
	    	booksTable.updateData(bookList);

	    	Alert searchBookAlert = new Alert(Alert.AlertType.CONFIRMATION, "The search is finished, you can check the books table.", ButtonType.OK);
	    	searchBookAlert.setTitle("Books search confirmation");
	    	searchBookAlert.showAndWait();
		}
	}
}
