package abstraction;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void UserMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Que voulez-vous faire ?");
            System.out.println("1) Arreter tout  2) Ajouter un utilisateur 3) Verifier la liste des utilisateurs 4) Modifier un utilisateur 5) Supprimer un User");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (wdyw) {
                case 1:
                    System.out.print("Fin du prog");
                    return; // Sort de la méthode

                case 2:
                    System.out.print("Prénom ? ");
                    String firstnameText = scanner.nextLine();
                    System.out.print("Nom de famille ? ");
                    String lastnameText = scanner.nextLine();
                    System.out.print("Email ? ");
                    String emailText = scanner.nextLine();
                    User userToCreate = new User(lastnameText, firstnameText, emailText);
                    UserFile.addUserInAFileTXT(userToCreate);
                    break;

                case 3:
                    for (User u : User.getAllUser()) {
                        System.out.println(u.toString());
                        }
                    
                    
                    break;

                case 4:
                    System.out.println("What is the id of the user that you want to modify?");
                    int idTomodify = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    System.out.print("Prénom ? ");
                    String firstnameNew = scanner.nextLine();
                    System.out.print("Nom de famille ? ");
                    String lastnameNew = scanner.nextLine();
                    System.out.print("Email ? ");
                    String emailNew = scanner.nextLine();

                    for (User u : User.getAllUser()) {
                        if (u.getId() == idTomodify) {
                        	u.setEmail(emailNew);
                        	u.setFirstname(firstnameNew);
                        	u.setLastname(lastnameNew);
                            UserFile.modifyUserInAFileTXT(u, lastnameNew, firstnameNew, emailNew);
                            break; // Once modified, no need to continue looping
                        }
                    }
                    break;

                case 5:
                    System.out.println("Quel est son id ?");
                    int id1 = scanner.nextInt();
                    Iterator<User> iterator = User.getAllUser().iterator();
                    while (iterator.hasNext()) {
                        User u = iterator.next();
                        if (u.getId() == id1) {
                            iterator.remove();
                            UserFile.deleteUserInAFileTXT(u);
                        }
                    }
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    public static void Commandline() throws IOException {
        Scanner scanner = new Scanner(System.in);
        UserFile.readUsersFromAFileTXT();

        while (true) {
            System.out.println("Que voulez-vous faire ?");
            System.out.println(" 1) User 2) Borrow 3) Book ");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); 

            switch (wdyw) {
                case 1:
                    UserMenu();
                    break;

                case 2:
                    System.out.println(" Borrow ");
                    break;

                case 3:
                    System.out.println(" Book ");
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Commandline();
    }
}
