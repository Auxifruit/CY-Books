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
	protected static int counterId = 1;
	protected static List<Borrow> allBorrows = new ArrayList<>();
	protected IntegerProperty id;
	protected IntegerProperty usersID;
	protected StringProperty booksISBN;
	protected StringProperty borrowDate;
	protected StringProperty returnDate;
	protected StringProperty effectiveReturnDate;
	protected SimpleBooleanProperty late;
	protected List<String> problems;
    
    /**
     * Borrow constructor with all fields as parameters
     * @param userID the ID of the user who borrowed the book
     * @param bookISBN the ISBN of the borrowed book
     * @param dateBorrow the borrow's date
     */
    public Borrow(int usersID, String booksISBN, LocalDate dateBorrow) {
        this.usersID = new SimpleIntegerProperty(usersID);
        this.booksISBN =new SimpleStringProperty(booksISBN);
        this.borrowDate = new SimpleStringProperty(dateBorrow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.returnDate = new SimpleStringProperty(dateBorrow.plusDays(Book.MAX_BORROW_TIME).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.effectiveReturnDate = new SimpleStringProperty("");
        this.late = new SimpleBooleanProperty(false);
        this.problems = new ArrayList<String>();
        this.id = new SimpleIntegerProperty(counterId++);
        allBorrows.add(this);
    }
    
    /**
     * Borrow constructor with all fields as parameters except the effectiveReturnDate
     * @param userID the ID of the user who borrowed the book
     * @param bookISBN the ISBN of the borrowed book
     * @param dateBorrow the borrow's date
     * @param returnDate the borrow's return date
     * @param late the statue of the borrow
     */
    public Borrow(int id, int usersID, String booksISBN, LocalDate dateBorrow, LocalDate returnDate, boolean late) {
        this(usersID, booksISBN, dateBorrow);
        this.returnDate = new SimpleStringProperty(returnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.late = new SimpleBooleanProperty(late);
        this.id = new SimpleIntegerProperty(id);
    }
    
    /**
     * Borrow constructor with all fields as parameters
     * @param userID the ID of the user who borrowed the book
     * @param bookISBN the ISBN of the borrowed book
     * @param dateBorrow the borrow's date
     * @param returnDate the borrow's return date
     * @param effectiveReturnDate the borrow's effective return date
     * @param late the statue of the borrow
     */
    public Borrow(int id, int usersID, String booksISBN, LocalDate dateBorrow, LocalDate returnDate, LocalDate effectiveReturnDate, boolean late) {
        this(usersID, booksISBN, dateBorrow);
        this.returnDate = new SimpleStringProperty(returnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.effectiveReturnDate = new SimpleStringProperty(effectiveReturnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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
        this.id.set(id);;
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
    	// We get the date in a string
    	String borrowDateString = this.getDateBorrow();
    	
		// We split the date's String to get the day, month and year
		String[] date = borrowDateString.split("-");
		
		// We use these element for our LocalDate
		LocalDate borrowDateLocalDate = LocalDate.of(Integer.valueOf(date[2]), Integer.valueOf(date[1]), Integer.valueOf(date[0]));
		
		return borrowDateLocalDate;
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
    	// We get the return date in a string
    	String returnDateString = this.getReturnDate();
    	
		// We split the return date's String to get the day, month and year
		String[] date = returnDateString.split("-");
		
		// We use these element for our LocalDate
		LocalDate returnDateLocalDate = LocalDate.of(Integer.valueOf(date[2]), Integer.valueOf(date[1]), Integer.valueOf(date[0]));
		
		return returnDateLocalDate;
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
    		// We split the return date's String to get the day, month and year
    		String[] date = effectiveReturnDateString.split("-");
    		
    		// We use these element for our LocalDate
    		LocalDate effectiveReturnDateLocalDate = LocalDate.of(Integer.valueOf(date[2]), Integer.valueOf(date[1]), Integer.valueOf(date[0]));
    		
    		return effectiveReturnDateLocalDate;
    	}
		
		return null;
    }

    /**
     * effectiveReturnDate setter method
     */
	public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
		if(effectiveReturnDate != null) {
			this.effectiveReturnDate.set(effectiveReturnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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
	 * Method to check is a borrow is late, if true, changed the late state to true
	 */
	public void checkBorrowLate() {
		LocalDate today = LocalDate.now();
		
		if((this.getEffectiveReturnDateLocalDate() == null && today.isAfter(this.getReturnDateLocalDate())
				|| (this.getEffectiveReturnDateLocalDate() != null && this.getEffectiveReturnDateLocalDate().isAfter(this.getReturnDateLocalDate())))) {
			this.setLate(true);
		}
		else {
			this.setLate(false);
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
	 * toString method for Borrow
	 * @return the string to print a borrow
	 */
    @Override
    public String toString() {
        String text =  "Borrow : " +
        		"\nID: " + getId() +
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
