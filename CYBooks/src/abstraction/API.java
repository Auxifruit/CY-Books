package abstraction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

import abstraction.exception.APIException;


public final class API {
	private final static String SPACE = "%20";
	private final static String QUOTE = "%22";
	
	// All the used identifier for the gallica API
	private final static String TITLE = "dc.title";
	private final static String CREATOR = "dc.creator";
	private final static String TYPE = "dc.type";
	private final static String DATE = "dc.date";
	private final static String LANGUAGE = "dc.language";
	private final static String IDENTIFIER = "dc.identifier";
	
	/** Put these identifiers in a static array */
	private final static String[] GALLICA_QUERY_KEYS = {TITLE, CREATOR, TYPE, DATE, LANGUAGE, IDENTIFIER};

	
	
	/** Exectute */
	public static void executeQuery(String[] queryInfo) {
		String queryString = "";
		for (int i = 0; i < GALLICA_QUERY_KEYS.length; i++) {
			if(!queryInfo[i].isEmpty()) {
				queryString = GALLICA_QUERY_KEYS + " " + queryInfo[i];
			}
		}
	}
	
	
	
	
	
		
	/**Fetch the result of  a query from the gallica AP aand return a list of single element 
	 * @category API=>Queries
	 * @return A collection of DOM elements
	 */
	private static Document fetchAPIResulty(String query) {
		String normalizedQuery = normalizeQuery(query);
		Document doc = null;
		
		try {
			// Fetch the result of a query
			URL url = new URL(normalizedQuery);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			System.out.println("Error code: " + responseCode);
			if(responseCode != 200) {
				throw new APIException("The query has a problem ! HTTPS Response code: " + responseCode);
				
			}
			
			StringBuilder infoString = new StringBuilder();
			Scanner scan = new Scanner(url.openStream());
			
			while(scan.hasNext()) {
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
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	/** Construct a query from a string by converting it from the norms of gallica API i.e By remove 'quote' and 'spaces'
	 * @category API=>misc 
	 * @return The valid query */
	private static String normalizeQuery(String query) {
		String normalizedQuery = query.replaceAll(" ", SPACE);
		normalizedQuery = normalizedQuery.replaceAll("\"", QUOTE);
		return normalizedQuery;
		
	}

	
	public static void main(String[] args) {
		String query = "https://gallica.bnf.fr/SRU?operation=searchRetrieve&version=1.2&query=gallica dc.title all \"Les Misérables\"";
		System.out.println(normalizeQuery(query));
		Document doc = fetchAPIResulty(query);
		if(doc == null) {
			throw new APIException("Document null");
		}
		
		/*
		Element rootElement = doc.getDocumentElement();
		NodeList recordNodes = rootElement.getElementsByTagName("srw:record");
		
		// Process each XML nodes
		for(int i = 0; i < recordNodes.getLength(); i++) {
			Element currentElement = (Element)recordNodes.item(i);
			Element recordData = (Element)currentElement.getElementsByTagName("srw:recordData").item(0);
			Element dc = (Element)recordData.getElementsByTagName("oai_dc:dc").item(0);
			
			NodeList nameElement = dc.getElementsByTagName("dc:Title");
			NodeList creatorElement = dc.getElementsByTagName("dc:creator");
			
			
			if(creatorElement.getLength() > 0) {
				System.out.println(creatorElement.item(0).getTextContent());
			}
		}
		
		System.out.println("New Test");
		NodeList titles = rootElement.getElementsByTagName("dc:title");
		for(int i = 0; i < titles.getLength(); i++) {
			System.out.println(titles.item(i).getTextContent());
		}*/
	}
}
