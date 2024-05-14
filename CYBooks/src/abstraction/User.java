package abstraction;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class User represents a generic user going to the library
 * @author CYTech Student
 */

public class User2 {
	protected static int MAX_BORROW_NUMBER = 3;
	protected static int compteurId = 1;
	protected static List<User2> allUser = new ArrayList<>();
	protected IntegerProperty id;
	protected StringProperty lastname;
	protected StringProperty firstname;
	protected StringProperty email;
	protected List<Borrow> borrowHistory = new ArrayList<>();

    /**
     * User constructor without the id
     * @param lastname the lastname of the user
     * @param firstname the firstname of the user
     * @param email the email of the user
     */
    public User2(String lastname, String firstname, String email) {
    	this.lastname = new SimpleStringProperty(lastname);
        this.firstname = new SimpleStringProperty(firstname);
        this.email = new SimpleStringProperty(email);
        this.id = new SimpleIntegerProperty(compteurId++);
        allUser.add(this);
    }
    
    /**
     * User constructor with all fields as parameters
     * @param id the id of the user
     * @param lastname the lastname of the user
     * @param firstname the firstname of the user
     * @param email the email of the user
     */
    public User2(int id, String lastname, String firstname, String email) {
    	this(lastname, firstname, email);
    	this.id = new SimpleIntegerProperty(id);
    }

    /**
     * Method to search an user in the user list
     * @param id the user's id we want to find
     * @return a user if we found it or null if not
     */
    public static User2 searchUserWithId(int id) {
        for (User2 user : allUser) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * Get the user's ID
     * @return the user's ID
     */
    public int getId() {
        return id.get();
    }

    /**
     * id setter method
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
     * Get the user's lastname
     * @return the user's lastname
     */
    public String getLastname() {
        return lastname.get();
    }

    /**
     * astname setter method
     * @param lastname the user's lastname
     */
    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    /**
     * Method to return the StringProperty of the user's lastname for the TableView
     * @return the StringProperty of the user's lastname 
     */
    public StringProperty lastnameProperty() { 
    	return lastname;
    }
    
    /**
     * Get the user's firstname
     * @return the user's firstname
     */
    public String getFirstname() {
        return firstname.get();
    }

    /**
     * firstname setter method
     * @param firstname the user's firstname
     */
    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    /**
     * Method to return the StringProperty of the user's firstname for the TableView
     * @return the StringProperty of the user's firstname 
     */
    public StringProperty firstnameProperty() { 
    	return firstname;
    }
    
    /**
     * Get the user's email
     * @return the user's email
     */
    public String getEmail() {
		return email.get();
	}
    /**
     * email setter method
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
     * Get the user's borrow history
     * @return the user's borrow history
     */
	public List<Borrow> getBorrowHistory() {
		return borrowHistory;
	}
	
	/**
	 * Get the list of all the users
	 * @return the list of all the users
	 */
	public static List<User2> getAllUser() {
		return allUser;
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
    	if(!(obj instanceof User2)) {
    		return false;
    	}
    	User2 u = (User2) obj;
    	return this.getFirstname().equals(u.getFirstname()) && this.getLastname().equals(u.getLastname()) && this.getEmail().equals(u.getEmail())
    			&& this.getId() == u.getId() && this.getBorrowHistory().equals(u.getBorrowHistory());
    }

}
