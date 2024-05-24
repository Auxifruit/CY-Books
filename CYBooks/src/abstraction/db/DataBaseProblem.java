package abstraction.db;

import abstraction.Problem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class that allows us to manage the Problems in the database
 */
public class DataBaseProblem {
	// Constants declared to save the tables names
	protected final static String PROBLEM_TABLE = "problem";

	protected final static String ID = "id";

	protected final static String BORROW_ID = "borrow_id";
	protected final static String PROBLEM_TEXT = "problem_text";
		
	/**
	 * Method to read all the values of the table problem
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void readProblemsTable() throws SQLException {
		try {
			Connection co = DataBaseManage.quickconnect();

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
		final String query = "INSERT INTO " + PROBLEM_TABLE + " (" + ID + ", " + BORROW_ID + ", " + PROBLEM_TEXT + ") " + "VALUES (?, ?, ?)";
		
		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			int problemsID = problemToAdd.getID();
			int borrowsID = problemToAdd.getBorrowsID();
			String text = problemToAdd.getText();

			// We set the values of the query
			ps.setInt(1, problemsID);
			ps.setInt(2, borrowsID);
			ps.setString(3, text);
			
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
			Connection co = DataBaseManage.quickconnect();
			
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
	
	/**
	 * Method to delete a Problem from the database with the borrow's ID
	 * @param borrowsID the borrow's ID
	 * @throws SQLException if we have an exception about SQL
	 */
	public static void deleteProblemInTable(int borrowsID) throws SQLException {
		// String for the query, the ? correspond to the values we want to assign
		final String query = "DELETE FROM " + PROBLEM_TABLE + " WHERE " + BORROW_ID + " = ?";

		try {
			Connection co = DataBaseManage.quickconnect();
			
			// Allow us to prepare the query to execute it later
			PreparedStatement ps = co.prepareStatement(query);

			// We set the values of the query
			ps.setInt(1, borrowsID);

			ps.executeUpdate();
			
			co.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Fail to connect to the database for unknown reasons");
		}
	}	
}
