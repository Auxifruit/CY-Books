package abstraction.db;

import abstraction.Borrow;
import abstraction.GeneralUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * The class that allows us to manage the Borrows in the database
 */
public class DataBaseBorrow {
	// Constants declared to save the tables names
	protected final static String BORROW_TABLE = "borrow";


	protected final static String ID = "id";
	protected final static String USER_ID = "user_id";
	protected final static String BOOK_ID = "book_id";
	protected final static String BORROW_START = "borrow_start";
	protected final static String BORROW_END = "borrow_end";
	protected final static String BORROW_REAL_END = "borrow_real_end";
	protected final static String BORROW_DURATION = "duration";
	protected final static String BORROW_LATE = "late";
		
	/**
	 * Method to read all the values of the table borrows
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readBorrowsTable() throws SQLException {
		try {
			Connection co = DataBaseManage.quickconnect();

			// Allow us to call the query
			Statement smt = co.createStatement();
			
			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT * FROM " + BORROW_TABLE);
			
			// While we have a row
			while(res.next()) {
				// We get the borrow's informations
				int borrowsID = res.getInt(ID);
				int usersID = res.getInt(USER_ID);
				String booksID = res.getString(BOOK_ID);
				LocalDate borrowDate = GeneralUtils.stringToLocalDate(res.getString(BORROW_START));
				LocalDate returnDate = GeneralUtils.stringToLocalDate(res.getString(BORROW_END));
				long borrowsDuration = res.getInt(BORROW_DURATION);
				boolean borrowsStatue = Boolean.parseBoolean(res.getString(BORROW_LATE));

				if(!(res.getString(BORROW_REAL_END).equals(""))) {
					LocalDate effectiveReturnDate = GeneralUtils.stringToLocalDate(res.getString(BORROW_REAL_END));
					
					new Borrow(borrowsID, usersID, booksID, borrowDate, returnDate, effectiveReturnDate, borrowsDuration, borrowsStatue);
				}
				else {
					new Borrow(borrowsID, usersID, booksID, borrowDate, returnDate, borrowsDuration, borrowsStatue);
				}
				
			}
			
			co.close();
			

			// We update the Borrow's class counterId to note have duplicate id if we have at least 2 element
			if(Borrow.getAllBorrow().size() > 1) {
				Borrow.setCounterId(Borrow.getAllBorrow().get(Borrow.getAllBorrow().size() - 1).getId() + 1);
			}

		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to add a Borrow in the database
	 * @param borrowToAdd the Borrow we want to add
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void addBorrowInTable(Borrow borrowToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "INSERT INTO " + BORROW_TABLE + " (" + ID + ", " + USER_ID + ", " + BOOK_ID + ", " 
	            + BORROW_START + ", " + BORROW_END + ", " + BORROW_REAL_END + ", " + BORROW_DURATION + ", " + BORROW_LATE + ") "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			int id = borrowToAdd.getId();
			int usersID = borrowToAdd.getUsersID();
			String booksID = borrowToAdd.getBooksIdentifier();
			String borrowDate = borrowToAdd.getDateBorrow();
			String returnDate = borrowToAdd.getReturnDate();
			String effectiveReturnDate = borrowToAdd.getEffectiveReturnDate();
			String duration = String.valueOf(borrowToAdd.getDuration());
			String late = String.valueOf(borrowToAdd.isLate());

			// We set the values of the query
			ps.setInt(1, id);
			ps.setInt(2, usersID);
			ps.setString(3, booksID);
			ps.setString(4, borrowDate);
			ps.setString(5, returnDate);
			ps.setString(6, effectiveReturnDate);
			ps.setString(7, duration);
			ps.setString(8, late);
			
			ps.executeUpdate();
			
			co.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to delete a Borrow from the database
	 * @param borrowToDelete the Borrow we want to delete
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteBorrowInTable(Borrow borrowToDelete) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM " + BORROW_TABLE + " WHERE " + ID + " = ?";

		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We only use the id because it is unique 
			int id = borrowToDelete.getId();

			// We set the values of the query
			ps.setInt(1, id);
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to delete a Borrow from the database by using the user's ID
	 * @param usersID the id of the user whose borrowings we want to delete
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteBorrowInTableByUsersID(int usersID) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM " + BORROW_TABLE + " WHERE " + USER_ID + " = ?";

		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setInt(1, usersID);
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}	

	/**
	 * Method to modify a Borrow in the database
	 * @param borrowToModifythe borrow we want to modify
	 * @param newBorrowDate the borrow's new date
	 * @param newReturnDate the borrow's new return date
	 * @param newEffectiveReturnDate the borrow's new effective return date
	 * @param newDuration the borro's new duration
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyBorrowInTable(Borrow borrowToModify, String newBorrowDate, String newReturnDate, String newEffectiveReturnDate, long newDuration) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + BORROW_TABLE + " SET " + BORROW_START + " = ?, " + BORROW_END + " = ?, " + BORROW_REAL_END + " = ?, " + BORROW_DURATION + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setString(1, newBorrowDate);
			ps.setString(2, newReturnDate);
			ps.setString(3, newEffectiveReturnDate);
			ps.setLong(4, newDuration);
			ps.setInt(5, borrowToModify.getId());
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to modify a Borrow in the database
	 * @param borrowToModifythe borrow we want to modify
	 * @param newBorrowDate the borrow's new date
	 * @param newReturnDate the borrow's new return date
	 * @param newDuration the borro's new duration
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyBorrowInTable(Borrow borrowToModify, String newBorrowDate, String newReturnDate, long newDuration) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + BORROW_TABLE + " SET " + BORROW_START + " = ?, " + BORROW_END + " = ?, " + BORROW_DURATION + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setString(1, newBorrowDate);
			ps.setString(2, newReturnDate);
			ps.setLong(3, newDuration);
			ps.setInt(4, borrowToModify.getId());
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to count the number of borrow for one user by it's ID
	 * @param usersID the user's ID
	 * @return the number of borrow for one user by it's ID
	 * @throws SQLException if we have an exception about SQL
	 */
	public static int countUsersBorrow(int usersID) throws SQLException {
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to call the query
			Statement smt = co.createStatement();

			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT COUNT(*) AS total FROM " + BORROW_TABLE + " WHERE " + USER_ID + " = " + usersID + " and " + BORROW_REAL_END + " = \"\";");
			
			// Get the number of borrow for one user
			int nombreEmprunt = res.getInt("total");
			
			co.close();			
			
			return nombreEmprunt;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to count the number of time a book is borrowed by it's identifier
	 * @param booksID the book's ID
	 * @return the number of time a book is borrowed by it's identifier
	 * @throws SQLException if we have an exception about SQL
	 */
	public static int countBookBorrowed(String booksID) throws SQLException {
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to call the query
			Statement smt = co.createStatement();

			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT COUNT(*) AS total FROM " + BORROW_TABLE + " WHERE " + BOOK_ID + " = '" + booksID + "' and " + BORROW_REAL_END + " = '' ;");
			
			// Get the number of book is borrowed
			int nombreEmprunt = res.getInt("total");
			
			co.close();			
			
			return nombreEmprunt;
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to return a TreeMap containing the the book's identifier and the number of times it was borrow for the last 30 days
	 * @return a TreeMap containing the book's identifier and the number of times it was borrow
	 * @throws SQLException if we have an exception about SQL
	 */
	public static Map<String, Integer> mostBorrowBookLastThirtyDays() throws SQLException {
		LocalDate todayMinusThirty = LocalDate.now().minusDays(30);
		
		try {
			Map<String, Integer> bookAndNumberBorrowed = new TreeMap<>();
			
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to call the query
			Statement smt = co.createStatement();

			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT " + BOOK_ID + ", COUNT(*) AS borrow_count FROM " + BORROW_TABLE
					+ " WHERE " + BORROW_START + " > '" + todayMinusThirty + "'"
					+" GROUP BY " + BOOK_ID);
			
			
			while (res.next()) {
                String bookId = res.getString(BOOK_ID);
                int borrowCount = res.getInt("borrow_count");
                bookAndNumberBorrowed.put(bookId, borrowCount);
            }
			
			co.close();
			
			return bookAndNumberBorrowed;
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
}
