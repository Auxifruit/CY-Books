import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import java.io.IOException;
import java.net.URL;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class UserInit {
    public static final String FILE_PATH = "User.txt";

    public static String[][] getNRandomUser(int n) {
        String[][] users = new String[n][5]; // Définir la taille du tableau en fonction du nombre d'utilisateurs

        try {
            ObjectMapper mapper = new ObjectMapper();
            URL url = new URL("https://randomuser.me/api/?nat=fr&results=" + n);
            JsonNode rootNode = mapper.readTree(url);
            JsonNode resultsNode = rootNode.get("results");

            int i = 0; // Compteur pour parcourir le tableau users

            for (JsonNode resultNode : resultsNode) {
                String lastName = resultNode.get("name").get("last").asText();
                String firstName = resultNode.get("name").get("first").asText();
                String email = resultNode.get("email").asText();
                String uuid = resultNode.get("login").get("uuid").asText();

                // Générer un nombre aléatoire entre 0 et 4 pour le numéro de compte
                Random random = new Random();
                int randomNumber = random.nextInt(5);

                // Stocker les informations dans le tableau
                users[i][0] = uuid;
                users[i][1] = firstName;
                users[i][2] = lastName;
                users[i][3] = email;
                users[i][4] = String.valueOf(randomNumber);

                i++; // Incrémenter le compteur
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

public static void addUserInAFileTXT(String[][] usersInfo) {
    try {
        // Créer un objet FileWriter avec le chemin du fichier et en mode append
        FileWriter fileWriter = new FileWriter(FILE_PATH, true);

        // Créer un objet BufferedWriter en enveloppant le FileWriter
        BufferedWriter writer = new BufferedWriter(fileWriter);

        // Parcourir les informations des utilisateurs
        for (String[] userInfo : usersInfo) {
            String user = "";
            for (String info : userInfo) {
                // Concaténer les informations des utilisateurs avec un point-virgule comme séparateur
                user = user + info + ";";
            }
            // Écrire la ligne de l'utilisateur dans le fichier avec un saut de ligne
            writer.write(user + "\n");
        }

        // Fermer le BufferedWriter
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public static void BddToUser(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            int numberOfLines = countLines(filePath);

            // Créer un objet BufferedReader en enveloppant le FileReader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Déclarez une variable pour stocker chaque ligne lue
            String line;

            // Lisez chaque ligne du fichier tant qu'il y en a
            while ((line = bufferedReader.readLine()) != null) {
                String[] UsersInfos = line.split(";");
                for( String k : UsersInfos){
                    System.out.println(k);
                }
            }

            // Fermer le BufferedReader
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countLines(String filePath) {
        int count = 0;
        try {
            // Créer un objet FileReader en spécifiant le chemin du fichier
            FileReader fileReader = new FileReader(filePath);

            // Créer un objet BufferedReader en enveloppant le FileReader
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Lire chaque ligne du fichier tant qu'il y en a
            while (bufferedReader.readLine() != null) {
                // Incrémenter le compteur pour chaque ligne lue
                count++;
            }

            // Fermer le BufferedReader
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void main(String[] args) {
        // Appeler la méthode BddToUser() pour lire les informations de l'utilisateur depuis le fichier
        //BddToUser(FILE_PATH);

        // Appeler la méthode getNRandomUser() pour obtenir les informations de N utilisateurs
       String[][] users = getNRandomUser(10);

        // Ajouter les utilisateurs dans le fichier
        addUserInAFileTXT(users);

        // Afficher les informations de chaque utilisateur

    }
}
