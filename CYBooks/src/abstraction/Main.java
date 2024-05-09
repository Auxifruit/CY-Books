package abstraction;

import java.time.LocalDate;

public class Main {
	public static void main(String[] args) {
		User user1 = new User("Mark", "Evans", "Mark@gmail.com");
		User user2 = new User("Axel", "Blaze", "Axel@gmail.com");
		
		Book book1 = new Book(123, "Les misérables", "Victor Hugo", "1862-01-01", "Tragédie", "Roman", "Albert Lacroix");
		Book book2 = new Book(123, "Les misérables", "Victor Hugo", "1862-01-01", "Tragédie", "Roman", "Albert Lacroix");
		
		Borrow borrow1 = new Borrow(user1, book1, LocalDate.now());
		
		System.out.println("Users :");
		System.out.println("\n" + user1);
		System.out.println("\n" + user2);
		System.out.println("\nuser1.equals(user2) : " + user1.equals(user2));
		
		System.out.println("\nBook :");
		System.out.println(book1);
		System.out.println("\nbook1.equals(book2) : " + book1.equals(book2));
		
		System.out.println("\nBorrow :");
		System.out.println(borrow1);
		System.out.println("\nIs the borrow late ? " + borrow1.isBorrowLate());
		
		
	}
}
