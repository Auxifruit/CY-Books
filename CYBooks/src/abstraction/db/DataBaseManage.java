package abstraction.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import abstraction.Borrow;
import abstraction.GeneralUtils;
import abstraction.Problem;
import abstraction.User;
import abstraction.Status;

/**
 * The class that allows us to manage the database
 */
public class DataBaseManage {
	// Constants declared to save the tables names
	public final static String USER_TABLE = "users";
	public final static String BORROW_TABLE = "borrow";
	public final static String PROBLEM_TABLE = "problem";

	// Constant used to avoid spelling mistakes when retrieving the results of
	// queries
	public final static String ID = "id";

	// USED BY USERS
	public final static String LAST_NAME = "lastname";
	public final static String FIRST_NAME = "firstname";
	public final static String EMAIL = "email";
	public final static String STATUS = "status";

	// USED BY BORROW
	public final static String USER_ID = "user_id";
	public final static String BOOK_ID = "book_id";
	public final static String BORROW_START = "borrow_start";
	public final static String BORROW_END = "borrow_end";
	public final static String BORROW_REAL_END = "borrow_real_end";
	public final static String BORROW_DURATION = "duration";
	public final static String BORROW_LATE = "late";
	
	// USED BY PROBLEM_TEXT
	public final static String BORROW_ID = "borrow_id";
	public final static String PROBLEM_TEXT = "problem_text";

	// Constants declared to form the tables structure
	public final static String USER_TABLE_STRUCTURE = "CREATE TABLE " + USER_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + LAST_NAME + " TEXT NOT NULL,\r\n" + FIRST_NAME
			+ " TEXT NOT NULL,\r\n" + EMAIL + " TEXT UNIQUE NOT NULL,\r\n" + STATUS + " TEXT NOT NULL);";

	public final static String BORROW_TABLE_STRUCTURE = "CREATE TABLE " + BORROW_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + USER_ID + " INTEGER NOT NULL,\r\n" + BOOK_ID
			+ " TEXT NOT NULL,\r\n" + BORROW_START + " TEXT NOT NULL,\r\n" + BORROW_END + " TEXT,\r\n"
			+ BORROW_REAL_END + " TEXT,\r\n" + BORROW_DURATION + " INTEGER,\r\n" + BORROW_LATE + " TEXT, \r\n"
			+ "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "));";
	
	public final static String PROBLEM_TABLE_STRUCTURE = "CREATE TABLE " + PROBLEM_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + BORROW_ID + " INTEGER NOT NULL,\r\n" + PROBLEM_TEXT
			+ " TEXT NOT NULL,\r\n"
			+ "FOREIGN KEY (" + BORROW_ID + ") REFERENCES " + BORROW_TABLE + "(" + ID + "));";

	// Regroup all of these constants into variables
	private final static String[] tables = { USER_TABLE, BORROW_TABLE, PROBLEM_TABLE };
	private final static String[] creationQuery = { USER_TABLE_STRUCTURE, BORROW_TABLE_STRUCTURE, PROBLEM_TABLE_STRUCTURE };

	/**
	 * Try to connect to the database (WILL AUTOMATICALLY CREATE THE DATABASE IF IT
	 * DOESN'T EXIST)
	 * 
	 * @return A statement on which you can execute queries
	 */
	public static Connection quickconnect() throws SQLException {
		try {
			return DriverManager.getConnection("jdbc:sqlite:cybase.db");
		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}

	}

	/**
	 * Create all the used tables in the project
	 */
	public static void createTables() throws SQLException {
		try {
			Connection co = quickconnect();
			Statement smt = co.createStatement();
			DatabaseMetaData dbData = co.getMetaData();

			for (int i = 0; i < tables.length; i++) {
				ResultSet res = dbData.getTables(null, null, tables[i], null); // Check if there's any table named after
																				// tables[i]

				// If there's not any table we create it
				if (!res.next()) {
					smt.executeUpdate(creationQuery[i]); // Use executeUpdate for DDL statements

				} else {
					System.out.println("The table " + tables[i] + " already exists !");
				}
			}

			smt.close();
			co.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Problem while creating the tables");
		}
	}
	
	/**
	 * Method to read all the values of the table users
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readUsersTable() throws SQLException {
		try {
			Connection co = quickconnect();

			// Allow us to call the query
			Statement smt = co.createStatement();
			
			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT * FROM " + USER_TABLE);
			
			// While we have a row
			while(res.next()) {
				// We get the users's informations
				int usersID = res.getInt(ID);
				String firstname = res.getString(FIRST_NAME);
				String lastname = res.getString(LAST_NAME);
				String email = res.getString(EMAIL);
				Status status = Status.valueOf(res.getString(STATUS).toUpperCase());
				
				new User(usersID, lastname, firstname, email, status);
			}
			
			co.close();
			
			// We update the User's class counterId to note have duplicate id if we have at least 1 element
			if(User.getAllUser().size() > 0) {
				User.setCounterId(User.getAllUser().get(User.getAllUser().size() - 1).getId() + 1);
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to add an User in the database
	 * @param userToAdd the user we want to add
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void addUserInTable(User userToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
				final String query = "INSERT INTO " + USER_TABLE + " (" + ID + ", " + LAST_NAME + ", " + FIRST_NAME + ", " 
			            + EMAIL + ", " +  STATUS + ") " + "VALUES (?, ?, ?, ?, ?)";
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			int usersID = userToAdd.getId();
			String firstname = userToAdd.getFirstname();
			String lastname = userToAdd.getLastname();
			String email = userToAdd.getEmail();
			String status = userToAdd.getStatus();

			// We set the values of the query
			ps.setInt(1, usersID);
			ps.setString(2, firstname);
			ps.setString(3, lastname);
			ps.setString(4, email);
			ps.setString(5, status);
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to delete an User from the database
	 * @param userToDelete the user we want to delete
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteUserInTable(User userToDelete) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM " + USER_TABLE + " WHERE " + ID + " = ?";
		
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);
			
			// We only use the id because it is unique 
			int usersID = userToDelete.getId();

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
	 * Method to modify an User in the database
	 * @param userToModify the user we want to modify
	 * @param oldLastname the user's old last name
	 * @param oldFirstname the user's old first name
	 * @param oldEmail the user's old e-mail
	 * @param newLastname the user's new last name
	 * @param newFirstname the user's new first name
	 * @param newEmail the user's new email
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyUserInTable(User userToModify, String newLastname, String newFirstname, String newEmail, String newStatus) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + USER_TABLE + " SET " + LAST_NAME + " = ?, " + FIRST_NAME + " = ?, " + EMAIL + " = ?, " + STATUS + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setString(1, newLastname);
			ps.setString(2, newFirstname);
			ps.setString(3, newEmail);
			ps.setString(4, newStatus);
			ps.setInt(5, userToModify.getId());
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to read all the values of the table borrows
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readBorrowsTable() throws SQLException {
		try {
			Connection co = quickconnect();

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
	 * Method to add a Borrow to the database
	 * @param borrowToAdd the Borrow we want to add
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void addBorrowInTable(Borrow borrowToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "INSERT INTO " + BORROW_TABLE + " (" + ID + ", " + USER_ID + ", " + BOOK_ID + ", " 
	            + BORROW_START + ", " + BORROW_END + ", " + BORROW_REAL_END + ", " + BORROW_DURATION + ", " + BORROW_LATE + ") "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			Connection co = quickconnect();
			
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
			Connection co = quickconnect();
			
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
			Connection co = quickconnect();
			
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
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyBorrowInTable(Borrow borrowToModify, String newBorrowDate, String newReturnDate, String newEffectiveReturnDate, long newDuration) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + BORROW_TABLE + " SET " + BORROW_START + " = ?, " + BORROW_END + " = ?, " + BORROW_REAL_END + " = ?, " + BORROW_DURATION + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = quickconnect();
			
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
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyBorrowInTable(Borrow borrowToModify, String newBorrowDate, String newReturnDate, long newDuration) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + BORROW_TABLE + " SET " + BORROW_START + " = ?, " + BORROW_END + " = ?, " + BORROW_DURATION + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = quickconnect();
			
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
			Connection co = quickconnect();
			
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
			Connection co = quickconnect();
			
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
			
			Connection co = quickconnect();
			
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
	
	/**
	 * Method to read all the values of the table problem
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readProblemsTable() throws SQLException {
		try {
			Connection co = quickconnect();

			// Allow us to call the query
			Statement smt = co.createStatement();
			
			// Allow us to store the result of the query 
			ResultSet res = smt.executeQuery("SELECT * FROM " + PROBLEM_TABLE);
			
			// While we have a row
			while(res.next()) {
				// We get the users's informations
				int borrowsId = res.getInt(BORROW_ID);
				String text = res.getString(PROBLEM_TEXT);
				
				new Problem(borrowsId, text);
			}
			
			co.close();
			
			// We update the Problem's class counterId to note have duplicate id if we have at least 1 element
			if(Problem.getAllProblems().size() > 0) {
				Problem.setCounterId(Problem.getAllProblems().get(Problem.getAllProblems().size() - 1).getID() + 1);
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}

	/**
	 * Method to add a problem in the database
	 * @param problemToAdd the problem we want to add
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void addProblemInTable(Problem problemToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
				final String query = "INSERT INTO " + PROBLEM_TABLE + " (" + BORROW_ID + ", " + PROBLEM_TEXT + ") " + "VALUES (?, ?)";
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			int borrowsID = problemToAdd.getBorrowsID();
			String text = problemToAdd.getText();

			// We set the values of the query
			ps.setInt(1, borrowsID);
			ps.setString(2, text);
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}

	/**
	 * Method to delete a Problem from the database
	 * @param problemToDelete the Problem we want to delete
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteProblemInTable(Problem problemToDelete) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM " + PROBLEM_TABLE + " WHERE " + BORROW_ID + " = ? and " + PROBLEM_TEXT + " = ? ";

		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setInt(1, problemToDelete.getBorrowsID());
			ps.setString(2, problemToDelete.getText());
			
			
			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}	
	
	public static void main(String[] args) throws SQLException {
		//System.out.println(BORROW_TABLE_STRUCTURE);
		//createTables();
		
		
		System.out.println(LocalDate.now().minusDays(30));
		
		
		/*
		 * try { Connection co = DriverManager.getConnection("jdbc:sqlite:cybase.db");
		 * Statement smt = co.createStatement(); System.out.println(co);
		 * 
		 * DatabaseMetaData metaData = co.getMetaData(); ResultSet tables =
		 * metaData.getTables(null, null, "utilisateur", null); if (tables.next()) { //
		 * Table exists System.out.println("Table " + "utilisateur" + " exists."); }
		 * else { // Table does not exist System.out.println("Table " + "utilisateur" +
		 * " does not exist."); }
		 * 
		 * smt.close();
		 * 
		 * co.close(); }
		 * 
		 * catch (SQLException e) { e.printStackTrace(); }
		 */
	}

}
