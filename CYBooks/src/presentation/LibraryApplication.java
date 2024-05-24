package presentation;

import abstraction.Book;
import abstraction.db.DataBaseManage;
import presentation.bookPresentation.BookSearch;
import presentation.bookPresentation.BooksTable;
import presentation.bookPresentation.MostBorrowedBook;
import presentation.borrowPresentation.BorrowInformation;
import presentation.borrowPresentation.BorrowModification;
import presentation.borrowPresentation.BorrowsTable;
import presentation.borrowPresentation.ProblemsTable;
import presentation.userPresentation.UserCreation;
import presentation.userPresentation.UserModification;
import presentation.userPresentation.UserProfile;
import presentation.userPresentation.UsersTable;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The class of the library application
 */
public class LibraryApplication extends Application {
	// The main pane of the application
	private BorderPane mainBorderPane;
	// The VBox containing the buttons to change between the center and bottom of the main BorderPane
	private HBox containerButtonChangeCenterAndBottomApp;
	
	// The class containing the pane and the table for the users
	private UsersTable usersTable;
	// The class containing the pane to create an user
	private UserCreation usersCreation;
	// The class containing the pane to modify an user
	private UserModification userModification;
	// The class containing the pane to display the user's profile
	private UserProfile userProfile;
	// The VBox containing the buttons to change the center of the main BorderPane to Users oriented Pane
	private VBox containerButtonChangeCenterUsersApp;
	
	// The class containing the pane and the table for the borrows
	private BorrowsTable borrowsTable;
	// The class containing the pane to modify a borrow
	private BorrowModification borrowModification;
	// The class containing the pane to display the borrow's informations
	private BorrowInformation borrowInformation;
	// The class containing the pane to display the borrow's problem
	private ProblemsTable problemsTable;
	// The VBox containing the buttons to change the center of the main BorderPane to Borrows oriented Pane
	private VBox containerButtonChangeCenterBorrowsApp;
	
	// The class containing the pane and the table for the books
	private BooksTable booksTable;
	// The class containing the pane to enter a book's information to search it
	private BookSearch bookSearch;
	// The class containing the pane and the table for the most borrowed books of the last 30 days
	private MostBorrowedBook mostBorrowedBook;
	// The VBox containing the buttons to change the center of the main BorderPane to Books oriented Pane
	private VBox containerButtonChangeCenterBooksApp;

	/**
	 * Method to start the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("CY-Books");
	    stage.setWidth(1100);
	    stage.setHeight(750);
		
	    DataBaseManage.createTables();
	    
		mainBorderPane = new BorderPane();
		
		// HBox containing all the button to change between User oriented scenes
		containerButtonChangeCenterAndBottomApp = createButtonChangeCenterAndBottomApp();
	    
		// User Table pane
	    usersTable = new UsersTable();
	    
	    // User Creation pane
	    usersCreation = new UserCreation(usersTable);

	    // User Modification pane
	    userModification = new UserModification(usersTable.getUsersTable());
	    
 		// User's Profile
	    userProfile = new UserProfile();
		
		// Borrow Table pane
 		borrowsTable = new BorrowsTable();
 		
 		// Borrow Modification pane
 		borrowModification = new BorrowModification(borrowsTable.getBorrowsTable());
 		
 		// Borrow's information pane
 		borrowInformation = new BorrowInformation();

 		// Borrow's problem pane
 		problemsTable = new ProblemsTable();
 		
 		// Borrow Table pane
 		booksTable = new BooksTable(new ArrayList<Book>());

 		// Book search pane
 		bookSearch = new BookSearch(booksTable);
 		
 		// Most borrowed book Pane
 		mostBorrowedBook = new MostBorrowedBook();
	    
    	// The VBox containing the buttons to change the center of the main BorderPane to Users oriented Pane
 		containerButtonChangeCenterUsersApp = createButtonChangeCenterUsers();
 		
 		containerButtonChangeCenterBooksApp = createButtonChangeCenterBooks();
 		
 		// The VBox containing the buttons to change the center of the main BorderPane to Borrows oriented Pane
 		containerButtonChangeCenterBorrowsApp = createButtonChangeCenterBorrows();
 		
	    // Main scene of the application
	    mainBorderPane.setLeft(containerButtonChangeCenterAndBottomApp);
	    stage.setScene(new Scene(mainBorderPane));
	    stage.show();
	}	
	
	/**
	 * Method to create a VBox containing the buttons to change the center of the main BorderPane to Users oriented Pane
	 * @return the VBox with the buttons to change the center of the main BorderPane to Users oriented Pane
	 */
	private VBox createButtonChangeCenterUsers() {
		VBox allContainer = new VBox(10);
		
	 	HBox buttonsContainer = new HBox(50);
	 	buttonsContainer.setAlignment(Pos.CENTER);
	 	buttonsContainer.setPrefHeight(50);

		Separator sep = new Separator();
		sep.setOrientation(Orientation.HORIZONTAL);
 		
 		// Buttons to change the BorderPane's center
 		Button goToUsersTableButton = new Button("Users table");
 		goToUsersTableButton.setOnAction(e -> {
 			mainBorderPane.setCenter(usersTable.getUsersTableVBox());
 	    });
 		
 		Button goToUserCreationButton = new Button("Create an user");
 	    goToUserCreationButton.setOnAction(e -> {
 	    	mainBorderPane.setCenter(usersCreation.getUserCreationVBox());
 	    });
 	    
 	    Button goToUserModificationButton = new Button("Modify the user");
 	    goToUserModificationButton.setOnAction(e -> {
 	    	mainBorderPane.setCenter(userModification.getUsersModificationVBox());
 	    });
 	    
 	    // If no user is selected, the button is disable
 	    goToUserModificationButton.disableProperty().bind(Bindings.isEmpty(usersTable.getUsersTable().getSelectionModel().getSelectedItems()));
 	    
 	    Button goToUserProfile = new Button("Check profile");
 	    goToUserProfile.setOnAction(e -> {
 	    	userProfile.setUserToDisplay(usersTable.getUsersTable().getSelectionModel().getSelectedItem());
 	    	userProfile.updateData();
	    	mainBorderPane.setCenter(userProfile.getUsersProfileVBox());	    	
	    });
	    
	    // If no user is selected, the button is disable
	    goToUserProfile.disableProperty().bind(Bindings.isEmpty(usersTable.getUsersTable().getSelectionModel().getSelectedItems()));

	    buttonsContainer.getChildren().addAll(goToUsersTableButton, goToUserCreationButton, goToUserModificationButton, goToUserProfile);
 	   
	    allContainer.getChildren().addAll(sep, buttonsContainer);
	    allContainer.setPadding(new Insets(10, 10, 10, 10));
 	   	
 	   	return allContainer;
	}
	
	/**
	 * Method to create a VBox containing the buttons to change the center of the main BorderPane to Borrows oriented Pane
	 * @return the VBox with the buttons to change the center of the main BorderPane to Borrows oriented Pane
	 */
	private VBox createButtonChangeCenterBorrows() {
		VBox allContainer = new VBox(10);
		
		Separator sep = new Separator();
		sep.setOrientation(Orientation.HORIZONTAL);
		
		HBox buttonsContainer = new HBox(50);
		buttonsContainer.setAlignment(Pos.CENTER);
		buttonsContainer.setPrefHeight(50);
		
		// Buttons to change the BorderPane's center
		Button goToUsersTableButton = new Button("Borrows table");
		goToUsersTableButton.setOnAction(e -> {
			mainBorderPane.setCenter(borrowsTable.getBorrowsTableVBox());
		});
		
		Button goToBorrowsModificationButton = new Button("Modify a borrow");
		goToBorrowsModificationButton.setOnAction(e -> {
			mainBorderPane.setCenter(borrowModification.getBorrowsModificationVBox());
		});
		
		// If no borrow is selected, the button is disable
		goToBorrowsModificationButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItems()));
		
		Button goToBorrowInformations = new Button("Check borrow's information");
		goToBorrowInformations.setOnAction(e -> {
			borrowInformation.setBorrowToDisplay(borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItem());
			borrowInformation.updateInfo();
	    	mainBorderPane.setCenter(borrowInformation.getBorrowsInformationsVBox());
	    });
	    
	    // If no borrow is selected, the button is disable
		goToBorrowInformations.disableProperty().bind(Bindings.isEmpty(borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItems()));
		
		// Button to see the borrow's problem
	    Button problemBorrowButton = new Button("Check borrow's problems");
	    problemBorrowButton.setOnAction(e -> {
	    	problemsTable.setBorrowToDisplay(borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItem());
	    	problemsTable.updateData();
	    	mainBorderPane.setCenter(problemsTable.getProblemsTableVBox());
	    });
	    
	    // If no borrow is selected, the button is disable
	    problemBorrowButton.disableProperty().bind(Bindings.isEmpty(borrowsTable.getBorrowsTable().getSelectionModel().getSelectedItems()));
		
		buttonsContainer.getChildren().addAll(goToUsersTableButton, goToBorrowsModificationButton, goToBorrowInformations, problemBorrowButton);
		 
		allContainer.getChildren().addAll(sep, buttonsContainer);
		allContainer.setPadding(new Insets(10, 10, 10, 10));
		
		return allContainer;
	}
	
	public VBox createButtonChangeCenterBooks() {
		VBox allContainer = new VBox(10);
		
		Separator sep = new Separator();
		sep.setOrientation(Orientation.HORIZONTAL);
		
		HBox buttonsContainer = new HBox(50);
		buttonsContainer.setAlignment(Pos.CENTER);
		buttonsContainer.setPrefHeight(50);
		
		// Buttons to change the BorderPane's center
		Button goToBooksTableButton = new Button("Books table");
		goToBooksTableButton.setOnAction(e -> {
			mainBorderPane.setCenter(booksTable.getBooksTableVBox());
		});
		
		Button goToSearchBookButton = new Button("Search books");
		goToSearchBookButton.setOnAction(e -> {
			mainBorderPane.setCenter(bookSearch.getCommandContainer());
		});

		Button goToMostBorrowedBooks = new Button("Most borrowed books");
		goToMostBorrowedBooks.setOnAction(e -> {
			mainBorderPane.setCenter(mostBorrowedBook.getBooksTableVBox());
		});  
		
		buttonsContainer.getChildren().addAll(goToSearchBookButton, goToBooksTableButton, goToMostBorrowedBooks);
		 
		allContainer.getChildren().addAll(sep, buttonsContainer);
		allContainer.setPadding(new Insets(10, 10, 10, 10));
		
		return allContainer;
	}
	
	/**
	 * Method to create a HBox containing the buttons to change the center and the bottom of the main BorderPane 
	 * @return the HBox with the buttons to change the center and the bottom of the main BorderPane
	 */
	private HBox createButtonChangeCenterAndBottomApp() {
		HBox allContainer = new HBox(10);
		
		Separator sep = new Separator();
		sep.setOrientation(Orientation.VERTICAL);
		
		VBox buttonContainer = new VBox(50);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPrefHeight(50);
		
		// Buttons to change the BorderPane's center and bottom
		Button goToUsers = new Button("Users");
		goToUsers.setOnAction(e -> {
			mainBorderPane.setCenter(usersTable.getUsersTableVBox());
			mainBorderPane.setBottom(containerButtonChangeCenterUsersApp);
			usersTable.updateData();
			userProfile.updateData();
		});
		
		Button goToBorrow = new Button("Borrows");
		goToBorrow.setOnAction(e -> {
			mainBorderPane.setCenter(borrowsTable.getBorrowsTableVBox());
			mainBorderPane.setBottom(containerButtonChangeCenterBorrowsApp);
			borrowsTable.updateData();
		});
		
		Button goToBooks = new Button("Books");
		goToBooks.setOnAction(e -> {
			mainBorderPane.setCenter(bookSearch.getCommandContainer());
			mainBorderPane.setBottom(containerButtonChangeCenterBooksApp);
		});
		
		buttonContainer.getChildren().addAll(goToUsers, goToBorrow, goToBooks);
		
		allContainer.getChildren().addAll(buttonContainer, sep);
		allContainer.setPadding(new Insets(10, 10, 10, 10));
		   
		return allContainer;
	}

	public static void main(String[] args) {	
		launch(args);
	}
}
