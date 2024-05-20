package abstraction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

import abstraction.exception.APIException;

public final class API {
	private final static String SPACE = "%20";
	private final static String QUOTE = "%22";

	// All the used identifier for the gallica API
	private final static String TITLE = "dc:title";
	private final static String CREATOR = "dc:creator";
	private final static String TYPE = "dc:type";
	private final static String DATE = "dc:date";
	private final static String LANGUAGE = "dc:language";
	private final static String IDENTIFIER = "dc:identifier"; // Equivalent to the ISBN
	private final static String PUBLISHER = "dc:publisher";
	private final static String FORMAT = "dc:format";

	// Regroup them into an array (Useful to loop through the informations from the
	// xml document)
	private final static String[] GALLICA_IDENTIFIERS = { TITLE, CREATOR, TYPE, DATE, LANGUAGE, IDENTIFIER, PUBLISHER, FORMAT };

	// Constants to retrieve information from the xml documemnt
	// private final static String SRW_RECORD = "recordData";
	// private final static String RECORD_DATA = "recordData";

	/**
	 * Create the query from the query information passed in parameter
	 * 
	 * @return The created query that is not normalized to the gallica norms
	 */
	private static String createQuery(String[] queryInfo, int maximumRecords) {
		// Initialise the base query
		String queryString = "https://gallica.bnf.fr/SRU?operation=searchRetrieve&version=1.2&maximumRecords="
				+ maximumRecords + "&startRecord=1&&query=gallica ";
		// Add the desired search filters
		for (int i = 0; i < queryInfo.length; i++) {
			if (!queryInfo[i].isEmpty()) {
				queryString += queryInfo[i];

				if (i < queryInfo.length - 1) {
					queryString += " and ";
				}
			}
		}
		return queryString;
	}

	/**
	 * Fetch the result of a query from the gallica AP and return a list of single
	 * element
	 * 
	 * @category API=>Queries
	 * @return A collection of DOM elements
	 */
	private static Document fetchAPIResulty(String query) {
		String normalizedQuery = normalizeQuery(query);
		Document doc = null;

		try {
			// Fetch the result of a query
			URL url = new URL(normalizedQuery);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			System.out.println("Error code: " + responseCode);
			if (responseCode != 200) {
				throw new APIException("The query has a problem ! HTTPS Response code: " + responseCode);

			}

			StringBuilder infoString = new StringBuilder();
			Scanner scan = new Scanner(url.openStream());

			while (scan.hasNext()) {
				infoString.append(scan.nextLine());
			}

			scan.close();

			// Convert the result into an XML file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new InputSource(new StringReader(infoString.toString())));

			// Normalize the document text to the XML standards
			doc.getDocumentElement().normalize();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * Construct a query from a string by converting it from the norms of gallica
	 * API i.e By remove 'quote' and 'spaces'
	 * 
	 * @category API=>misc
	 * @return The valid query
	 */
	private static String normalizeQuery(String query) {
		String normalizedQuery = query.replaceAll(" ", SPACE);
		normalizedQuery = normalizedQuery.replaceAll("\"", QUOTE);
		return normalizedQuery;

	}

	/**
	 * Go to the API fetch result and construct a list of book objects
	 * 
	 * @param query          The categories of the search
	 * @param maximumRecords The numbers max returned result
	 * @return A list of constructed book objects
	 **/
	public static List<Book> searchBook(String[] query, int maximumRecords) {
		if (maximumRecords <= 0) {
			// return new ArrayList<Book>();
			return new ArrayList<Book>();
		}

		String operationalQuery = createQuery(query, maximumRecords);
		operationalQuery = normalizeQuery(operationalQuery);
		System.out.println("QueryReady: " + operationalQuery);

		// Now that the query is ready we execute it
		Document queryResult = fetchAPIResulty(operationalQuery);
		if (queryResult == null) {
			throw new APIException("Document null from query call");
		}

		// From this document root we retrieve the attributes of each recorded element
		Element rootElement = queryResult.getDocumentElement();
		NodeList recordedNodes = rootElement.getElementsByTagName("srw:record");
		// Map to efficiently fetch the XML results
		HashMap<String, String> fetchMap = new HashMap<>();
		List<Book> returnedBooks = new ArrayList<>();

		// For each element in the returned document we create an instanced book from
		// its informations
		for (int i = 0; i < recordedNodes.getLength(); i++) {
			// Retrieve the current book
			Element currentElement = (Element) recordedNodes.item(i);	

			for (String idenfier : GALLICA_IDENTIFIERS) {
				Node currentNode = currentElement.getElementsByTagName(idenfier).item(0);
				fetchMap.put(idenfier, (currentNode == null) ? "": currentNode.getTextContent());
			}
			
			// Create a new instance of book with the fetched informations
			Book currentBook  = new Book(fetchMap.get(IDENTIFIER).replaceFirst("https://gallica.bnf.fr/ark:/12148/", ""), fetchMap.get(TITLE), fetchMap.get(CREATOR), 
					"0000-01-01", fetchMap.get(FORMAT) , fetchMap.get(TYPE), fetchMap.get(PUBLISHER));
			
			// Add the created book to the returned list and clear the map
			returnedBooks.add(currentBook);	
			// Clear the map when we find a new book
			fetchMap.clear();
		

		}

		return returnedBooks;
	}
	
	
	/** Check if the document identifier exist
	 * 
	 * @param identifier The identifier used for the search
	 * @return boolean expression (Whether or not the book document exist)
	 */
	public static boolean checkIdentifierExistance(String identifier) {
		String[] query = { "dc.identifier all \""+identifier+"\""};
		List<Book> book = searchBook(query, 15);
		return book.size() > 0;
	}

	public static void main(String[] args) {
		//String[] a = { "dc.title all \"Les Misérables\"", "dc.creator all \"Victor Hugo\"" };
		//searchBook(a, 70);
		System.out.println(checkIdentifierExistance("bpt6k1194845g"));
		// String[] a = { "dc.title all \"Les Misérables\"", "dc.creator all \"Victor
		// Hugo\"" };
		// System.out.println(normalizeQuery(executeQuery(a)));
		/*
		 * String query =
		 * "https://gallica.bnf.fr/SRU?operation=searchRetrieve&version=1.2&query=gallica dc.title all \"Les Misérables\""
		 * ; System.out.println(normalizeQuery(query)); Document doc =
		 * fetchAPIResulty(query); if(doc == null) { throw new
		 * APIException("Document null"); }
		 */

		/*
		 * Element rootElement = doc.getDocumentElement(); NodeList recordNodes =
		 * rootElement.getElementsByTagName("srw:record");
		 * 
		 * // Process each XML nodes for(int i = 0; i < recordNodes.getLength(); i++) {
		 * Element currentElement = (Element)recordNodes.item(i); Element recordData =
		 * (Element)currentElement.getElementsByTagName("srw:recordData").item(0);
		 * Element dc = (Element)recordData.getElementsByTagName("oai_dc:dc").item(0);
		 * 
		 * NodeList nameElement = dc.getElementsByTagName("dc:Title"); NodeList
		 * creatorElement = dc.getElementsByTagName("dc:creator");
		 * 
		 * 
		 * if(creatorElement.getLength() > 0) {
		 * System.out.println(creatorElement.item(0).getTextContent()); } }
		 * 
		 * System.out.println("New Test"); NodeList titles =
		 * rootElement.getElementsByTagName("dc:title"); for(int i = 0; i <
		 * titles.getLength(); i++) {
		 * System.out.println(titles.item(i).getTextContent()); }
		 */
	}
}
