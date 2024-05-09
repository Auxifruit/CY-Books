package abstraction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Borrow represents a borrow made by the user
 * @author CYTech Student
 */
public class Borrow {
	protected static List<Borrow> allBorrows = new ArrayList<>();
	protected User user;
	protected Book book;
	protected LocalDate borrowDate;
	protected LocalDate returnDate;
	protected boolean late;
	protected List<String> problems;
    
    /**
     * Borrow constructor with all fields as parameters
     * @param user the user who borrowed the book
     * @param book the borrowed book
     * @param dateBorrow the borrow's date
     */
    public Borrow(User user, Book book, LocalDate dateBorrow) {
        this.user = user;
        this.book = book;
        this.borrowDate = dateBorrow;
        this.returnDate = borrowDate.plusDays(Book.MAX_BORROW_TIME);
        this.late = false;
        this.problems = new ArrayList<String>();
        allBorrows.add(this);
    }

    /**
     * Get the user who borrowed the book
     * @return the user who borrowed the book
     */
    public User getUser() {
        return user;
    }

    /**
     * user setter method
     * @param user the user who borrowed the book
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the borrowed book
     * @return the borrowed book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Book setter method
     * @param book the borrowed book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Get the borrow's date
     * @return the borrow's date
     */
    public LocalDate getDateBorrow() {
        return borrowDate;
    }

    /**
     * borrowDate setter method
     * @param borrowDate
     */
    public void setDateBorrow(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * Get the borrow's return date
     * @return returnDate
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * returnDate setter method
     * @param return the borrow's return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
	 * Method to check is a borrow is late, if true, changed the late state to true
	 * @return true if the borrow is late and false if not
	 */
	public boolean isBorrowLate() {
		LocalDate today = LocalDate.now();
		
		if(today.isAfter(getReturnDate())) {
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
        		"\n"+ getUser() +
        		"\n\nBook :\n" + getBook() +
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
    	return this.getUser().equals(b.getUser()) && this.getBook().equals(b.getBook()) && this.getDateBorrow().equals(b.getDateBorrow()) &&
    			this.getReturnDate().equals(b.getReturnDate()) && this.getProblems().equals(b.getProblems());
    }
  
}
