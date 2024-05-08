import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private String nom;
    private int id;
    private String prenom;
    private static int compteurId = 1;
    private static List<Utilisateur> utilisateurs = new ArrayList<>();

    // Constructeur
    public Utilisateur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = compteurId++;
        utilisateurs.add(this);
    }

    // Méthode pour rechercher un utilisateur par son ID
    public static Utilisateur chercherUtilisateurParId(int id) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                return utilisateur;
            }
        }
        return null; // Retourner null si aucun utilisateur n'est trouvé avec cet ID
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Méthode pour afficher les informations de l'utilisateur
    @Override
    public String toString() {
        return "Utilisateur{" +
                "nom='" + nom + '\'' +
                ", id=" + id +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
