package presentation;

import java.time.LocalDate;

import abstraction.Book;
import abstraction.Borrow;
import abstraction.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The class of the users management application
 */
public class LibraryApplication extends Application {
	// The main pane of the application
	private BorderPane mainBorderPane;
	// The VBox containing the buttons to change between the center and bottom of the main BorderPane
	private VBox vboxButtoChangeCenterAndBottomApp;
	
	// The class containing the pane and the table for the users
	private UsersTable usersTable;
	// The class containing the pane to create an user
	private UserCreation usersCreation;
	// The class containing the pane to modify an user
	private UserModification userModification;
	// The HBox containing the buttons to change the center of the main BorderPane to Users oriented Pane
	private HBox hboxButtonChangeCenterUserApp;
	
	// The class containing the pane and the table for the borrows
	private BorrowsTable borrowsTable;
	// The HBox containing the buttons to change the center of the main BorderPane to Borrows oriented Pane
	private HBox hboxButtonChangeSceneBorrowsApp;
	
	// The class containing the pane to enter a book's information to search it
	private BookSearch bookSearch;

	/**
	 * Method to start the application
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Users table view");
	    stage.setWidth(1000);
	    stage.setHeight(700);
		
		mainBorderPane = new BorderPane();
		
		// HBox containing all the button to change between User oriented scenes
		vboxButtoChangeCenterAndBottomApp = createButtonChangeCenterAndBottomApp();
	    
		// User Table pane
	    usersTable = new UsersTable();
	    
	    // User Creation pane
	    usersCreation = new UserCreation(usersTable.getData(), usersTable.getUsersTable(), usersTable.getUsersTablePagination());

	    // User Modification pane
	    userModification = new UserModification(usersTable.getUsersTable());
	    
    	// HBox containing all the button to change between User oriented scenes
 		hboxButtonChangeCenterUserApp = createButtonChangeSceneUsers();
	    
 		// Borrow Table pane
 		borrowsTable = new BorrowsTable();
 		
 		// Book search pane
 		bookSearch = new BookSearch();
 		
 		hboxButtonChangeSceneBorrowsApp = createButtonChangeSceneBorrows();
 		
	    // Main scene of the application
	    mainBorderPane.setLeft(vboxButtoChangeCenterAndBottomApp);
	    stage.setScene(new Scene(mainBorderPane));
	    stage.show();
	}	
	
	/**
	 * Method to create a HBox containing the buttons to change the center of the main BorderPane to Users oriented Pane
	 * @return the HBox with the buttons to change the center of the main BorderPane to Users oriented Pane
	 */
	private HBox createButtonChangeSceneUsers() {
	 	HBox hboxUsers = new HBox(50);
	 	hboxUsers.setAlignment(Pos.CENTER);
	 	hboxUsers.setPrefHeight(50);
 		
 		// Buttons to change scene
 		Button goToUsersTableButton = new Button("Users table");
 		goToUsersTableButton.setOnAction(e -> {
 			mainBorderPane.setCenter(usersTable.getUsersTableVBox());
 	    });
 		
 		Button goToUserCreationButton = new Button("Create an user");
 	    goToUserCreationButton.setOnAction(e -> {
 	    	mainBorderPane.setCenter(usersCreation.getUserCreationVBox());
 	    });
 	    
 	    Button goToUserModificationButton = new Button("Modify a user");
 	    goToUserModificationButton.setOnAction(e -> {
 	    	mainBorderPane.setCenter(userModification.getUsersModificationVBox());
 	    });

 	   hboxUsers.getChildren().addAll(goToUsersTableButton, goToUserCreationButton, goToUserModificationButton);
 	   
 	   return hboxUsers;
	}
	
	/**
	 * Method to create a HBox containing the buttons to change the center of the main BorderPane to Borrows oriented Pane
	 * @return the HBox with the buttons to change the center of the main BorderPane to Borrows oriented Pane
	 */
	private HBox createButtonChangeSceneBorrows() {
		HBox hboxBorrows = new HBox(50);
		hboxBorrows.setAlignment(Pos.CENTER);
		hboxBorrows.setPrefHeight(50);
		
		// Buttons to change scene
		Button goToUsersTableButton = new Button("Borrows table");
		goToUsersTableButton.setOnAction(e -> {
			mainBorderPane.setCenter(borrowsTable.getBorrowsTableVBox());
		});
		
		Button goToBorrowsCreationButton = new Button("Create a borrow");
		goToBorrowsCreationButton.setOnAction(e -> {
		});
		goToBorrowsCreationButton.setDisable(true);
		
		Button goToBorrowsModificationButton = new Button("Modify a borrow");
		goToBorrowsModificationButton.setOnAction(e -> {
		});
		goToBorrowsModificationButton.setDisable(true);
		
		hboxBorrows.getChildren().addAll(goToUsersTableButton, goToBorrowsCreationButton, goToBorrowsModificationButton);
		   
		return hboxBorrows;
	}
	
	/**
	 * Method to create a VBox containing the buttons to change the center and the bottom of the main BorderPane 
	 * @return the VBox with the buttons to change the center and the bottom of the main BorderPane
	 */
	private VBox createButtonChangeCenterAndBottomApp() {
		VBox vboxApp = new VBox(50);
		vboxApp.setAlignment(Pos.CENTER);
		vboxApp.setPrefHeight(50);
		
		// Buttons to change scene
		Button goToUsers = new Button("Users");
		goToUsers.setOnAction(e -> {
			mainBorderPane.setCenter(usersTable.getUsersTableVBox());
			mainBorderPane.setBottom(hboxButtonChangeCenterUserApp);
		});
		
		Button goToBorrow = new Button("Borrows");
		goToBorrow.setOnAction(e -> {
			mainBorderPane.setCenter(borrowsTable.getBorrowsTableVBox());
			mainBorderPane.setBottom(hboxButtonChangeSceneBorrowsApp);
		});
		
		Button goToBooks = new Button("Books");
		goToBooks.setOnAction(e -> {
			mainBorderPane.setCenter(bookSearch.getCommandContainer());
		});  
		
		vboxApp.getChildren().addAll(goToUsers, goToBorrow, goToBooks);
		   
		return vboxApp;
	}

	public static void main(String[] args) {
		// FOR TESTING	
		Book book1 = new Book(123, "Les misérables", "Victor Hugo", "1862-01-01", "Tragédie", "Roman", "Albert Lacroix");
		Book book2 = new Book(5475, "L'étranger", "Albert Camus", "1942-05-19", "Tragédie", "Fiction", "Éditions Gallimard");
		
		for(int i = 0; i < 12; i++) {
			User user1 = new User("Mark", "Evans", "Mark@gmail.com");

			new Borrow(user1, book1, LocalDate.now());
		}
		
		for(int i = 0; i < 9; i++) {
			User user2 = new User("Axel", "Blaze", "Axel@gmail.com");
			
			new Borrow(user2, book2, LocalDate.now()).setLate(true);
		}
				
		launch(args);
	}
}
