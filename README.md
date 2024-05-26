![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/biblio.png)

# PROJET CY BOOKS ING1

## Table of content
1. [GENERALS INFORMATIONS](#generals-informations)
2. [UTILISATION](#utilisation)
3. [HOME PAGE](#home-page)
4. [USER](#user)
5. [BORROW](#borrow)
6. [BOOK](#book)
7. [TASK LIST](#task-list)
8. [CREATORS](#creators)

## GENERALS INFORMATIONS
CY-Books is a graphical application used by librarians to manage a library, with all functionalities such as user registration, book management, stock and borrowing.<br>
The application uses BNF's Gallica API to retrieve book information.

## UTILISATION
Before launching the application, please install the JAVAFX and SQLITE libraries to ensure that the application works correctly.

### HOME PAGE
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/homepage.png)
When you launch the application, you'll be taken to the home page, which introduces the application and explains how to use it. You can change pages using the buttons on the left and at the bottom of the window.

### USER
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/user.png)
On the user page, you can view a list of all users of the application. You can also search for a specific user.
You can delete a user by selecting it. You can also create, edit and access a user's profile. On a user's profile, you can view their borrowing history.
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/userProfile.png)

### BORROW
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/borrow.png)
On the Borrow page, you can display a list of all the borrows and filter them according to various criteria, such as overdue or finished items. As with users, you can delete a borrow, create or modify one, and view the borrow information. You can also validate or cancel the return of a book. You can also view problems concerning a borrow other than overdue, and add or delete problems.
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/borrowProblem.png)

### BOOK
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/book.png)
On the book page, you can perform a search using several criteria, using the BNF API. When the search is complete, simply go to the list of books found. From this list you can create a BORROW by selecting a user id.
![alt_text](https://github.com/Auxifruit/CY-Books/blob/Livrable/Ressources/bookTable.png)

## TASK LIST
To keep track of the list of tasks, we decided to use Jira, a website that lets you manage a task board containing three columns: To do / In progress / Done. <br />
Link to task list : https://cy-tech-java.atlassian.net/jira/software/projects/KAN/boards/1

## CREATORS
BARRÉ Guillaume -> Auxifruit <br />
BEZAMAT Lucas -> Luczerty <br />
DURÉCU Clément -> shxclem <br />
SENCÉE Raphaël -> gjjoifo <br />
VEROVE Augustin -> Hermeticis


