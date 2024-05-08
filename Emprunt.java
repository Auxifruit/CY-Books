import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Utilisateur utilisateur;
    private Livre livre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;
    private static int compteurId = 1;

    // Constructeur
    public Emprunt(Utilisateur utilisateur, Livre livre, LocalDate dateEmprunt) {
        this.id = compteurId++;
        this.utilisateur = utilisateur;
        this.livre = livre;
        this.dateEmprunt = dateEmprunt;
        // Par défaut, la date de retour est initialisée à null
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    // Méthode pour afficher les informations de l'emprunt
    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", utilisateur=" + utilisateur.getNom() + " " + utilisateur.getPrenom() +
                ", livre=" + livre.getTitre() +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetour=" + (dateRetour != null ? dateRetour : "Non retourné") +
                '}';
    }
    
    public static void main(String[] args) {
        // Création d'utilisateurs
        Utilisateur utilisateur1 = new Utilisateur("Doe", "John");
        Utilisateur utilisateur2 = new Utilisateur("Smith", "Alice");

        // Création de livres
        Livre livre1 = new Livre("Le Seigneur des Anneaux", "J.R.R. Tolkien", 1954);
        Livre livre2 = new Livre("Harry Potter à l'école des sorciers", "J.K. Rowling", 1997);

        // Emprunts
        Emprunt emprunt1 = new Emprunt(utilisateur1, livre1, LocalDate.of(2024, 5, 10));
        Emprunt emprunt2 = new Emprunt(utilisateur2, livre2, LocalDate.of(2024, 5, 8));

        // Affichage des emprunts
        System.out.println(emprunt1);
        System.out.println(emprunt2);
    }
}
