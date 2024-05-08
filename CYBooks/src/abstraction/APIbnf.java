package abstraction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APIbnf {
	public static void main(String[] args) {
		try {
			URL url = new URL("https://catfact.ninja/fact");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			
			int responseCode = conn.getResponseCode();
			
			if(responseCode != 200) {
				throw new RuntimeException("HttpResponseCode : " + responseCode);
			}
			else {
				StringBuilder informationString = new StringBuilder();
				Scanner scanner = new Scanner(url.openStream());
				
				while(scanner.hasNext()) {
					
					informationString.append(scanner.nextLine());
				}
				
				scanner.close();
				
				System.out.println(informationString);
				
				JSONParser parse = new JSONParser();
				JSONObject obj = (JSONObject) parse.parse(String.valueOf(informationString));
				
				String fact = (String) obj.get("fact");
				long longueur = (long) obj.get("length");
				
				System.out.println("Fact : " + fact);
				System.out.println("Longueur : " + longueur);
				
				
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
}

