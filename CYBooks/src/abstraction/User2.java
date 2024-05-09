package abstraction;

import java.util.ArrayList;
import java.util.List;

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
	protected int id;
	protected StringProperty lastname;
	protected StringProperty firstname;
	protected StringProperty email;
	protected List<Borrow> borrowHistory = new ArrayList<>();

    /**
     * User constructor with all fields as parameters
     * @param lastname the lastname of the user
     * @param firstname the firstname of the user
     * @param email the email of the user
     */
    public User2(String lastname, String firstname, String email) {
    	this.lastname = new SimpleStringProperty(lastname);
        this.firstname = new SimpleStringProperty(firstname);
        this.email = new SimpleStringProperty(email);
        this.id = compteurId++;
        allUser.add(this);
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
        return id;
    }

    /**
     * id setter method
     * @param id the user's id
     */
    public void setId(int id) {
        this.id = id;
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

    public StringProperty lastnameProperty() { return lastname; }
    
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

    public StringProperty firstnameProperty() { return firstname; }
    
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

    public StringProperty emailProperty() { return email; }
	
	/**
     * Get the user's borrow history
     * @return the user's borrow history
     */
	public List<Borrow> getBorrowHistory() {
		return borrowHistory;
	}
	
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
