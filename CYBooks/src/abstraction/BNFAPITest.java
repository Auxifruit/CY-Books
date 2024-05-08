package abstraction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BNFAPITest {
    public static void main(String[] args) {
        try {
            // Construire l'URL de la requête
            String urlStr = "https://gallica.bnf.fr/services/OAIRecord?ark=bpt6k62551748&format=json";
            URL url = new URL(urlStr);
            
            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            // Lire la réponse
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            
            // Afficher la réponse
            System.out.println("Réponse de l'API Gallica:");
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}