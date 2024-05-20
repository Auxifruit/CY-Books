package abstraction;

import java.time.LocalDate;

/**
 * The class Book represents a book in the library
 * @author CYTech Student
 */
public class Book {
	protected static long MAX_BORROW_TIME = 30;
	protected String isbn;
	protected String title;
	protected String author;
	protected LocalDate publishedDate;
	protected String theme;
	protected String type;
	protected String editor;
	
	/**
	 * book constructor with all fields as parameters
	 * @param isbn the book's isbn
	 * @param title the book's title
	 * @param author the book's author
	 * @param publishedDate the book's published date
	 * @param theme the book's theme
	 * @param type the book's type
	 * @param editor the book's editor
	 */
	public Book(String isbn, String title, String author, String publishedDate, String theme, String type, String editor) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publishedDate = LocalDate.parse(publishedDate);
		this.theme = theme;
		this.type = type;
		this.editor = editor;
	}

	/**
     * get the book's isbn
     * @return the book's isbn
     */
	public String getIsbn() {
		return isbn;
	}

	/**
     * isbn setter method
     * @param isbn the book's isbn
     */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
     * get the book's title
     * @return the book's title
     */
	public String getTitle() {
		return title;
	}

	/**
     * title setter method
     * @param title the book's title
     */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
     * get the book's author
     * @return the book's author
     */
	public String getAuthor() {
		return author;
	}

	/**
     * author setter method
     * @param author the book's author
     */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
     * get the book's published date
     * @return the book's published date
     */
	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	/**
     * published date setter method
     * @param publishedDate the book's published date
     */
	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}

	/**
     * get the book's theme
     * @return the book's theme
     */
	public String getTheme() {
		return theme;
	}

	/**
     * theme setter method
     * @param theme the book's theme
     */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
     * get the book's type
     * @return the book's type
     */
	public String getType() {
		return type;
	}

	/**
     * type setter method
     * @param type the book's type
     */
	public void setType(String type) {
		this.type = type;
	}

	/**
     * get the book's editor
     * @return the book's editor
     */
	public String getEditor() {
		return editor;
	}

	/**
     * editor setter method
     * @param editor the book's editor
     */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * toString method for Book
	 * @return the string to print a book
	 */
	@Override
	public String toString() {
		return "Book : " +
				"\nISBN : " + getIsbn() +
				"\nTitle : " + getTitle() +
				"\nAuthor : " + getAuthor() +
				"\nPublished date : " + getPublishedDate() +
				"\nTheme : " + getTheme() +
				"\nType : " + getType() + 
				"\nEditor : " + getEditor();
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
    	return this.getIsbn() == b.getIsbn() && this.getTitle().equals(b.getTitle()) && this.getAuthor().equals(b.getAuthor())
    			&& this.publishedDate.equals(b.getPublishedDate()) && this.getTheme().equals(b.getTheme()) && this.getType().equals(b.getType())
    			&& this.getEditor().equals(b.getEditor());
    }
	
}
