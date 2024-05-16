package presentation;

import controle.ConfirmSearchButtonHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookSearch extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		VBox commandContainer = new VBox(10);
		
		// Title 
		HBox titleCommand = new HBox(10);
		Label booksTitleLabel = new Label("Title : ");
		TextField titleTextField = new TextField();
		ToggleGroup anyOrAllTitle = new ToggleGroup();
	    RadioButton buttonAnyTitle = new RadioButton("any");
	    RadioButton buttonAllTitle = new RadioButton("all");
		
		titleTextField.setPromptText("Book's title");		
		
		buttonAnyTitle.setToggleGroup(anyOrAllTitle);
		buttonAnyTitle.setSelected(true);
		buttonAllTitle.setToggleGroup(anyOrAllTitle);
		
		titleCommand.getChildren().addAll(booksTitleLabel, buttonAnyTitle, buttonAllTitle, titleTextField);
		
		// Author
		HBox authorCommand = new HBox(10);
		Label authorLabel = new Label("Author : ");
		TextField authorTextField = new TextField();
		ToggleGroup anyOrAllAuthor = new ToggleGroup();
	    RadioButton buttonAnyAuthor = new RadioButton("any");
	    RadioButton buttonAllAuthor = new RadioButton("all");
		
	    authorTextField.setPromptText("Book's author's full name");		
		
	    buttonAnyAuthor.setToggleGroup(anyOrAllAuthor);
	    buttonAnyAuthor.setSelected(true);
	    buttonAllAuthor.setToggleGroup(anyOrAllAuthor);	    
		
		authorCommand.getChildren().addAll(authorLabel, buttonAnyAuthor, buttonAllAuthor, authorTextField);
		
		// HBox formatCommand = new HBox(10);

		// Type
		HBox typeCommand = new HBox(10);
		Label typeLabel = new Label("Type : ");
		TextField typeTextField = new TextField();
		ToggleGroup anyOrAllType = new ToggleGroup();
	    RadioButton buttonAnyType = new RadioButton("any");
	    RadioButton buttonAllType = new RadioButton("all");
		
	    typeTextField.setPromptText("Book's type");		
		
		buttonAnyType.setToggleGroup(anyOrAllType);
		buttonAnyType.setSelected(true);
		buttonAllType.setToggleGroup(anyOrAllType);
	    		
		typeCommand.getChildren().addAll(typeLabel, buttonAnyType, buttonAllType, typeTextField);

		// Date
		HBox dateCommand = new HBox(10);
		Label dateLabel = new Label("Date : ");
		TextField dateTextField = new TextField();
		ToggleGroup anyOrAllDate = new ToggleGroup();
	    RadioButton buttonAnyDate = new RadioButton("any");
	    RadioButton buttonAllDate = new RadioButton("all");
		
	    dateTextField.setPromptText("Book's published date");		
		
		buttonAnyDate.setToggleGroup(anyOrAllDate);
		buttonAnyDate.setSelected(true);
		buttonAllDate.setToggleGroup(anyOrAllDate);
		
		dateCommand.getChildren().addAll(dateLabel, buttonAnyDate, buttonAllDate, dateTextField);
		
		// Language
		HBox languageCommand = new HBox(10);
		Label languageLabel = new Label("Language : ");
		TextField languageTextField = new TextField();
		ToggleGroup anyOrAllLanguage = new ToggleGroup();
	    RadioButton buttonAnyLanguage = new RadioButton("any");
	    RadioButton buttonAllLanguage = new RadioButton("all");
		
	    languageTextField.setPromptText("Book's language");		
		
		buttonAnyLanguage.setToggleGroup(anyOrAllLanguage);
		buttonAnyLanguage.setSelected(true);
		buttonAllLanguage.setToggleGroup(anyOrAllLanguage);
		
		languageCommand.getChildren().addAll(languageLabel, buttonAnyLanguage, buttonAllLanguage, languageTextField);
		
		// Identifier
		HBox identifierCommand = new HBox(10);
		Label identifierLabel = new Label("Identifier : ");
		TextField identifierTextField = new TextField();
		ToggleGroup anyOrAllIdentifier = new ToggleGroup();
	    RadioButton buttonAnyIdentifier = new RadioButton("any");
	    RadioButton buttonAllIdentifier = new RadioButton("all");
		
	    identifierTextField.setPromptText("Book's identifier");		
	    
		buttonAnyIdentifier.setToggleGroup(anyOrAllIdentifier);
		buttonAnyIdentifier.setSelected(true);
		buttonAllIdentifier.setToggleGroup(anyOrAllIdentifier);
				
		identifierCommand.getChildren().addAll(identifierLabel, buttonAnyIdentifier, buttonAllIdentifier, identifierTextField);
		
		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(new ConfirmSearchButtonHandler(titleTextField, authorTextField, typeTextField, dateTextField, languageTextField, identifierTextField, anyOrAllTitle, anyOrAllAuthor, anyOrAllType, anyOrAllDate, anyOrAllLanguage, anyOrAllIdentifier));
				
		commandContainer.getChildren().addAll(titleCommand, authorCommand, typeCommand, dateCommand, languageCommand, identifierCommand, confirmButton);
		
		stage.setScene(new Scene(commandContainer));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
