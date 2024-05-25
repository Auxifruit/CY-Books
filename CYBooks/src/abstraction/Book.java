package abstraction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The class Book represents a book in the library
 * @author CYTech Student
 */
public class Book {
	private static long MAX_BORROW_TIME = 30;
	private static long NUMBER_BORROW_POSSIBLE = 1;
	private StringProperty identifier;
	private StringProperty title;
	private StringProperty author;
	private StringProperty publishedDate;
	private StringProperty format;
	private StringProperty type;
	private StringProperty publisher;
	private IntegerProperty nbBorrowed;
	
	/**
	 * book constructor with all fields as parameters
	 * @param identifier the book's identifier
	 * @param title the book's title
	 * @param author the book's author
	 * @param publishedDate the book's published date
	 * @param format the book's format
	 * @param type the book's type
	 * @param publisher the book's publisher
	 */
	public Book(String identifier, String title, String author, String publishedDate, String format, String type, String publisher) {
		this.identifier = new SimpleStringProperty(identifier);
		this.title = new SimpleStringProperty(title);
		this.author = new SimpleStringProperty(author);
		this.publishedDate = new SimpleStringProperty(publishedDate);
		this.format = new SimpleStringProperty(format);
		this.type = new SimpleStringProperty(type);
		this.publisher = new SimpleStringProperty(publisher);
		this.nbBorrowed = new SimpleIntegerProperty(0);
	}

	/**
     * Getter method for the book's identifier
     * @return the book's identifier
     */
	public String getIdentifier() {
		return identifier.get();
	}

	/**
     * Setter method for the book's identifier
     * @param identifier the book's identifier
     */
	public void setIdentifier(String identifier) {
		this.identifier.set(identifier);
	}
	
	/**
     * Method to return the StringProperty of the book's identifier for the TableView
     * @return the StringProperty of the book's identifier 
     */
    public StringProperty identifierProperty() {
        return identifier;
    }

	/**
     * Getter method for the book's title
     * @return the book's title
     */
	public String getTitle() {
		return title.get();
	}

	/**
     * Setter method for book's title
     * @param title the book's title
     */
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	/**
     * Method to return the StringProperty of the book's title for the TableView
     * @return the StringProperty of the book's title 
     */
    public StringProperty titleProperty() {
        return title;
    }

	/**
     * Getter method for the book's author
     * @return the book's author
     */
	public String getAuthor() {
		return author.get();
	}

	/**
     * Setter method for the book's author
     * @param author the book's author
     */
	public void setAuthor(String author) {
		this.author.set(author);
	}
	
	/**
     * Method to return the StringProperty of the book's author for the TableView
     * @return the StringProperty of the book's author 
     */
    public StringProperty authorProperty() {
        return author;
    }

	/**
     * Getter method for the book's published date
     * @return the book's published date
     */
	public String getPublishedDate() {
		return publishedDate.get();
	}

	/**
     * Setter method for the book's published date
     * @param publishedDate the book's published date
     */
	public void setPublishedDate(String publishedDate) {
		this.publishedDate.set(publishedDate);
	}
	
	/**
     * Method to return the StringProperty of the book's published date for the TableView
     * @return the StringProperty of the book's published date 
     */
    public StringProperty publishedDateProperty() {
        return publishedDate;
    }

	/**
     * Getter method for the book's format
     * @return the book's format
     */
	public String getFormat() {
		return format.get();
	}

	/**
     * Setter method for the book's format
     * @param format the book's format
     */
	public void setFormat(String format) {
		this.format.set(format);
	}
	
	/**
     * Method to return the StringProperty of the book's format for the TableView
     * @return the StringProperty of the book's format 
     */
    public StringProperty formatProperty() {
        return format;
    }

	/**
     * Getter method for the book's type
     * @return the book's type
     */
	public String getType() {
		return type.get();
	}

	/**
     * Getter method for the book's type
     * @param type the book's type
     */
	public void setType(String type) {
		this.type.set(type);
	}
	
	/**
     * Method to return the StringProperty of the book's type for the TableView
     * @return the StringProperty of the book's type 
     */
    public StringProperty typeProperty() {
        return type;
    }

	/**
     * Getter method for the book's publisher
     * @return the book's publisher
     */
	public String getPublisher() {
		return publisher.get();
	}

	/**
     * Getter method for for the book's publisher
     * @param publisher the book's publisher
     */
	public void setPublisher(String publisher) {
		this.publisher.set(publisher);
	}
	
	/**
     * Method to return the StringProperty of the book's publisher for the TableView
     * @return the StringProperty of the book's publisher 
     */
    public StringProperty publisherProperty() {
        return publisher;
    }
    
    /**
     * Getter method for the number of time a book has been borrowed
     * @return the number of time a book has been borrowed
     */
    public int getNbBorrowed() {
    	return nbBorrowed.get();
    }
    
    /**
     * G=Setter method for the number of time a book has been borrowed
     */
    public void setNbBorrowed(int nbBorrowed) {
    	this.nbBorrowed.set(nbBorrowed);
    }
    
    /**
     * Method to return the IntegerProperty of the book's number of time it has been borrowed
     * @return the StringProperty of the book's number of time it has been borrowed
     */
    public IntegerProperty nbBorrowed() {
        return nbBorrowed;
    }
	
	/**
	 * Get how long a book can be borrow
	 * @return how long a book can be borrow
	 */
	public static long getMaxTimeBorrow() {
		return MAX_BORROW_TIME;
	}
	
	/**
	 * Get the maximum number of time a book can be borrow
	 * @return the maximum number of time a book can be borrow
	 */
	public static long getNumberBorrowPossible() {
		return NUMBER_BORROW_POSSIBLE;
	}
	
	/**
	 * toString method for Book
	 * @return the string to print a book
	 */
	@Override
	public String toString() {
		return "Book : " +
				"\nIdentifier : " + getIdentifier() +
				"\nTitle : " + getTitle() +
				"\nAuthor : " + getAuthor() +
				"\nPublished date : " + getPublishedDate() +
				"\nFormat : " + getFormat() +
				"\nType : " + getType() + 
				"\nPublisher : " + getPublisher();
	}
	
	/**
     * equals method to check if a object is equal to an specefic book
     * @param obj the object to check if equals
     * @return true if obj is equals to a specefic book and false if not
     */
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof Book)) {
    		return false;
    	}
    	Book b = (Book) obj;
    	return this.getIdentifier() == b.getIdentifier() && this.getTitle().equals(b.getTitle()) && this.getAuthor().equals(b.getAuthor())
    			&& this.getPublishedDate().equals(b.getPublishedDate()) && this.getFormat().equals(b.getFormat()) && this.getType().equals(b.getType())
    			&& this.getPublisher().equals(b.getPublisher()) && this.getNbBorrowed() == b.getNbBorrowed();
    }
	
}
