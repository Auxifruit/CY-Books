package abstraction;

import java.util.ArrayList;
import java.util.List;

/**
 * The class User represents a generic user going to the library
 * @author CYTech Student
 */

public class User {
	protected static int MAX_BORROW_NUMBER = 3;
	protected static int compteurId = 1;
	protected static List<User> allUser = new ArrayList<>();
	protected int id;
	protected String lastname;
	protected String firstname;
	protected String email;
	protected List<Borrow> borrowHistory;

    /**
     * User constructor with all fields as parameters
     * @param lastname the lastname of the user
     * @param firstname the firstname of the user
     * @param email the email of the user
     */
    public User(String lastname, String firstname, String email) {
    	this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.id = compteurId++;
        allUser.add(this);
    }

    /**
     * Method to search an user in the user list
     * @param id the user's id we want to find
     * @return a user if we found it or null if not
     */
    public static User searchUserWithId(int id) {
        for (User user : allUser) {
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
        return lastname;
    }

    /**
     * astname setter method
     * @param lastname the user's lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get the user's firstname
     * @return the user's firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * firstname setter method
     * @param firstname the user's firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the user's email
     * @return the user's email
     */
    public String getEmail() {
		return email;
	}
    /**
     * email setter method
     * @param email the user's email
     */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
     * Get the user's borrow history
     * @return the user's borrow history
     */
	public List<Borrow> getBorrowHistory() {
		return borrowHistory;
	}

	/**
	 * toString method for User
	 * @return the string to print an user
	 */
    @Override
    public String toString() {
        return "Utilisater: " + 
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
    			&& this.getId() == u.getId() && this.getBorrowHistory().equals(u.getBorrowHistory());
    }

}
