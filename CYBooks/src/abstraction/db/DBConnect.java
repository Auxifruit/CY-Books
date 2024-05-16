package abstraction.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
