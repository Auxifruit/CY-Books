package presentation.borrowPresentation;

import abstraction.API;
import abstraction.Book;
import abstraction.Borrow;
import abstraction.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * The class containing the pane to display the borrow's information
 */
public class BorrowInformation {
	private VBox borrowInformationVBox;
	
	private Borrow borrowToDisplay;
	
	Label usersIDValue = new Label("");
    Label usersFirstnameValue = new Label("");
    Label usersLastnameValue = new Label("");
    Label usersEmailValue = new Label("");
    Label usersStatusValue = new Label("");
	
	Label booksIDValue = new Label("");
	Label booksTitleValue = new Label("");
	Label booksAuthorValue = new Label("");
	Label booksPublishedDateValue = new Label("");
	Label booksFormatValue = new Label("");
	Label booksTypeValue = new Label("");
	Label booksPublisherValue = new Label("");
	
	Label borrowDateValue = new Label("");
    Label borrowReturnDateValue = new Label("");
    Label borrowEffectiveReturnDateValue = new Label("none");

	/**
     * Constructor of the BorrowInformation class
     * @param borrowsTable the table view displaying the borrow's informations
     */
    public BorrowInformation() {
    	this.borrowToDisplay = null;

        createBorrowInformationPane();
    }
    
    /**
     * Getter method to get the borrow to display
     * @return the class' borrow to display
     */
    public Borrow getBorrowToDisplayy() {
    	return borrowToDisplay;
    }
    
    /**
     * Setter method to set the borrow to display
     * @param user the new borrow to display
     */
    public void setBorrowToDisplay(Borrow borrowToDisplay) {
    	this.borrowToDisplay = borrowToDisplay;
    }
    
    /**
	 * Getter to get the VBox containing all the element for the borrow's information
	 * @return the the VBox containing all the element for the borrow's information
	 */
    public VBox getBorrowsInformationsVBox() {
    	return borrowInformationVBox;
    }
    
    /**
	 * Method to create a pane to display the borrow's information
	 * @return the pane to display the borrow's information
	 */
	protected void createBorrowInformationPane() {
		Label borrowInformationLabel = new Label("BORROW'S INFORMATION :");
	    borrowInformationLabel.setFont(new Font("Arial", 24));
	    borrowInformationLabel.setUnderline(true);
	    borrowInformationLabel.setStyle("-fx-font-weight: bold;");
	    
		// VBox containing all the user's informations
	    VBox usersInfosContainer = new VBox(15);
	    
	    Label usersInfosLabel = new Label("User's information :");
	    usersInfosLabel.setFont(new Font("Arial", 16));
	    usersInfosLabel.setUnderline(true);
	    usersInfosLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox usersIDAndValue = new HBox();
	    HBox usersFirsnameAndValue = new HBox();
	    HBox usersLastnameAndValue = new HBox();
	    HBox usersEmailAndValue = new HBox();
	    HBox usersStatusAndValue = new HBox();
	    
	    Label usersID = new Label("- ID : ");
	    Label usersFirstname = new Label("- First name : ");
	    Label usersLastname = new Label("- Last name : ");
	    Label usersEmail = new Label("- E-mail : ");
	    Label usersStatus = new Label("- Status : ");
	    
	    usersID.setStyle("-fx-font-weight: bold;");
	    usersFirstname.setStyle("-fx-font-weight: bold;");
	    usersLastname.setStyle("-fx-font-weight: bold;");
	    usersEmail.setStyle("-fx-font-weight: bold;");
	    usersStatus.setStyle("-fx-font-weight: bold;");
	    
	    // We had in each HBox the info's name and its value
	    usersIDAndValue.getChildren().addAll(usersID, usersIDValue);
	    usersFirsnameAndValue.getChildren().addAll(usersFirstname, usersFirstnameValue);
	    usersLastnameAndValue.getChildren().addAll(usersLastname, usersLastnameValue);
	    usersEmailAndValue.getChildren().addAll(usersEmail, usersEmailValue);
	    usersStatusAndValue.getChildren().addAll(usersStatus, usersStatusValue);
	    
	    // We add the node useful for the user's information
	    usersInfosContainer.getChildren().addAll(usersInfosLabel, usersIDAndValue, usersFirsnameAndValue, usersLastnameAndValue, usersEmailAndValue, usersStatusAndValue);
	    
	    // VBox containing all the book's informations
	    VBox booksInfosContainer = new VBox(15);
	    
	    Label booksInfosLabel = new Label("Book's information :");
	    booksInfosLabel.setFont(new Font("Arial", 16));
	    booksInfosLabel.setUnderline(true);
	    booksInfosLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox booksIDAndValue = new HBox();
	    HBox booksTitleAndValue = new HBox();
	    HBox booksAuthorAndValue = new HBox();
	    HBox booksPublishedDateAndValue = new HBox();
	    HBox booksFormatAndValue = new HBox();
	    HBox booksTypeAndValue = new HBox();
	    HBox booksPublisherAndValue = new HBox();
	    
	    Label booksID = new Label("- Identifier : ");
	    Label booksTitle = new Label("- Title : ");
	    Label booksAuthor = new Label("- Author : ");
	    Label booksPublishedDate = new Label("- Published date : ");
	    Label booksFormat = new Label("- Format : ");
	    Label booksType = new Label("- Type : ");
	    Label booksPublisher = new Label("- Editor : ");
	    
	    booksID.setStyle("-fx-font-weight: bold;");
	    booksTitle.setStyle("-fx-font-weight: bold;");
	    booksAuthor.setStyle("-fx-font-weight: bold;");
	    booksPublishedDate.setStyle("-fx-font-weight: bold;");
	    booksFormat.setStyle("-fx-font-weight: bold;");
	    booksType.setStyle("-fx-font-weight: bold;");
	    booksPublisher.setStyle("-fx-font-weight: bold;");
	    
	   
	    
	    // We had in each HBox the info's name and its value
	    booksIDAndValue.getChildren().addAll(booksID, booksIDValue);
	    booksTitleAndValue.getChildren().addAll(booksTitle, booksTitleValue);
	    booksAuthorAndValue.getChildren().addAll(booksAuthor, booksAuthorValue);
	    booksPublishedDateAndValue.getChildren().addAll(booksPublishedDate, booksPublishedDateValue);
	    booksFormatAndValue.getChildren().addAll(booksFormat, booksFormatValue);
	    booksTypeAndValue.getChildren().addAll(booksType, booksTypeValue);
	    booksPublisherAndValue.getChildren().addAll(booksPublisher, booksPublisherValue);
	    
	    // We add the node useful for the user's information
	    booksInfosContainer.getChildren().addAll(booksInfosLabel, booksIDAndValue, booksTitleAndValue, booksAuthorAndValue, booksPublishedDateAndValue, booksFormatAndValue, booksTypeAndValue, booksPublisherAndValue);
	    
	    // VBox containing all the borrow's informations
	    VBox borrowsInfos = new VBox(15);
	    
	    Label borrowsInfoLabel = new Label("Borrow's information :");
	    borrowsInfoLabel.setFont(new Font("Arial", 16));
	    borrowsInfoLabel.setUnderline(true);
	    borrowsInfoLabel.setStyle("-fx-font-weight: bold;");
	    
	    HBox borrowDateAndValue = new HBox();
	    HBox borrowReturnDateAndValue = new HBox();
	    HBox borrowEffectiveReturnDateAndValue = new HBox();
	    
	    Label borrowDate = new Label("- Date : ");
	    Label borrowReturnDate = new Label("- Return date : ");
	    Label oldBorrowEffectiveReturnDate = new Label("- Effective return date : ");
	    
	    borrowDate.setStyle("-fx-font-weight: bold;");
	    borrowReturnDate.setStyle("-fx-font-weight: bold;");
	    oldBorrowEffectiveReturnDate.setStyle("-fx-font-weight: bold;");
	    
	    borrowDateAndValue.getChildren().addAll(borrowDate, borrowDateValue);
	    borrowReturnDateAndValue.getChildren().addAll(borrowReturnDate, borrowReturnDateValue);
	    borrowEffectiveReturnDateAndValue.getChildren().addAll(oldBorrowEffectiveReturnDate, borrowEffectiveReturnDateValue);
	    
	    // We add the node useful for the borrow's information
	    borrowsInfos.getChildren().addAll(borrowsInfoLabel, borrowDateAndValue, borrowReturnDateAndValue, borrowEffectiveReturnDateAndValue);
	    
	    // HBox containing the user's infos, the book's infos and the borrow's infos
	    HBox userAndBookInfos = new HBox();
	    userAndBookInfos.setAlignment(Pos.CENTER);
	    
	    // These 2 spacer ensure that all the element take the maximum of space possible
	    Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
	    
	    userAndBookInfos.getChildren().addAll(usersInfosContainer, spacer1, booksInfosContainer, spacer2, borrowsInfos);
	    
	    // VBox containing the nodes for the user modification
	    borrowInformationVBox = new VBox(25);
	    borrowInformationVBox.setPadding(new Insets(10, 10, 10, 10));
	    borrowInformationVBox.getChildren().addAll(borrowInformationLabel, userAndBookInfos);
	    borrowInformationVBox.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * Method to update the label with the user, book and borrow's information
	 */
	public void updateInfo() {
		// If the borrow is null we can't display its informations and that of the user and the book
		if(borrowToDisplay != null) {

			//We update the borrow's informations
			borrowDateValue.setText(borrowToDisplay.getDateBorrow());
        	borrowReturnDateValue.setText(borrowToDisplay.getReturnDate());
        	if(borrowToDisplay.getEffectiveReturnDate().equals("")) {
        		borrowEffectiveReturnDateValue.setText(borrowToDisplay.getEffectiveReturnDate());
        	}
        	else {
        		borrowEffectiveReturnDateValue.setText("none");
        	}
        	
        	// If the user is null we can't display its informations
        	User userToDisplay = User.getUserById(borrowToDisplay.getUsersID());
        	
        	//We update the user's informations
    		if(userToDisplay != null) {
    			usersIDValue.setText(String.valueOf(userToDisplay.getId()));
	           	usersFirstnameValue.setText(userToDisplay.getFirstname());
	           	usersLastnameValue.setText(userToDisplay.getLastname());
	          	usersEmailValue.setText(userToDisplay.getEmail());
	          	usersStatusValue.setText(userToDisplay.getStatus());
    		}
    		
    		// If the Book is null we can't display its informations
    		Book bookToDisplay = API.getOneBookFromID(borrowToDisplay.getBooksIdentifier());
    		
    		//We update the book's informations
    		if(bookToDisplay != null) {
    			booksIDValue.setText(bookToDisplay.getIdentifier());
    			booksTitleValue.setText(bookToDisplay.getTitle());
    			booksAuthorValue.setText(bookToDisplay.getAuthor());
    			booksPublishedDateValue.setText(bookToDisplay.getPublishedDate());
    			booksFormatValue.setText(bookToDisplay.getFormat());
    			booksTypeValue.setText(bookToDisplay.getType());
    			booksPublisherValue.setText(bookToDisplay.getPublisher());
    		}
    	}
	}
}
