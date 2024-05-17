package abstraction.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import abstraction.User;

public class DBConnect {
	// Constants declared to save the tables names
	public final static String USER_TABLE = "users";
	public final static String BORROW_TABLE = "borrow";

	// Constant used to avoid spelling mistakes when retrieving the results of
	// queries
	public final static String ID = "id";

	// USED BY USERS
	public final static String LAST_NAME = "lastname";
	public final static String NAME = "name";
	public final static String EMAIL = "email";

	// USED BY BORROW
	public final static String USER_ID = "user_id";
	public final static String BOOK_ID = "book_id";
	public final static String BORROW_START = "borrow_start";
	public final static String BORROW_END = "borrow_end";

	// Constants declared to form the tables structure
	public final static String USER_TABLE_STRUCTURE = "CREATE TABLE " + USER_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\r\n" + LAST_NAME + " TEXT NOT NULL,\r\n" + NAME
			+ " TEXT NOT NULL,\r\n" + EMAIL + " TEXT UNIQUE NOT NULL);";

	public final static String BORROW_TABLE_STRUCTURE = "CREATE TABLE " + BORROW_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,\r\n" + USER_ID + " INTEGER NOT NULL,\r\n" + BOOK_ID
			+ " INTEGER NOT NULL,\r\n" + BORROW_START + " DATE NOT NULL,\r\n" + BORROW_END + " DATE,\r\n"
			+ "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "));";

	// Regroup all of these constants into variables
	private final static String[] tables = { USER_TABLE, BORROW_TABLE };
	private final static String[] creationQuery = { USER_TABLE_STRUCTURE, BORROW_TABLE_STRUCTURE };

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
			ResultSet res = smt.executeQuery("SELECT * FROM users");
			
			// While we have a row
			while(res.next()) {
				
				int usersID = res.getInt(ID);
				String firstname = res.getString(NAME);
				String lastname = res.getString(LAST_NAME);
				String email = res.getString(EMAIL);
				
				new User(usersID, lastname, firstname, email);
			}
			
			co.close();
			
			User.compteurId = User.getAllUser().get(User.getAllUser().size() - 1).getId() + 1;
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}
	
	/**
	 * Method to add an User to the database
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void addUserInTable(User userToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "INSERT INTO users VALUES(?,?,?,?)";
		
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
	 * Method to delete an User to the database
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteUserInTable(User userToAdd) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM users WHERE " + ID + " = ? and " + NAME + " = ? and " + LAST_NAME + " = ? and " + EMAIL + " = ?";
		
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
	 * Method to delete an User to the database
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyUserInTable(int userToModifysID, String oldLastname, String oldFirstname, String oldEmail, String newLastname, String newFirstname, String newEmail) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + USER_TABLE + " SET " + LAST_NAME + " = ?, " + NAME + " = ?, " + EMAIL + " = ?  "
				+ "WHERE " + ID + " = ? and " + LAST_NAME + " = ? and " + NAME + " = ? and " + EMAIL + " = ?";
		
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
	
	public static void main(String[] args) throws SQLException {
		System.out.println(BORROW_TABLE_STRUCTURE);
		createTables();
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
