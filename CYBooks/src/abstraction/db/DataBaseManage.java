package abstraction.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The class that allows us to manage the database
 */
public class DataBaseManage {
	// Constants declared to save the tables names
	protected final static String USER_TABLE = "users";
	protected final static String BORROW_TABLE = "borrow";
	protected final static String PROBLEM_TABLE = "problem";
	
	// MODIFICATION LOST
	protected final static String LOST_TABLE = "lostbook";

	// Constant used to avoid spelling mistakes when retrieving the results of
	// queries
	protected final static String ID = "id";

	// USED BY USERS
	protected final static String LAST_NAME = "lastname";
	protected final static String FIRST_NAME = "firstname";
	protected final static String EMAIL = "email";
	protected final static String STATUS = "status";

	// USED BY BORROW
	protected final static String USER_ID = "user_id";
	protected final static String BOOK_ID = "book_id";
	protected final static String BORROW_START = "borrow_start";
	protected final static String BORROW_END = "borrow_end";
	protected final static String BORROW_REAL_END = "borrow_real_end";
	protected final static String BORROW_DURATION = "duration";
	protected final static String BORROW_LATE = "late";
	// MODIFICATION  LOST
	protected final static String BORROW_LATE_LOST = "lost";
	
	
	// USED BY PROBLEM
	protected final static String BORROW_ID = "borrow_id";
	protected final static String PROBLEM_TEXT = "problem_text";

	// Constants declared to form the tables structure
	protected final static String USER_TABLE_STRUCTURE = "CREATE TABLE " + USER_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + LAST_NAME + " TEXT NOT NULL,\r\n" + FIRST_NAME
			+ " TEXT NOT NULL,\r\n" + EMAIL + " TEXT UNIQUE NOT NULL,\r\n" + STATUS + " TEXT NOT NULL);";

	protected final static String BORROW_TABLE_STRUCTURE = "CREATE TABLE " + BORROW_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + USER_ID + " INTEGER NOT NULL,\r\n" + BOOK_ID + ""
			+ " TEXT NOT NULL,\r\n" + BORROW_START + " TEXT NOT NULL,\r\n" + BORROW_END + " TEXT,\r\n"
			+ BORROW_REAL_END + " TEXT,\r\n" + BORROW_DURATION + " INTEGER,\r\n" + BORROW_LATE + " TEXT, \r\n"
			+ "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "));";
	
	protected final static String PROBLEM_TABLE_STRUCTURE = "CREATE TABLE " + PROBLEM_TABLE + " (\r\n" + ID
			+ " INTEGER PRIMARY KEY,\r\n" + BORROW_ID + " INTEGER NOT NULL,\r\n" + PROBLEM_TEXT
			+ " TEXT NOT NULL,\r\n" + BORROW_LATE_LOST + " TEXT, "
			+ "FOREIGN KEY (" + BORROW_ID + ") REFERENCES " + BORROW_TABLE + "(" + ID + "));";
	
	// MODIFICATION LOST
	protected final  static String LOST_TABLE_STRUCTURE = "CREATE TABLE " + LOST_TABLE + " (\r\n"
			+ BORROW_ID + " TEXT NOT NULL,\r\n"
	        + ID + " INTEGER PRIMARY KEY,\r\n" + USER_ID + " INTEGER NOT NULL,\r\n" + BOOK_ID
			+ ", FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + ")"
			+ " FOREIGN KEY (" + BORROW_ID + ") REFERENCES " + BORROW_TABLE + "(" + ID + "));";
			

	// Regroup all of these constants into variables
	private final static String[] tables = { USER_TABLE, BORROW_TABLE, PROBLEM_TABLE, LOST_TABLE };
	private final static String[] creationQuery = { USER_TABLE_STRUCTURE, BORROW_TABLE_STRUCTURE, PROBLEM_TABLE_STRUCTURE, LOST_TABLE_STRUCTURE };

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
	
	public static void main(String[] args) throws SQLException {
		createTables();
	}

}
