package abstraction.exception;

public class BookCantBeBorrowedException extends Exception {

	public BookCantBeBorrowedException(String msg) {
		super(msg); 
	}
}
