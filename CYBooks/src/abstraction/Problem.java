package abstraction;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to represent a borrow's problem
 */
public class Problem {
	protected static int counterId = 1;
	protected static List<Problem> allProblems = new ArrayList<>();
	protected IntegerProperty id;
	protected IntegerProperty borrowsID;
	protected StringProperty text;
	
	/**
	 * Constructor for the class Problem with some fields
	 * @param borrowsID the borrowsID we want the problem to refer to
	 * @param text the problem's text
	 */
	public Problem(int borrowsID, String text) {
		this.borrowsID = new SimpleIntegerProperty(borrowsID);
		this.text = new SimpleStringProperty(text);
		this.id = new SimpleIntegerProperty(counterId++);
		allProblems.add(this);
	}
	
	/**
	 * Constructor for the class Problem with all the field
	 * @param borrowsID the borrowsID we want the problem to refer to
	 * @param text the problem's text
	 * @param id the problem's id
	 */
	public Problem(int borrowsID, String text, int id) {
		this(borrowsID, text);
		this.id.set(id);
	}

	/**
	 * Getter method to get the problem's id
	 * @return the problem's id 
	 */
	public int getID() {
		return id.get();
	}
	
	/**
     * Method to return the IntegerProperty of the problem's id
     * @return the IntegerProperty of the problem's id
     */
	public IntegerProperty idProperty() {
		return id;
	}
	
	/**
	 * Getter method to get the borrow's id the problem refer to
	 * @return the borrow's id the problem refer to 
	 */
	public int getBorrowsID() {
		return borrowsID.get();
	}
	
	/**
     * Method to return the IntegerProperty of the borrow's id the problem refer to
     * @return the IntegerProperty of the borrow's id the problem refer to
     */
	public IntegerProperty booksIdentifierProperty() {
		return borrowsID;
	}

	/**
	 * Getter method to get the problem's text
	 * @return the problem's text
	 */
	public String getText() {
		return text.get();
	}
	
	/**
     * Method to return the StringProperty of the problem's text
     * @return the StringProperty of the problem's text
     */
	public StringProperty textProperty() {
		return text;
	}
	
	/**
	 * Get the list of all the problems
	 * @return the list of all the problems
	 */
	public static List<Problem> getAllProblems() {
		return allProblems;
	}
	
    /**
	 * Setter method for the counterId of the class Problem
	 */
	public static void setCounterId(int newCounterId) {
		counterId = newCounterId;
	}
	
}
