package abstraction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import abstraction.db.DataBaseUser;
import abstraction.db.DataBaseBorrow;
import abstraction.exception.*;

public class Main {

	public static void userMenu() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) Stop 2) Add a user 3) View the list of users 4) Modify a user 5) Delete a user");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (wdyw) {
                case 1:
                    System.out.println("Program ending");

                    return;

                case 2:
                    System.out.print("First name? ");
                    String firstnameText = scanner.nextLine();
                    System.out.print("Last name? ");
                    String lastnameText = scanner.nextLine();
                    System.out.print("Email? ");
                    String emailText = scanner.nextLine();
                    User userToCreate = new User(lastnameText, firstnameText, emailText);
                    DataBaseUser.addUserInTable(userToCreate);

                    System.out.println("User added: " + userToCreate);
                    break;

                case 3:
                    List<User> users = User.getAllUser();
                    if (users.isEmpty()) {
                        System.out.println("No users found.");
                    } else {
                        for (User u : users) {
                            System.out.println(u);
                        }
                    }
                    break;

                case 4:
                    try {
                        System.out.println("What is the ID of the user to modify?");
                        int idToModify = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        if (!User.isExisting(idToModify)) {
                            throw new UserNotExistException("This user does not exist: " + idToModify);
                        }

                        System.out.print("First name? ");
                        String firstnameNew = scanner.nextLine();
                        System.out.print("Last name? ");
                        String lastnameNew = scanner.nextLine();
                        System.out.print("Email? ");
                        String emailNew = scanner.nextLine();

                        User userToModify = User.getUserById(idToModify);
                        if (userToModify != null) {
                            userToModify.setFirstname(firstnameNew);
                            userToModify.setLastname(lastnameNew);
                            userToModify.setEmail(emailNew);
                            System.out.println("User modified: " + userToModify);
                        }
                    } catch (UserNotExistException e) {
                        System.out.println(e.getMessage());
                        System.out.println("You need to redo the procedure.");
                    }
                    break;

                case 5:
                    try {
                        System.out.println("What is the ID of the user to delete?");
                        int idToDelete = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        if (!User.isExisting(idToDelete)) {
                            throw new UserNotExistException("This user does not exist: " + idToDelete);
                        }

                        User userToDelete = User.getUserById(idToDelete);
                        if (userToDelete != null) {
                            User.getAllUser().remove(User.getUserById(idToDelete));
                            DataBaseUser.deleteUserInTable(userToDelete);
                            System.out.println("User deleted: " + userToDelete);
                        }
                    } catch (UserNotExistException e) {
                        System.out.println(e.getMessage());
                        System.out.println("You need to redo the procedure.");
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void borrowMenu() throws SQLException,BookCantBeBorrowedException,UserCanBorrowException, UserNotExistException, BookNotExistException, BorrowNotExistException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) View borrow list 2) Borrow a book 3) Return a book 4) Search a book 5) Quit");

            int wdyw = scanner.nextInt();
            scanner.nextLine(); 

            switch (wdyw) {
                case 1:
                    List<Borrow> borrowList = Borrow.getAllBorrow();
                    if (borrowList.isEmpty()) {
                        System.out.println("No borrows at the moment.");
                    } else {
                        for (Borrow n : borrowList) {
                            System.out.println(n);
                        }
                    }
                    break;

                case 2:
                    if (!User.getAllUser().isEmpty()) {
                        try {
                            System.out.println("What is the user ID?");
                            int userId = scanner.nextInt();
                            scanner.nextLine(); // Clear the buffer

                            if (!User.isExisting(userId)) {
                                throw new UserNotExistException("This user does not exist: " + userId);
                            }

                            if (!Borrow.canUserBorrow(userId)) {
                            	throw new UserCanBorrowException("This user can't borrow he has to much borrow "+ userId);
                            }
                            
                            
                            System.out.println("What is the book's ISBN?");
                            String bookISBN = scanner.nextLine();

                            if (!API.checkIdentifierExistance(bookISBN)) {
                                throw new BookNotExistException("This book does not exist: " + bookISBN);
                            }
                            
                            if (!Borrow.canBookBeBorrowed(bookISBN)) {
                            	throw new BookCantBeBorrowedException("This book can't be borrowed");
                            }

                            LocalDate dateBorrow = LocalDate.now();
                            Borrow newBorrow = new Borrow(userId, bookISBN, dateBorrow);
                            DataBaseBorrow.addBorrowInTable(newBorrow);
                            System.out.println("Borrow added: " + newBorrow);

                        } catch (UserNotExistException | BookNotExistException | UserCanBorrowException | BookCantBeBorrowedException e) {
                            System.out.println(e.getMessage());
                            System.out.println("You need to redo the procedure.");
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No users available.");
                    }
                    break;

                case 3:
                	try {
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

                    if (borrowToReturn == null ) {
                    	throw new BorrowNotExistException("This borrow id does not exist " + borrowId);
                    }
                    
  
                        LocalDate effectiveReturnDate = LocalDate.now();
                        borrowToReturn.setEffectiveReturnDate(effectiveReturnDate);
                        borrowToReturn.checkBorrowLate();
                        System.out.println("Borrow returned: " + borrowToReturn);
                	}
                	catch (BorrowNotExistException e) {
                        System.out.println(e.getMessage());
                        System.out.println("You need to redo the procedure.");
                	}
                    break;

                case 4:
                    String[] searchCriteria = { "dc.title all \"Les Misérables\"", "dc.creator all \"Victor Hugo\"" };            	
                    List<Book> books = API.searchBook(searchCriteria, 1);
                    for (Book book : books) {
                        System.out.println(book);
                    }
                    break;

                case 5:
                    System.out.println("Exiting borrow menu.");
                
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void commandline() throws IOException, SQLException, UserNotExistException, BookNotExistException, BorrowNotExistException, BookCantBeBorrowedException, UserCanBorrowException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1) User 2) Book and Borrow Management 3) Finish the program");
            int wdyw = scanner.nextInt();
            scanner.nextLine(); 

            switch (wdyw) {
                case 1:
                    userMenu();
                    break;
                    
                case 2:
                    borrowMenu();
                    break;
                    
                case 3:
                    
                    return;
                    
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException, UserNotExistException, BookNotExistException, BorrowNotExistException, BookCantBeBorrowedException, UserCanBorrowException {
        try {
        	DataBaseUser.readUsersTable();
        	DataBaseBorrow.readBorrowsTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        commandline();
        System.out.println("The program has terminated");
    }
}
