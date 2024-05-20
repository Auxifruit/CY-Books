package abstraction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import abstraction.db.DBConnect;

public class Main {

	
    public static void UserMenu() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) Stop everything 2) Add a user 3) Check the list of users 4) Modify a user 5) Delete a user");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (wdyw) {
                case 1:
                    System.out.print("Program ending");
                    return; // Exit the method

                case 2:
                    System.out.print("First name? ");
                    String firstnameText = scanner.nextLine();
                    System.out.print("Last name? ");
                    String lastnameText = scanner.nextLine();
                    System.out.print("Email? ");
                    String emailText = scanner.nextLine();
                    User userToCreate = new User(lastnameText, firstnameText, emailText);
                   // DBConnect.addUserInTable(userToCreate);
                    
                    System.out.println("User added: " + userToCreate.toString());
                    break;

                case 3:
                    for (User u : User.getAllUser()) {
                        System.out.println(u.toString());
                    }
                    break;

                case 4:
                    System.out.println("What is the ID of the user you want to modify?");
                    int idToModify = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("First name? ");
                    String firstnameNew = scanner.nextLine();
                    System.out.print("Last name? ");
                    String lastnameNew = scanner.nextLine();
                    System.out.print("Email? ");
                    String emailNew = scanner.nextLine();

                    for (User u : User.getAllUser()) {
                        if (u.getId() == idToModify) {
                            u.setEmail(emailNew);
                            u.setFirstname(firstnameNew);
                            u.setLastname(lastnameNew);
                            System.out.println("User modified: " + u.toString());
                            break; // Once modified, no need to continue looping
                        }
                    }
                    break;

                case 5:
                    System.out.println("What is the ID?");
                    int idToDelete = scanner.nextInt();
                    Iterator<User> iterator = User.getAllUser().iterator();
                    while (iterator.hasNext()) {
                        User u = iterator.next();
                        if (u.getId() == idToDelete) {
                            iterator.remove();
                            System.out.println("User deleted: " + u.toString());
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void BorrowMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) View borrow list 2) Borrow a book 3) Return a book 4) Search a book 5) Exit");

            int wdyw = scanner.nextInt();
            scanner.nextLine(); 

            switch (wdyw) {
            case 1:
                List<Borrow> borrowList = Borrow.getAllBorrow();
                if (borrowList.isEmpty()) {
                    System.out.println("Aucun emprunt pour le moment.");
                } else {
                    for (Borrow n : borrowList) {
                        System.out.println(n.toString());
                    }
                }
                break;

                case 2:
                	if ( !User.getAllUser().isEmpty()) {
                    System.out.println("What is the user ID?");
                    int userId = scanner.nextInt();
                    scanner.nextLine(); 
                    while (!User.getExist(userId)) {
                    	 System.out.println("This user doesn't exist ,What is the user ID?");
                    	userId = scanner.nextInt();
                    	scanner.nextLine(); 
                    }

                    System.out.println("What is the book's ISBN?");
                    String bookISBN = scanner.nextLine();

                    LocalDate dateBorrow = LocalDate.now();
                    Borrow newBorrow = new Borrow(userId, bookISBN, dateBorrow);
                   // DBConnect.addBorrowInTable(newBorrow);
                    System.out.println("Borrow added: " + newBorrow.toString());
                	}
                	else {
                		System.out.println("There is no user so you cant borrow ");
                	}
                    break;

                case 3:
                    System.out.println("What is the ID of the borrow to return?");
                    int borrowId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Borrow borrowToReturn = null;
                    for (Borrow borrow : Borrow.getAllBorrow()) {
                        if (borrow.getId() == borrowId) {
                            borrowToReturn = borrow;
                            break;
                        }
                    }

                    if (borrowToReturn != null) {
                        LocalDate effectiveReturnDate = LocalDate.now();
                        borrowToReturn.setEffectiveReturnDate(effectiveReturnDate);
                        borrowToReturn.checkBorrowLate();
                        System.out.println("Borrow returned: " + borrowToReturn.toString());
                    } else {
                        System.out.println("Borrow not found.");
                    }
                    break;

                case 4:
                    System.out.println("Search functionality not yet implemented.");
                    break;

                case 5:
                    System.out.println("Exiting borrow menu.");
                    return; // Exit the BorrowMenu

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }



    public static void Commandline() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) User 2) Book and Borrow Management");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); 

            switch (wdyw) {
                case 1:
                    UserMenu();
                    break;

                case 2:
                    BorrowMenu();
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
    	try {
    		DBConnect.readUsersTable();
		DBConnect.readBorrowsTable();
		} catch (SQLException e) {

			e.printStackTrace();
		}
    	Commandline();
    }
}
