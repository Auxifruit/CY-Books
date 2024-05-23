package abstraction;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In this class there's multiple static method used across the project
 */
public class GeneralUtils {

	/**
	 * Method taking a String in the LocalDate format and return the LocalDate from the String
	 * @param dateString the String we want to parse to have a LocalDate
	 * @return the LocalDate from the entered String
	 */
	public static LocalDate stringToLocalDate(String dateString) {
		// We split the date's String to get the day, month and year
		String[] dateParsed = dateString.split("-");
		
		// We get the values of the parsing
		int year = Integer.valueOf(dateParsed[0]);
		int month = Integer.valueOf(dateParsed[1]);
		int day = Integer.valueOf(dateParsed[2]);
		
		// We use these element to return our LocalDate
		return LocalDate.of(year, month, day);
	}
	/**
	 * Change date format to "dd-mm-yyyy" to "yyyy-mm-dd"
	 * @param dateString the date we want to change the format
	 * @return the date in the new format
	 */
	public String changeDateFormat(String dateString) {
		// We split the date's String to get the day, month and year
		String[] dateParsed = dateString.split("-");
		
		// We get the values of the parsing
		int year = Integer.valueOf(dateParsed[2]);
		int month = Integer.valueOf(dateParsed[1]);
		int day = Integer.valueOf(dateParsed[0]);
		
		return year + "-" + month + "-" + day;
	}
	
	/**
	 * Method to check if a String matches the e-mail format
	 * @param email the string we want to check the format
	 * @return true if String matches the e-mail format and false if not
	 */
	public static boolean isValidEmail(String email) {
		// Regular expression to validate an e-mail
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object containing our regular expression
        Pattern pattern = Pattern.compile(regex);

        // Create an Matcher object to search a motif in our regular expression
        Matcher matcher = pattern.matcher(email);

        // Check if the String matches the e-mail format
        return matcher.matches();
    }
}
