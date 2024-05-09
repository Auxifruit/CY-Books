package presentation;

import abstraction.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AppliLibrary extends Application {
	private TableView<User> table = new TableView<User>();
	private final ObservableList<User> data =
		    FXCollections.observableArrayList(
		    	User.getAllUser()
		    );

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Table View Sample");
	    stage.setWidth(450);

	    final Label label = new Label("Users");
	    label.setFont(new Font("Arial", 20));

	    TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
	    firstNameCol.setMinWidth(100);
	    firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));

	    TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
	    lastNameCol.setMinWidth(100);
	    lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));

	    TableColumn<User, String> emailCol = new TableColumn<>("Email");
	    emailCol.setMinWidth(200);
	    emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

	    table.setItems(data);
	    table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

	    Button bouton = new Button("Change firstname");
	    
	    bouton.setOnAction(e -> {
	    	User.getAllUser().get(0).setFirstname("NEW FIRSTNAME");
	    });
	    
	    final VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(label, table, bouton);

	    stage.setScene(new Scene(new Group(vbox)));
	    stage.show();
	}

	public static void main(String[] args) {
		User user1 = new User("Mark", "Evans", "Mark@gmail.com");
		User user2 = new User("Axel", "Blaze", "Axel@gmail.com");
		launch(args);
	}
}
