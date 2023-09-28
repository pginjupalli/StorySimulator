/**This class reads and saves a file containing a StoryTree, allowing a user to play, edit, and save a Story game
 * 
 * @author Pooja Ginjupalli
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class Zork {
	private static Scanner kb = new Scanner(System.in);
	
	public static void main(String args[]) throws FileNotFoundException, DataFormatException, NodeNotPresentException, TreeFullException {
		System.out.println("Hello and Welcome to Zork!\n");
		System.out.print("Please enter a file name: ");
		String filename = kb.nextLine();
		System.out.println("\nLoading game from file...");
		
		StoryTree tree = new StoryTree();

		try {
			tree = StoryTree.readTree(filename);
			System.out.println("\nFile loaded!\n");
		} catch (FileNotFoundException e) {
			System.out.println("Invalid: File not found.");
			tree = new StoryTree();
		} catch (DataFormatException e) {
			System.out.println("Invalid: Format of file is invalid.");
			tree = new StoryTree();
		}
		
		String choice;
		
		while (true) {
			System.out.print("Would you like to edit (E), play (P) or quit (Q)? ");
			choice = kb.nextLine().trim();
			choice = choice.toUpperCase();
			System.out.println();

			switch (choice) {
			case "E":
				editTree(tree);
				break;
				
			case "P":
				playTree(tree);
				break;
				
			case "Q":
				StoryTree.saveTree(filename, tree);
				System.out.println("Game being saved to " + filename + "..." +
				"\n\nSave Successful!\n\nProgram terminating normally.");
				System.exit(0);
				
			default:
				System.out.println("Invalid.");
				break;
			}
		}
		
	}
	
	/**Allows the user to play a StoryTree
	 * 
	 * @param tree
	 * 	The tree that is to be played
	 * @throws NodeNotPresentException 
	 */
	public static void playTree(StoryTree tree) throws NodeNotPresentException {
		tree.resetCursor();
		String choice;
		String[][] options;
		
		System.out.println(tree.getCursorOption());
		
		while (!tree.cursorIsLeaf()) {
			options = tree.getOptions();
			
			System.out.println(tree.getCursorMessage());
			for (int x = 1; x <= options.length; x++) 
				System.out.println(x + ") " + options[x - 1][1]);
			
			choice = "c";
			while (choice.equalsIgnoreCase("c")) {
				System.out.print("Please make a choice: ");
				choice = kb.nextLine().trim();
				System.out.println();
				
				if (choice.equalsIgnoreCase("c")) {
					System.out.println("Probability of a win at this point: " + String.format("%.1f", tree.winProbability()) + "%\n");
				} else if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
					System.out.println("Invalid.\n");
					choice = "c";
				} else if (Integer.parseInt(choice) > options.length) {
					System.out.println("Invalid.\n");
					choice = "c";
				} else {
					tree.selectChild(tree.getCursorPosition() + "-" + choice);
				}
			}
			
		}
		
		System.out.println(tree.getCursorMessage() + "\n");
		System.out.println("Thanks for playing.");
	}
	
	/**Provides an interface which allows the user to edit a given tree
	 * 
	 * @param tree
	 * 	The tree the user wishes to edit
	 */
	public static void editTree(StoryTree tree) {
		tree.resetCursor();
		String choice = "";
		String[][] options;
		String children;
		String miniChoice = "";
		String miniChoice2 = "";
		while (!choice.equalsIgnoreCase("Q")) {
			System.out.println("\nZork Editor:\r\n"
					+ "    V: View the cursor's position, option and message.\r\n"
					+ "    S: Select a child of this cursor (options are 1, 2, and 3).\r\n"
					+ "    O: Set the option of the cursor.\r\n"
					+ "    M: Set the message of the cursor.\r\n"
					+ "    A: Add a child StoryNode to the cursor.\r\n"
					+ "    D: Delete one of the cursor's children and all its descendants.\r\n"
					+ "    R: Move the cursor to the root of the tree.\r\n"
					+ "    Q: Quit editing and return to main menu.");
			System.out.print("Please select an option: ");
			choice = kb.nextLine().trim();
			choice = choice.toUpperCase();
			children = "";
			miniChoice = "";
			miniChoice2 = "";
			
			switch (choice) {
			case "V":
				if (tree.getStoryRoot() == null) {
					System.out.println("Position: root\r\n"
							+ "Option: root\r\n"
							+ "Message: Hello, and welcome to Zork!");
				} else {
					System.out.println("Position: " + tree.getCursorPosition());
					System.out.println("Option: " + tree.getCursorOption());
					System.out.println("Message: " + tree.getCursorMessage());
				}
				break;
				
			case "S":
				if (tree.cursorIsLeaf()) {
					System.out.println("Invalid: Cursor has no children.");
					break;
				}
				options = tree.getOptions();
				children += "[";
				for (int x = 1; x <= options.length; x++) 
					children = children + x + ",";
				children = children.substring(0, children.length() - 1);
				children += "]";
				
				System.out.print("Please select a child: " + children + " ");
				miniChoice = kb.nextLine().trim();
				try {
					tree.selectChild(tree.getCursorPosition() + "-" + miniChoice);
				} catch (NodeNotPresentException e){
					System.out.println("Error. No child " + miniChoice + " for the current node.");
					break;
				}
				break;
				
			case "O":
				System.out.print("Please enter a new option: ");
				miniChoice = kb.nextLine().trim();
				
				try {
					tree.setCursorOption(miniChoice);
					System.out.println("Option set.");
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid.");
					break;
				}
				break;
				
			case "M":
				System.out.print("Please enter a new message: ");
				miniChoice = kb.nextLine().trim();
				
				try {
					tree.setCursorMessage(miniChoice);
					System.out.println("Message set.");
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid.");
					break;
				}
				break;
				
			case "A":
				if (tree.getCursorNumChildren() == 3) {
					System.out.println("Error");
					break;
				}
				System.out.print("Enter an option: ");
				miniChoice = kb.nextLine().trim();
				System.out.print("Enter a message: ");
				miniChoice2 = kb.nextLine().trim();
				
				try {
					tree.addChild(miniChoice, miniChoice2);
					System.out.println("Child added.");
				} catch (TreeFullException e) {
					System.out.println("Error");
					break;
				}
				break;
				
			case "D":
				if (tree.cursorIsLeaf()) {
					System.out.println("Invalid: Cursor has no children.");
					break;
				}
				options = tree.getOptions();
				children += "[";
				for (int x = 1; x <= options.length; x++) 
					children = children + x + ",";
				children = children.substring(0, children.length() - 1);
				children += "]";
				
				System.out.print("Please select a child: " + children + " ");
				miniChoice = kb.nextLine().trim();
				
				try {
					tree.removeChild(tree.getCursorPosition() + "-" + miniChoice);
					System.out.println("Subtree deleted.");
				} catch (NodeNotPresentException e) {
					System.out.println("Error. No child " + miniChoice + " for the current node.");
					break;
				}
				break;
				
			case "R":
				tree.resetCursor();
				System.out.println("Cursor moved to root.");
				
			case "Q":
				break;
				
			default:
				System.out.println("Invalid.");
				break;
			}
		}
	}
}
