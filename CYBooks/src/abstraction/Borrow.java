package abstraction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Borrow represents a borrow made by the user
 * @author CYTech Student
 */
public class Borrow {
	protected static List<Borrow> allBorrows = new ArrayList<>();
	protected String usersID;
	protected String booksISBN;
	protected String borrowDate;
	protected String returnDate;
	protected String effectiveReturnDate;
	protected boolean late;
	protected List<String> problems;
    
    /**
     * Borrow constructor with all fields as parameters
     * @param user the user who borrowed the book
     * @param book the borrowed book
     * @param dateBorrow the borrow's date
     */
    public Borrow(User user, Book book, LocalDate dateBorrow) {
        this.usersID = String.valueOf(user.getId());
        this.booksISBN = String.valueOf(book.getIsbn());
        this.borrowDate = dateBorrow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.returnDate = dateBorrow.plusDays(Book.MAX_BORROW_TIME).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));;
        this.effectiveReturnDate = "";
        this.late = false;
        this.problems = new ArrayList<String>();
        allBorrows.add(this);
    }

    /**
     * Get the id of the user who borrowed the book
     * @return the id of the user who borrowed the book
     */
    public String getUsersID() {
        return usersID;
    }

    /**
     * user setter method
     * @param user the user who borrowed the book
     */
    public void setUsersID(User user) {
        this.usersID = String.valueOf(user.getId());
    }

    /**
     * Get the ISBN of the borrowed book
     * @return the ISBN of the borrowed book
     */
    public String getBooksISBN() {
        return booksISBN;
    }

    /**
     * Book's ISBN setter method
     * @param book new the borrowed book
     */
    public void setBook(Book book) {
        this.booksISBN = String.valueOf(book.getIsbn());
    }

    /**
     * Get the borrow's date
     * @return the borrow's date
     */
    public String getDateBorrow() {
        return borrowDate;
    }

    /**
     * borrowDate setter method
     * @param borrowDate the date of the creation of the borrow
     */
    public void setDateBorrow(LocalDate borrowDate) {
        this.borrowDate = borrowDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * Get the borrow's return date
     * @return returnDate the date of which the book need to be return
     */
    public String getReturnDate() {
        return returnDate;
    }

    /**
     * returnDate setter method
     * @param return the borrow's return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    
    
    /**
     * Get the borrow's effective return date
     * @return effectiveReturnDate
     */
    public String getEffectiveReturnDate() {
		return effectiveReturnDate;
	}

    /**
     * effectiveReturnDate setter method
     */
	public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
		this.effectiveReturnDate = effectiveReturnDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	/**
	 * Get the borrow's state : late or not
	 * @return true if the borrow is late and false if not
	 */
	public boolean isLate() {
		return late;
	}
    
    /**
     * late setter method
     * @param late true or false if the borrow is late or not
     */
    public void setLate(boolean late) {
		this.late = late;
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
    	return this.getUsersID().equals(b.getUsersID()) && this.getBooksISBN().equals(b.getBooksISBN()) && this.getDateBorrow().equals(b.getDateBorrow()) &&
    			this.getReturnDate().equals(b.getReturnDate()) && this.getProblems().equals(b.getProblems());
    }
  
}
