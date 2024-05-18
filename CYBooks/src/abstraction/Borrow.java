package abstraction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class Borrow represents a borrow made by the user
 * @author CYTech Student
 */
public class Borrow {
	protected static List<Borrow> allBorrows = new ArrayList<>();
	protected IntegerProperty usersID;
	protected StringProperty booksISBN;
	protected StringProperty borrowDate;
	protected StringProperty returnDate;
	protected StringProperty effectiveReturnDate;
	protected SimpleBooleanProperty late;
	protected List<String> problems;
    
    /**
     * Borrow constructor with all fields as parameters
     * @param user the user who borrowed the book
     * @param book the borrowed book
     * @param dateBorrow the borrow's date
     */
    public Borrow(User user, Book book, LocalDate dateBorrow) {
        this.usersID = new SimpleIntegerProperty(user.getId());
        this.booksISBN =new SimpleStringProperty(String.valueOf(book.getIsbn()));
        this.borrowDate = new SimpleStringProperty(dateBorrow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.returnDate = new SimpleStringProperty(dateBorrow.plusDays(Book.MAX_BORROW_TIME).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.effectiveReturnDate = new SimpleStringProperty("");
        this.late = new SimpleBooleanProperty(false);
        this.problems = new ArrayList<String>();
        allBorrows.add(this);
    }

    /**
     * Get the id of the user who borrowed the book
     * @return the id of the user who borrowed the book
     */
    public int getUsersID() {
        return usersID.get();
    }

    /**
     * user setter method
     * @param user the user who borrowed the book
     */
    public void setUsersID(User user) {
        this.usersID.set(user.getId());
    }
    
    /**
     * Method to return the IntegerProperty of the user's id for the TableView
     * @return the IntegerProperty of the user's id that borrowed a book
     */
    public IntegerProperty idProperty() {
        return usersID;
    }

    /**
     * Get the ISBN of the borrowed book
     * @return the ISBN of the borrowed book
     */
    public String getBooksISBN() {
        return booksISBN.get();
    }

    /**
     * Book's ISBN setter method
     * @param book new the borrowed book
     */
    public void setBook(Book book) {
        this.booksISBN.set(String.valueOf(book.getIsbn()));
    }
    
    /**
     * Method to return the StringProperty of the book's ISBN for the TableView
     * @return the IntegerProperty of the book's ISBN that been borrowed
     */
    public StringProperty isbnProperty() {
        return booksISBN;
    }

    /**
     * Get the borrow's date
     * @return the borrow's date
     */
    public String getDateBorrow() {
        return borrowDate.get();
    }

    /**
     * borrowDate setter method
     * @param borrowDate the date of the creation of the borrow
     */
    public void setDateBorrow(LocalDate borrowDate) {
        this.borrowDate.set(borrowDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    /**
     * Method to return the StringProperty of the borrow's date for the TableView
     * @return the StringProperty of the borrow's date
     */
    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    /**
     * Get the borrow's return date
     * @return returnDate the date of which the book need to be return
     */
    public String getReturnDate() {
        return returnDate.get();
    }

    /**
     * returnDate setter method
     * @param return the borrow's return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
    
    /**
     * Method to return the StringProperty of the borrow's return date for the TableView
     * @return the StringProperty of the borrow's return date
     */
    public StringProperty returnDateProperty() {
        return returnDate;
    }
    
    /**
     * Get the borrow's effective return date
     * @return effectiveReturnDate
     */
    public String getEffectiveReturnDate() {
		return effectiveReturnDate.get();
	}

    /**
     * effectiveReturnDate setter method
     */
	public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
		this.effectiveReturnDate.set(effectiveReturnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	}

	/**
     * Method to return the StringProperty of the borrow's effective return date for the TableView
     * @return the StringProperty of the borrow's effective return date
     */
    public StringProperty effectiveReturnDateProperty() {
        return effectiveReturnDate;
    }

	/**
	 * Get the borrow's state : late or not
	 * @return true if the borrow is late and false if not
	 */
	public boolean isLate() {
		return late.get();
	}
    
    /**
     * late setter method
     * @param late true or false if the borrow is late or not
     */
    public void setLate(boolean late) {
		this.late.set(late);
	}
    
    /**
     * Method to return the StringProperty of the borrow's effective return date for the TableView
     * @return the StringProperty of the borrow's effective return date
     */
    public SimpleBooleanProperty lateProperty() {
        return late;
    }

	/**
     * Get the borrow's problems
     * @return the borrow's problems
     */
    public List<String> getProblems() {
		return problems;
    }
    
    /**
     * Method to add a problem to the borrow
     * @param problem the problem we need to add
     */
	public void addProblem(String problem) {
		getProblems().add(problem);
	}
	
	/**
	 * Get the list of all the users
	 * @return the list of all the users
	 */
	public static List<Borrow> getAllBorrow() {
		return allBorrows;
	}
	
	/**
	 * Method to check is a borrow is late, if true, changed the late state to true
	 * @return true if the borrow is late and false if not
	 */
	public boolean isBorrowLate() {
		LocalDate today = LocalDate.now();
		
		if(today.isAfter(LocalDate.parse(getReturnDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")))) {
			this.setLate(true);
		}
		
		return isLate();
	}
	
	/**
	 * toString method for Borrow
	 * @return the string to print a borrow
	 */
    @Override
    public String toString() {
        String text =  "Borrow : " +
        		"\nUser's ID : "+ getUsersID() +
        		"\nBook's ISBN : " + getBooksISBN() +
        		"\nBorrow date : " + getDateBorrow() +
        		"\nReturn date : " + getReturnDate();
        if(getProblems().size() > 0) {
        	text += "\nProblems : " + getProblems();
        }
        return text;
    }
    
    /**
     * equals method to check if a object is equal to an specefic borrow
     * @param obj the object to check if equals
     * @return true if obj is equals to a specefic borrow and false if not
     */
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof Borrow)) {
    		return false;
    	}
    	Borrow b = (Borrow) obj;
    	return this.getUsersID() == b.getUsersID() && this.getBooksISBN().equals(b.getBooksISBN()) && this.getDateBorrow().equals(b.getDateBorrow()) &&
    			this.getReturnDate().equals(b.getReturnDate()) && this.getProblems().equals(b.getProblems());
    }
  
}
