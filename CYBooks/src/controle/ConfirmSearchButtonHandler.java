package controle;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * The class to handle the event of the button to search a book
 */
public class ConfirmSearchButtonHandler implements EventHandler<ActionEvent> {
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
     * @param authorTextField author's name
     * @param typeTextField book's type
     * @param dateTextField book's published date
     * @param identifierTextField book's identifier
     */
    public ConfirmSearchButtonHandler(TextField titleTextField, TextField authorTextField, TextField typeTextField, TextField dateTextField, TextField languageTextField, TextField identifierTextField, ToggleGroup anyOrAllTitle, ToggleGroup anyOrAllAuthor, ToggleGroup anyOrAllType, ToggleGroup anyOrAllDate, ToggleGroup anyOrAllLanguage, ToggleGroup anyOrAllIdentifier) {
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

	@Override
    public void handle(ActionEvent event) {
		
		String titleRadio = ((RadioButton) getAnyOrAllTitle().getSelectedToggle()).getText();
		String authorRadio = ((RadioButton) getAnyOrAllAuthor().getSelectedToggle()).getText();
		String typeRadio = ((RadioButton) getAnyOrAllType().getSelectedToggle()).getText();
		String dateRadio = ((RadioButton) getAnyOrAllDate().getSelectedToggle()).getText();
		String languageRadio = ((RadioButton) getAnyOrAllLanguage().getSelectedToggle()).getText();
		String identifierRadio = ((RadioButton) getAnyOrAllIdentifier().getSelectedToggle()).getText();
		
    	String title = "dc.title " + titleRadio + " \"" + getTitleTextField().getText() + "\"";
    	String author = "dc.creator " + authorRadio + " \""  + getAuthorTextField().getText() + "\"";    	
    	String type = "dc.type " + typeRadio + " \""  + getTypeTextField().getText() + "\"";    	
    	String date = "dc.date " + dateRadio + " \""  + getDateTextField().getText() + "\"";    	
    	String language = "dc.language "  + languageRadio + " \""  + getLanguageTextField().getText() + "\"";    	
    	String identifier = "dc.identifier "  + identifierRadio + " \""  + getIdentifierTextField().getText() + "\"";
    	
    	List<String> query = new ArrayList<>();
    	
    	query.add(title);
    	query.add(author);
    	query.add(type);
    	query.add(date);
    	query.add(language);
    	query.add(identifier);
    	
    	System.out.println(query);
	}
}
