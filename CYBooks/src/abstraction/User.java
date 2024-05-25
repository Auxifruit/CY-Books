package abstraction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import abstraction.db.DataBaseUser;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * The class User represents a generic user going to the library
 * @author CYTech Student
 */

public class User {
	protected static int MAX_BORROW_NUMBER = 3;
	protected static int counterId = 1;
	protected static List<User> allUser = new ArrayList<>();
	protected IntegerProperty id;
	protected StringProperty lastname;
	protected StringProperty firstname;
	protected StringProperty email;
	private StringProperty status;

    /**
     * User constructor without the id
     * @param lastname the last name of the user
     * @param firstname the first name of the user
     * @param email the email of the user
     */
    public User(String lastname, String firstname, String email) {
    	this.lastname = new SimpleStringProperty(lastname);
        this.firstname = new SimpleStringProperty(firstname);
        this.email = new SimpleStringProperty(email);
        this.id = new SimpleIntegerProperty(counterId++);
        this.status = new SimpleStringProperty(Status.PUNCTUAL.getText());
        allUser.add(this);
    }
    
    /**
     * User constructor with all fields as parameters
     * @param id the id of the user
     * @param lastname the last name of the user
     * @param firstname the first name of the user
     * @param email the email of the user
     * @param status the status of the user
     */
    public User(int id, String lastname, String firstname, String email, Status status) {
    	this(lastname, firstname, email);
    	this.id = new SimpleIntegerProperty(id);
    	this.status = new SimpleStringProperty(status.getText());
    }

    /**
     * Getter method for the user's ID
     * @return the user's ID
     */
    public int getId() {
        return id.get();
    }

    /**
     * Setter method for the user's id 
     * @param id the user's id
     */
    public void setId(int id) {
        this.id.set(id);;
    }
    
    /**
     * Method to return the IntegerProperty of the user's id for the TableView
     * @return the IntegerProperty of the user's id 
     */
    public IntegerProperty idProperty() {
        return id;
    }
    
    /**
     * Getter method for the user's last name
     * @return the user's last name
     */
    public String getLastname() {
        return lastname.get();
    }

    /**
     * Setter method for last name 
     * @param lastname the user's last name
     */
    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    /**
     * Method to return the StringProperty of the user's last name for the TableView
     * @return the StringProperty of the user's last name 
     */
    public StringProperty lastnameProperty() { 
    	return lastname;
    }
    
    /**
     * Getter method for the user's first name
     * @return the user's first name
     */
    public String getFirstname() {
        return firstname.get();
    }

    /**
     * Setter method for the user's first name 
     * @param firstname the user's first name
     */
    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    /**
     * Method to return the StringProperty of the user's first name for the TableView
     * @return the StringProperty of the user's first name 
     */
    public StringProperty firstnameProperty() { 
    	return firstname;
    }
    
    /**
     * Getter method for the user's email
     * @return the user's email
     */
    public String getEmail() {
		return email.get();
	}
    
    /**
     * Getter method for the user's email 
     * @param email the user's email
     */
	public void setEmail(String email) {
		this.email.set(email);
	}

	/**
     * Method to return the StringProperty of the user's e-mail for the TableView
     * @return the StringProperty of the user's e-mail
     */
    public StringProperty emailProperty() {
    	return email;
    }
    
    /**
     * Getter method for the user' status
     * @return the user' status
     */
    public String getStatus() {
    	return status.get();
    }
    
    /**
     * Setter method for for the user' status
     * @param status the user' status
     */
    public void setStatus(Status status) {
    	this.status.set(status.getText());
    }
    
    /**
     * Method to return the StringProperty of the user' status for the TableView
     * @return the StringProperty of the user' status
     */
    public StringProperty statusProperty() {
    	return status;
    }
	
	/**
	 * Getter method for the list of all the users
	 * @return the list of all the users
	 */
	public static List<User> getAllUser() {
		return allUser;
	}
	
	/**
	 * Getter method for the counterId of the class User
	 * @return the counterId of the class User
	 */
	public static int getCounterId() {
		return counterId;
	}
	
	/**
	 * Setter method for the counterId of the class User
	 */
	public static void setCounterId(int newCounterId) {
		counterId = newCounterId;
	}
	
	/**
	 * Getter method for the maximum number of borrow an user can make
	 * @return the maximum number of borrow an user can make
	 */
	public static int getMaxBorrowNumber() {
		return MAX_BORROW_NUMBER;
	}
	
	/**
	 * Method to check the user' statue
	 */
	public void checkUserStatus() {
		// We start from the fact the user is punctual
		Status status = Status.PUNCTUAL;
		
		for(Borrow b : Borrow.getAllBorrow()) {
			// The user has at least one late borrow
			if(!(b.equals(null)) && b.getUsersID() == this.getId() && b.isLate()) {
				status = Status.LATECOMER;
			}
		}
		
		this.setStatus(status);
		
		try {
			DataBaseUser.modifyUserInTable(this, this.getLastname(), this.getFirstname(), this.getEmail(), status.getText());
		} catch (SQLException e) {
			System.err.println("Fail to change the user' status in the database");
			Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while changing the user' status.", ButtonType.OK);
			errorAlert.showAndWait();
		}	
	}
	
	/**
	 * Method to check the status for all the users
	 */
	public static void checkAllUserStatus() {
		for(User u : User.getAllUser()) {
			u.checkUserStatus();
		}
	}
	
	/**
	 * Method to check if an user exist by its id
	 * @param id the user's id
	 * @return true if the user exist and false if not
	 */
	public static boolean isExisting(int id) {
		for(User u : User.getAllUser()) {
			if (u.getId() == id){
				return true ; 
			}
		}
		return false ;
	}
	
    /**
     * Get the user with its id 
     * @return the use that you want if he exist or null
     */
	public static User getUserById(int id) {
		for (User u : User.getAllUser()) {
			if (u.getId() == id) {
				return u;
			}
		}
		return null ;
	}
	
	/**
	 * toString method for User
	 * @return the string to print an user
	 */
    @Override
    public String toString() {
        return "User: " + 
        		"\nId : " + getId() +
        		"\nLastname : " + getLastname() +
        		"\nFirstname : "  + getFirstname() +
        		"\nE-mail : " + getEmail();
    }
    
    /**
     * equals method to check if a object is equal to an specefic User
     * @param obj the object to check if equals
     * @return true if obj is equals to a specefic User and false if not
     */
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof User)) {
    		return false;
    	}
    	User u = (User) obj;
    	return this.getFirstname().equals(u.getFirstname()) && this.getLastname().equals(u.getLastname()) && this.getEmail().equals(u.getEmail())
    			&& this.getId() == u.getId() && this.getStatus().equals(u.getStatus());
    }

}
