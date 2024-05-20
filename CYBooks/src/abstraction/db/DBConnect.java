package abstraction.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import abstraction.Borrow;
import abstraction.User;

public class DBConnect {
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

	// USED BY BORROW
	public final static String USER_ID = "user_id";
	public final static String BOOK_ID = "book_id";
	public final static String BORROW_START = "borrow_start";
	public final static String BORROW_END = "borrow_end";
	public final static String BORROW_REAL_END = "borrow_real_end";
	public final static String BORROW_LATE = "late";
	
	// USED BY PROBLEM
	public final static String BORROW_ID = "borrow_id";
	public final static String PROBLEM = "problem_text";

	// Constants declared to form the tables structure
	public final static String USER_TABLE_STRUCTURE = "CREATE TABLE " + USER_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\r\n" + LAST_NAME + " TEXT NOT NULL,\r\n" + FIRST_NAME
			+ " TEXT NOT NULL,\r\n" + EMAIL + " TEXT UNIQUE NOT NULL);";

	public final static String BORROW_TABLE_STRUCTURE = "CREATE TABLE " + BORROW_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\r\n" + USER_ID + " INTEGER NOT NULL,\r\n" + BOOK_ID
			+ " TEXT NOT NULL,\r\n" + BORROW_START + " TEXT NOT NULL,\r\n" + BORROW_END + " TEXT,\r\n"
			+ BORROW_REAL_END + " TEXT,\r\n" + BORROW_LATE + " TEXT, \r\n"
			+ "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "));";
	
	public final static String PROBLEM_TABLE_STRUCTURE = "CREATE TABLE " + PROBLEM_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\r\n" + BORROW_ID + " INTEGER NOT NULL,\r\n" + PROBLEM
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
				
				new User(usersID, lastname, firstname, email);
			}
			
			co.close();
			
			// We update the User's class counterId to note have duplicate id if we have at least 2 element
			if(User.getAllUser().size() > 1) {
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
			            + EMAIL + ") " + "VALUES (?, ?, ?, ?)";
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			int usersID = userToAdd.getId();
			String firstname = userToAdd.getFirstname();
			String lastname = userToAdd.getLastname();
			String email = userToAdd.getEmail();

			// We set the values of the query
			ps.setInt(1, usersID);
			ps.setString(2, firstname);
			ps.setString(3, lastname);
			ps.setString(4, email);
			
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
	 * @param userToModifysID the id of the user we want to modify
	 * @param oldLastname the user's old last name
	 * @param oldFirstname the user's old first name
	 * @param oldEmail the user's old e-mail
	 * @param newLastname the user's new last name
	 * @param newFirstname the user's new first name
	 * @param newEmail the user's new email
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyUserInTable(int userToModifysID, String oldLastname, String oldFirstname, String oldEmail, String newLastname, String newFirstname, String newEmail) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + USER_TABLE + " SET " + LAST_NAME + " = ?, " + FIRST_NAME + " = ?, " + EMAIL + " = ?  "
				+ "WHERE " + ID + " = ? and " + LAST_NAME + " = ? and " + FIRST_NAME + " = ? and " + EMAIL + " = ?";
		
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setString(1, newLastname);
			ps.setString(2, newFirstname);
			ps.setString(3, newEmail);
			
			ps.setInt(4, userToModifysID);
			ps.setString(5, oldLastname);
			ps.setString(6, oldFirstname);
			ps.setString(7, oldEmail);
			
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
				boolean borrowsStatue = Boolean.parseBoolean(res.getString(BORROW_LATE));
				
				String borrowDateString = res.getString(BORROW_START);
				// We split the date's String to get the day, month and year
				String[] date1 = borrowDateString.split("-");
				// We use these element for our LocalDate
				LocalDate borrowDate = LocalDate.of(Integer.valueOf(date1[2]), Integer.valueOf(date1[1]), Integer.valueOf(date1[0]));
				
				String returnDateString = res.getString(BORROW_END);
				// We split the date's String to get the day, month and year
				String[] date2 = returnDateString.split("-");
				// We use these element for our LocalDate
				LocalDate returnDate = LocalDate.of(Integer.valueOf(date2[2]), Integer.valueOf(date2[1]), Integer.valueOf(date2[0]));
				
				String effectiveReturnDateString = res.getString(BORROW_REAL_END);
				// We split the date's String to get the day, month and year
				String[] date3 = effectiveReturnDateString.split("-");

				if(date3.length > 1) {
					// We use these element for our LocalDate
					LocalDate effectiveReturnDate = LocalDate.of(Integer.valueOf(date3[2]), Integer.valueOf(date3[1]), Integer.valueOf(date3[0]));
					
					new Borrow(borrowsID, usersID, booksID, borrowDate, returnDate, effectiveReturnDate, borrowsStatue);
				}
				else {
					new Borrow(borrowsID, usersID, booksID, borrowDate, returnDate, borrowsStatue);
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
	            + BORROW_START + ", " + BORROW_END + ", " + BORROW_REAL_END + ", " + BORROW_LATE + ") "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			int id = borrowToAdd.getId();
			int usersID = borrowToAdd.getUsersID();
			String booksID = borrowToAdd.getBooksISBN();
			String borrowDate = borrowToAdd.getDateBorrow();
			String returnDate = borrowToAdd.getReturnDate();
			String effectiveReturnDate = borrowToAdd.getEffectiveReturnDate();
			String late = String.valueOf(borrowToAdd.isLate());

			// We set the values of the query
			ps.setInt(1, id);
			ps.setInt(2, usersID);
			ps.setString(3, booksID);
			ps.setString(4, borrowDate);
			ps.setString(5, returnDate);
			ps.setString(6, effectiveReturnDate);
			ps.setString(7, late);
		
			
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
	 * Method to modify a Borrow in the database
	 * @param borrowToModifysID the id of the borrow we want to modify
	 * @param oldBorrowDate the borrow's old date
	 * @param oldReturnDate the borrow's old return date
	 * @param oldEffectiveReturnDate the borrow's old effective return date
	 * @param newBorrowDate the borrow's new date
	 * @param newReturnDate the borrow's new return date
	 * @param newEffectiveReturnDate the borrow's new effective return date
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyBorrowInTable(int borrowToModifysID, String oldBorrowDate, String oldReturnDate, String oldEffectiveReturnDate, String newBorrowDate, String newReturnDate, String newEffectiveReturnDate) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + BORROW_TABLE + " SET " + BORROW_START + " = ?, " + BORROW_END + " = ?, " + BORROW_REAL_END + " = ?  "
				+ "WHERE " + ID + " = ? and " + BORROW_START + " = ? and " + BORROW_END + " = ? and " + BORROW_REAL_END + " = ?";
		
		try {
			Connection co = quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setString(1, newBorrowDate);
			ps.setString(2, newReturnDate);
			ps.setString(3, newEffectiveReturnDate);
			
			ps.setInt(4, borrowToModifysID);
			ps.setString(5, oldBorrowDate);
			ps.setString(6, oldReturnDate);
			ps.setString(7, oldEffectiveReturnDate);
			
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
		createTables();
		
		//readUsersTable();
		
		/*
		User user2 = new User(0, "TEST", "TEST", "TEST@gmail.com");
		Borrow b2 = new Borrow(user2.getId(), "2", LocalDate.now());
		b2.setLate(true);
		Borrow b1 = new Borrow(user2.getId(), "3", LocalDate.now());
		Borrow b3 = new Borrow(user2.getId(), "4", LocalDate.now());
		b3.setEffectiveReturnDate(LocalDate.now().plusDays(35));
		Borrow b = new Borrow(user2.getId(), "5", LocalDate.now());
		b.setEffectiveReturnDate(LocalDate.now().plusDays(35));
		b.setLate(true);
		
		for(Borrow bo : Borrow.getAllBorrow()) {
			try {
				DBConnect.addBorrowInTable(bo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
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
