package abstraction;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class UserFile {
    public static final String FILE_PATH = "User.txt";

    /**
     * Method to add an user to the text file where we store all the users
     * @param user the user to add 
     */
	public static void addUserInAFileTXT(User2 user) {
		try {
	        // Créer un objet FileWriter avec le chemin du fichier et en mode append
	        FileWriter fileWriter = new FileWriter(FILE_PATH, true);
	
	        // Créer un objet BufferedWriter en enveloppant le FileWriter
	        BufferedWriter writer = new BufferedWriter(fileWriter);
	
	        // Parcourir les informations de l'utilisateurs
            String userID = String.valueOf(user.getId());
            String userLastname = user.getLastname();
            String userFirstname = user.getFirstname();
            String userEmail = user.getEmail();
            
            // Écrire la ligne de l'utilisateur dans le fichier avec un saut de ligne
            writer.write(userID +";"+userLastname+";"+userFirstname+";"+userEmail+"\n");
	
	        // Fermer le BufferedWriter
	        writer.close();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Method to delete an user from the text file where we store all the users
	 * @param userToDelete the user to delete
	 * @throws IOException if the file isn't found
	 */
	public static void deleteUserInAFileTXT(User2 userToDelete) throws IOException {
		try {
			File inputFile = new File(FILE_PATH);
	        File tempFile = new File("temp" + FILE_PATH);

	        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

	        int userToDeleteID = userToDelete.getId();
	        String currentLine;

	        while ((currentLine = reader.readLine()) != null) {
	        	String[] userInfos = currentLine.split(";");
	        	
				if(Integer.valueOf(userInfos[0]) != userToDeleteID) {
					writer.write(userInfos[0] +";"+userInfos[1]+";"+userInfos[2]+";"+userInfos[3]+"\n");
				}
	        }
	        
	        writer.close();
	        reader.close();
	        inputFile.delete();
	        tempFile.renameTo(inputFile);
	        
		} catch (IOException e) {
			System.err.println("File not found. (deleteUserInAFileTXT)");
		}
    }
	
	/**
	 * Method to modify an user from the text file where we store all the users
	 * @param userToModify the user we want to modify
	 * @param newLastname the user's new lastname
	 * @param newFirstname the user's new firstname
	 * @param newEmail the user's new e-mail
	 * @throws IOException if the file isn't found
	 */
	public static void modifyUserInAFileTXT(User2 userToModify, String newLastname, String newFirstname, String newEmail) throws IOException {
		try {
			File inputFile = new File(FILE_PATH);
	        File tempFile = new File("temp" + FILE_PATH);

	        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

	        int userToModifyID = userToModify.getId();
	        String currentLine;

	        while ((currentLine = reader.readLine()) != null) {
	        	String[] userInfos = currentLine.split(";");
	        	
				if(Integer.valueOf(userInfos[0]) != userToModifyID) {
					writer.write(userInfos[0] +";"+userInfos[1]+";"+userInfos[2]+";"+userInfos[3]+"\n");
				}
				else {
					String userLastname = userInfos[1];
					String userFirstname = userInfos[2];
					String userEmail = userInfos[3];
					
					if(!(newLastname.equals(null) || newLastname.isEmpty())) {
						userLastname = newLastname;
		    		}
		    		if(!(newFirstname.equals(null) || newFirstname.isEmpty())) {
		    			userFirstname = newFirstname;
		    		}
		    		if(!(newEmail.equals(null) || newEmail.isEmpty())) {
		    			userEmail = newEmail;
		    		}
		    		
		    		writer.write(userInfos[0] +";"+userLastname+";"+userFirstname+";"+userEmail+"\n");
				}
	        }
	        
	        writer.close();
	        reader.close();
	        inputFile.delete();
	        tempFile.renameTo(inputFile);
	        
		} catch (IOException e) {
			System.err.println("File not found. (modifyUserInAFileTXT)");
		}
	}

	/**
	 * Method to read all the users in a text file
	 * @throws IOException if the file isn't found
	 */
    public static void readUsersFromAFileTXT() throws IOException {
        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(FILE_PATH));
			String line = reader.readLine();

			while (line != null) {
				String[] userInfos = line.split(";");

				int userID = Integer.valueOf(userInfos[0]);
				String userLastname = userInfos[1];
	            String userFirstname = userInfos[2];
	            String userEmail = userInfos[3];
	            
	            new User2(userID, userLastname, userFirstname, userEmail);
	            
				line = reader.readLine();
			}

			reader.close();
			
			User2.compteurId = User2.getAllUser().get(User2.getAllUser().size() - 1).getId() + 1;
		} catch (IOException e) {
			System.err.println("File not found. (readUsersFromAFileTXT)");
		}
    }

    public static void main(String[] args) throws IOException {
    	
    }
}