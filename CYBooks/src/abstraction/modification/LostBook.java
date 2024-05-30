package abstraction.modification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import abstraction.db.DataBaseManage;

public class LostBook {
	/** Add a book to the lost book table */
	public static void addLostBook(int borrowID, String bookID, int userID) throws SQLException{
		try {
			Connection co = DataBaseManage.quickconnect();
			Statement smt = co.createStatement();
			String query = "INSERT INTO lostbook VALUES (" + borrowID + "\"" + bookID + "\", " + "" + userID + ");";

			smt.execute(query);
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public  static void removeLostBook(String bookID) {
		try {
			Connection co = DataBaseManage.quickconnect();
			Statement smt = co.createStatement();
			String query = "DELETE FROM lostbook WHERE book_id = " + bookID +";";

			smt.execute(query);
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
