package abstraction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The class Borrow represents a borrow made by the user
 * @author CYTech Student
 */
public class Borrow {
	protected static int counterId = 1;
	protected static List<Borrow> allBorrows = new ArrayList<>();
	protected IntegerProperty id;
	protected IntegerProperty usersID;
	protected StringProperty booksIdentifier;
	protected StringProperty borrowDate;
	protected StringProperty returnDate;
	protected StringProperty effectiveReturnDate;
	protected LongProperty duration;
	protected SimpleBooleanProperty late;
	
    /**
     * Borrow constructor without all the fields
     * @param userID the ID of the user who borrowed the book
     * @param bookIdentifier the Identifier of the borrowed book
     * @param dateBorrow the borrow's date
     */
    public Borrow(int usersID, String booksIdentifier, LocalDate dateBorrow) {
        this.usersID = new SimpleIntegerProperty(usersID);
        this.booksIdentifier =new SimpleStringProperty(booksIdentifier);
        this.borrowDate = new SimpleStringProperty(dateBorrow.toString());
        this.returnDate = new SimpleStringProperty(dateBorrow.plusDays(Book.MAX_BORROW_TIME).toString());
        this.effectiveReturnDate = new SimpleStringProperty("");
        this.late = new SimpleBooleanProperty(false);
        this.id = new SimpleIntegerProperty(counterId++);
        this.duration = new SimpleLongProperty(ChronoUnit.DAYS.between(getDateBorrowLocalDate(), LocalDate.now()));
        allBorrows.add(this);
    }
    
    /**
     * Borrow constructor with all fields as parameters except the effectiveReturnDate
     * @param userID the ID of the user who borrowed the book
     * @param bookIdentifier the Identifier of the borrowed book
     * @param dateBorrow the borrow's date
     * @param returnDate the borrow's return date
     * @param late the statue of the borrow
     */
    public Borrow(int id, int usersID, String booksIdentifier, LocalDate dateBorrow, LocalDate returnDate, long duration, boolean late) {
        this(usersID, booksIdentifier, dateBorrow);
        this.returnDate = new SimpleStringProperty(returnDate.toString());
        this.late = new SimpleBooleanProperty(late);
        this.duration = new SimpleLongProperty(duration);
        this.id = new SimpleIntegerProperty(id);
    }
    
    /**
     * Borrow constructor with all fields as parameters
     * @param userID the ID of the user who borrowed the book
     * @param bookIdentifier the Identifier of the borrowed book
     * @param dateBorrow the borrow's date
     * @param returnDate the borrow's return date
     * @param effectiveReturnDate the borrow's effective return date
     * @param late the statue of the borrow
     */
    public Borrow(int id, int usersID, String booksIdentifier, LocalDate dateBorrow, LocalDate returnDate, LocalDate effectiveReturnDate, long duration, boolean late) {
        this(usersID, booksIdentifier, dateBorrow);
        this.returnDate = new SimpleStringProperty(returnDate.toString());
        this.effectiveReturnDate = new SimpleStringProperty(effectiveReturnDate.toString());
        this.duration = new SimpleLongProperty(duration);
        this.late = new SimpleBooleanProperty(late);
        this.id = new SimpleIntegerProperty(id);
    }

    /**
     * Get the borrow's ID
     * @return the borrow's ID
     */
    public int getId() {
        return id.get();
    }

    /**
     * id setter method
     * @param id the borrow's id
     */
    public void setId(int id) {
        this.id.set(id);
    }
    
    /**
     * Method to return the IntegerProperty of the borrow's id for the TableView
     * @return the IntegerProperty of the borrow's id 
     */
    public IntegerProperty idProperty() {
        return id;
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
    public IntegerProperty userIDProperty() {
        return usersID;
    }

    /**
     * Get the Identifier of the borrowed book
     * @return the Identifier of the borrowed book
     */
    public String getBooksIdentifier() {
        return booksIdentifier.get();
    }

    /**
     * Book's Identifier setter method
     * @param book new the borrowed book
     */
    public void setBook(Book book) {
        this.booksIdentifier.set(String.valueOf(book.getIdentifier()));
    }
    
    /**
     * Method to return the StringProperty of the book's Identifier for the TableView
     * @return the IntegerProperty of the book's Identifier that been borrowed
     */
    public StringProperty isbnProperty() {
        return booksIdentifier;
    }

    /**
     * Get the borrow's date in String
     * @return the borrow's date in String
     */
    public String getDateBorrow() {
        return borrowDate.get();
    }
    
    /**
     * Get the borrow's date in LocalDate
     * @return the borrow's date in LocalDate
     */
    public LocalDate getDateBorrowLocalDate() {		
		return GeneralUtils.stringToLocalDate(this.getDateBorrow());
    }

    /**
     * borrowDate setter method
     * @param borrowDate the date of the creation of the borrow
     */
    public void setDateBorrow(LocalDate borrowDate) {
        this.borrowDate.set(borrowDate.toString());
    }
    
    /**
     * Method to return the StringProperty of the borrow's date for the TableView
     * @return the StringProperty of the borrow's date
     */
    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    /**
     * Get the borrow's return date in String
     * @return returnDate the date of which the book need to be return in String
     */
    public String getReturnDate() {
        return returnDate.get();
    }
    
    /**
     * Get the borrow's return date in LocalDate
     * @return the borrow's return date in LocalDate
     */
    public LocalDate getReturnDateLocalDate() {
		return GeneralUtils.stringToLocalDate(this.getReturnDate());
    }

    /**
     * returnDate setter method
     * @param returnDate the borrow's return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate.set(returnDate.toString());
    }
    
    /**
     * Method to return the StringProperty of the borrow's return date for the TableView
     * @return the StringProperty of the borrow's return date
     */
    public StringProperty returnDateProperty() {
        return returnDate;
    }
    
    /**
     * Get the borrow's effective return date in String
     * @return effectiveReturnDate the date of which the book have been returned in String
     */
    public String getEffectiveReturnDate() {
		return effectiveReturnDate.get();
	}
    
    /**
     * Get the borrow's effective return date in LocalDate
     * @return the borrow's effective return date in LocalDate
     */
    public LocalDate getEffectiveReturnDateLocalDate() {
    	// We get the return date in a string
    	String effectiveReturnDateString = this.getEffectiveReturnDate();
    	
    	if(!(effectiveReturnDateString.equals(""))) {
    		return GeneralUtils.stringToLocalDate(effectiveReturnDateString);
    	}
		
		return null;
    }

    /**
     * effectiveReturnDate setter method
     */
	public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
		if(effectiveReturnDate != null) {
			this.effectiveReturnDate.set(effectiveReturnDate.toString());
		}
		else {
			this.effectiveReturnDate.set(""); ;
		}
	}

	/**
     * Method to return the StringProperty of the borrow's effective return date for the TableView
     * @return the StringProperty of the borrow's effective return date
     */
    public StringProperty effectiveReturnDateProperty() {
        return effectiveReturnDate;
    }
    
    /**
     * duration getter method
     * @return the borrow's duration in days
     */
    public long getDuration() {
    	return duration.get();
    }
    
    /**
     * duration setter method
     * @param duration the borrow's duration in days
     */
    public void setDuration(long duration) {
    	this.duration.set(duration);
    }
    
    /**
     * Method to return the LongProperty of the borrow's duration for the TableView
     * @return the LongProperty of the borrow's duration
     */
    public LongProperty durationProperty() {
    	return duration;
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
	 * Get the list of all the users
	 * @return the list of all the users
	 */
	public static List<Borrow> getAllBorrow() {
		return allBorrows;
	}
	
	/**
	 * Get the counterId of the class Borrow
	 * @return the counterId of the class Borrow
	 */
	public static int getCounterId() {
		return counterId;
	}
	
	/**
	 * Setter for the counterId of the class Borrow
	 */
	public static void setCounterId(int newCounterId) {
		counterId = newCounterId;
	}
	
	/**
	 * Method to update the duration after a modification
	 */
	public void updateDuration() {
		if(getEffectiveReturnDate().equals("")) {
			// If the borrow is on going the duration is between the borrow's date and today
			setDuration(ChronoUnit.DAYS.between(getDateBorrowLocalDate(), LocalDate.now()));
		}
		else {
			// If the borrow is on going the duration is between the borrow's date and the borrow's effective return date
			setDuration(ChronoUnit.DAYS.between(getDateBorrowLocalDate(), getEffectiveReturnDateLocalDate()));
		}
	}
	
	/**
	 * Method to check is a borrow is late, if true, changed the late state to true
	 */
	public void checkBorrowLate() {
		LocalDate today = LocalDate.now();
		
		if((this.getEffectiveReturnDateLocalDate() == null && today.isAfter(this.getReturnDateLocalDate())
				|| (this.getEffectiveReturnDateLocalDate() != null && this.getEffectiveReturnDateLocalDate().isAfter(this.getReturnDateLocalDate())))) {
			this.setLate(true);
			
			int usersID = this.getUsersID();
			User u = User.getUserById(usersID);
			u.setStatus(Status.LATECOMER);
		}
		else {
			this.setLate(false);
			int usersID = this.getUsersID();
			User u = User.getUserById(usersID);
			u.setStatus(Status.PUNCTUAL);
		}
	}
	
	/**
	 * Method to check the statue of all the borrows
	 * @return 
	 */
	public static void checkAllborrowsLate() {
		for(Borrow b : Borrow.getAllBorrow()) {
			if(!(b.equals(null))) {
				b.checkBorrowLate();
			}
		}
	}
	
	/**
	 * Method to remove a borrow from all the borrow by using the user's ID
	 * @param userID the user's ID we use to delete borrow
	 */
	public static void removeBorrowByUsersID(int usersID) {
		Iterator<Borrow> iterator = getAllBorrow().iterator();
		
		while(iterator.hasNext()) {
			Borrow b = iterator.next();
			if(b.getUsersID() == usersID) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Method to remove a borrow from all the borrow by using the borrow's ID
	 * @param borrowsID the borrow's ID we use to delete borrow
	 */
	public static void removeBorrowByBorrowsID(int borrowsID) {
		Iterator<Borrow> iterator = getAllBorrow().iterator();
		
		while(iterator.hasNext()) {
			Borrow b = iterator.next();
			if(b.getId() == borrowsID) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Method to see if an User can borrow another book
	 * @param userID the user's ID
	 * @return true if the user can borrow another book and false if not
	 */
	public static boolean canUserBorrow(int userID) {
		int count = 0;
		
		for(Borrow b : getAllBorrow()) {
			if(b.getUsersID() == userID) {
				count++;
			}
			if(count == User.getMaxBorrowNumber()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method to see if an User can borrow another book
	 * @param userID the user's ID
	 * @return true if the user can borrow another book and false if not
	 */
	public static boolean canBookBeBorrowed(String bookID) {
		int count = 0;
		
		for(Borrow b : getAllBorrow()) {
			if(b.getBooksIdentifier().equals(bookID)) {
				count++;
			}
			if(count == Book.getNumberBorrowPossible()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * toString method for Borrow
	 * @return the string to print a borrow
	 */
    @Override
    public String toString() {
        return "Borrow : " +
        		"\nID: " + getId() +
        		"\nUser's ID : "+ getUsersID() +
        		"\nBook's identifier : " + getBooksIdentifier() +
        		"\nBorrow date : " + getDateBorrow() +
        		"\nReturn date : " + getReturnDate() +
        		"\nDuration : " + getDuration() + 
        		"\nLate : " + isLate();
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
    	return this.getUsersID() == b.getUsersID() && this.getBooksIdentifier().equals(b.getBooksIdentifier()) && this.getDateBorrow().equals(b.getDateBorrow()) &&
    			this.getReturnDate().equals(b.getReturnDate()) && this.getEffectiveReturnDate().equals(b.getEffectiveReturnDate()) && this.isLate() == b.isLate();
    }
  
}
