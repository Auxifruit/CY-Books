package abstraction.db;

import abstraction.Status;
import abstraction.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class that allows us to manage the Users in the database
 */
public class DataBaseUser {
	// Constants declared to save the tables names
	public final static String USER_TABLE = "users";
	public final static String BORROW_TABLE = "borrow";
	public final static String PROBLEM_TABLE = "problem";
	
	public final static String ID = "id";
	public final static String LAST_NAME = "lastname";
	public final static String FIRST_NAME = "firstname";
	public final static String EMAIL = "email";
	public final static String STATUS = "status";
		
	/**
	 * Method to read all the values of the table users
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readUsersTable() throws SQLException {
		try {
			Connection co = DataBaseManage.quickconnect();

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
			Connection co = DataBaseManage.quickconnect();
			
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
			Connection co = DataBaseManage.quickconnect();
			
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
	 * @param newLastname the user's new last name
	 * @param newFirstname the user's new first name
	 * @param newEmail the user's new email
	 * @param newStatus the user's new status
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void modifyUserInTable(User userToModify, String newLastname, String newFirstname, String newEmail, String newStatus) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "UPDATE " + USER_TABLE + " SET " + LAST_NAME + " = ?, " + FIRST_NAME + " = ?, " + EMAIL + " = ?, " + STATUS + " = ? "
				+ "WHERE " + ID + " = ?";
		
		try {
			Connection co = DataBaseManage.quickconnect();
			
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
}
