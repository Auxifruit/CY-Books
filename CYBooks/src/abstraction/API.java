package abstraction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;


public final class API {
	private final static String SPACE = "%20";
	private final static String QUOTE = "%22";
	
	
	/** Execute a query from the gallica API, parse it and return a list of single element 
	 * @category API=>Queries
	 * @return A collection of DOM elements
	 */
	public static Document execQuery(String query) {
		String normalizedQuery = normalizeQuery(query);
		Document doc = null;
		
		try {
			// Fetch the result of a query
			URL url = new URL(normalizeQuery(normalizedQuery));
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			if(responseCode != 200) {
				throw new RuntimeException("HttpsResponseCode: " + responseCode);
				
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
	
	/** Return the ISBN of a book, by truncating the returned dc:identifier of the query result
	 * @category API=>Queries
	 * @return The ISBN number, to identify a book inside the bnf database */
	public static String getISBN(String dcIdentifier) {
		return "";
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
		Document doc = execQuery(query);
		if(doc == null) {
			throw new RuntimeException("Document null");
		}
		
		
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
		}
		
		
		/*
		try {
			// Fetch the result of a query
			//String query = "https://gallica.bnf.fr/SRU?operation=searchRetrieve&version=1.2&query=gallica%20dc.title%20all%20%22Les%20misérables%22";
			//String query = "https://gallica.bnf.fr/SRU?operation=searchRetrieve&version=1.2&query=gallica%20dc.title%20all%20%22Les%20Misérables%22";
			URL url = new URL(normalizeQuery(query));
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			if(responseCode != 200) {
				throw new RuntimeException("HttpsResponseCode: " + responseCode);
				
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
			Document doc = dBuilder.parse(new InputSource(new StringReader(infoString.toString())));
			
			// Normalize the document text to the XML standards
			doc.getDocumentElement().normalize();
			
			Element rootElement = doc.getDocumentElement();
			NodeList recordNodes = rootElement.getElementsByTagName("srw:record");
			
			// Process each XML nodes
			for(int i = 0; i < recordNodes.getLength(); i++) {
				Element currentElement = (Element)recordNodes.item(i);
				Element recordData = (Element)currentElement.getElementsByTagName("srw:recordData").item(0);
				Element dc = (Element)recordData.getElementsByTagName("oai_dc:dc").item(0);
				
				NodeList nameElement = dc.getElementsByTagName("dc:Title");
				NodeList creatorElement = dc.getElementsByTagName("dc:creator");
				
				System.out.println(nameElement.getLength() + " " + creatorElement.getLength());
				
				if(creatorElement.getLength() > 0) {
					System.out.println(creatorElement.item(0).getTextContent());
				}
			}
			
			System.out.println("New Test");
			NodeList titles = rootElement.getElementsByTagName("dc:title");
			for(int i = 0; i < titles.getLength(); i++) {
				System.out.println(titles.item(i).getTextContent());
			}
			
			
			
			
			
			
			
			
			
			
		}
		
		catch (Exception e){
			e.printStackTrace();
		}*/
	}
}
