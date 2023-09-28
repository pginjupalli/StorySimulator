/**This is a tree of Nodes which represents the progression of the game 
 * depending on which options are taken
 * 
 * @author Pooja Ginjupalli
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.zip.DataFormatException;

public class StoryTree {
	private StoryTreeNode root; //The root of the tree
	private StoryTreeNode cursor; //The currently selected Node of the tree
	private GameState state; //The state of the game at any time
	private StoryTreeNode[] nodes; //An array of the nodes in the tree in preorder
	private int counter;
	
	/**Creates an instance of an empty StoryTree with only the root node
	 * 
	 * @Postcondition:
	 * 	The root node has been initialized and the cursor points to the root
	 */
	public StoryTree() {
		root = new StoryTreeNode();
		root.setPosition("root");
		root.setOption("root");
		root.setMessage("Hello, welcome to Zork!");
		
		cursor = root;
		counter = 0;
	}
	
	/**Reads a textfile for a StoryTree, crafts it, and returns it
	 * 
	 * @param filename
	 * 	The textfile name to be read
	 * 
	 * @Precondition:
	 * 	filename is a nonnull, nonempty String that points to a textfile
	 * 
	 * @return
	 * 	A StoryTree made from the textfile
	 * 
	 * @exception FileNotFoundException 
	 * 	Indicates the passed file is not found
	 * 
	 * @exception IllegalArgumentException
	 * 	filename is empty or null
	 * 
	 * @exception DataFormatException()
	 * 	Indicates file data is inconsistant with expected data format
	 * @throws TreeFullException 
	 */
	public static StoryTree readTree(String filename) throws FileNotFoundException, DataFormatException, TreeFullException {
		if (filename == null || filename.isBlank())
			throw new IllegalArgumentException();
		if (!hasValidFormat(filename))
			throw new DataFormatException();
		Scanner file = new Scanner(new File(filename));
		if (!file.hasNextLine())
			throw new DataFormatException();
		
		StoryTree tree = new StoryTree();
		StoryTreeNode newNode = new StoryTreeNode();
		StoryTreeNode pointer = new StoryTreeNode();
		String data[];
		int index = 2;
		int childNumber;
		int lineCounter = 1;
		
		data = file.nextLine().split(" \\| ");
		tree.addChild(data[1], data[2]);
		
		while (file.hasNextLine()) {
			newNode = new StoryTreeNode();
			data = file.nextLine().split(" \\| ");
			newNode.setPosition(data[0]);
			newNode.setOption(data[1]);
			newNode.setMessage(data[2]);
			
			pointer = tree.getStoryRoot();
			index = 2;
			while (index < data[0].length() - 2) {
				childNumber = data[0].charAt(index) - '0';
				if (childNumber == 1)
					pointer = pointer.getLeftChild();
				if (childNumber == 2)
					pointer = pointer.getMiddleChild();
				if (childNumber == 3)
					pointer = pointer.getRightChild();
				index += 2;
			}
			
			childNumber = data[0].charAt(index) - '0';
			if (childNumber == 1)
				pointer.setLeftChild(newNode);
			if (childNumber == 2)
				pointer.setMiddleChild(newNode);
			if (childNumber == 3)
				pointer.setRightChild(newNode);
			lineCounter++;
		}
		
		file.close();
		tree.resetCursor();
		return tree;
	}
	
	/**Saves a tree to the specified textfile in the proper format
	 * 
	 * @param filename
	 * 	The file to save the tree to
	 * 
	 * @param tree
	 * 	The reference to the tree to be saved to the file
	 * 
	 * @Precondition:
	 * 	tree is nonull
	 * 	filename is nonnull and nonempty
	 * 
	 * @exception IllegalArgumentException
	 * 	filename is empty or null
	 * 	tree is null
	 * 
	 * @exception FileNotFoundException
	 * 	Indicates filename doesn't point to an accesible file
	 */
	public static void saveTree(String filename, StoryTree tree) throws FileNotFoundException {
		tree.setNodeArraySize();
		tree.getNodes(tree.getStoryRoot());
		StoryTreeNode[] nodes = tree.getNodesArray();
		
		if (nodes.length == 0) {
			PrintWriter file = new PrintWriter(filename);
			file.close();
			return;
		}
		PrintWriter file = new PrintWriter(filename);
		for (int x = 0; x < nodes.length - 1; x++) 
			file.write(nodes[x].getPosition() + " | " + nodes[x].getOption()
					+ " | " + nodes[x].getMessage() + "\n");
		file.write(nodes[nodes.length - 1].getPosition() + " | " + nodes[nodes.length - 1].getOption()
				+ " | " + nodes[nodes.length - 1].getMessage());
		file.close();
	}
	
	/**Returns the game state of the StoryTree
	 * 
	 * @Precondition:
	 * 	state is not null
	 * 
	 * @return
	 * 	The current state of the game
	 */
	public GameState getGameState() {
		return state;
	}
	
	/**Returns the position of the cursor within the tree
	 * 
	 * @return
	 * 	The position of the cursor in the tree
	 */
	public String getCursorPosition() {
		return cursor.getPosition();
	}
	
	/**Returns the option of the cursor
	 * 
	 * @return
	 * 	The option String of the cursor node
	 */
	public String getCursorOption() {
		return cursor.getOption();
	}
	
	/**Returns the message of the cursor
	 * 
	 * @return
	 * 	The message of the cursor which would be shown if it's chosen
	 */
	public String getCursorMessage() {
		return cursor.getMessage();
	}
	
	/**Returns an array of String pairs for each child of the cursor
	 * 
	 * @return
	 * 	A 2D String array with the positions and options of each child of the cursor
	 */
	public String[][] getOptions() {
		String[][] options = new String[cursor.getNumChildren()][2];
		int index = 0;
		
		if (cursor.getLeftChild() != null) {
			options[index][0] = cursor.getLeftChild().getPosition();
			options[index][1] = cursor.getLeftChild().getOption();
			index++;
		}
		if (cursor.getMiddleChild() != null) {
			options[index][0] = cursor.getMiddleChild().getPosition();
			options[index][1] = cursor.getMiddleChild().getOption();
			index++;
		}
		if (cursor.getRightChild() != null) {
			options[index][0] = cursor.getRightChild().getPosition();
			options[index][1] = cursor.getRightChild().getOption();
			index++;
		}
		
		return options;
	}
	
	/**Changes the cursor's message
	 * 
	 * @param message
	 * 	The new message to be set as the cursor's message
	 */
	public void setCursorMessage(String message) {
		cursor.setMessage(message);
	}
	
	/**Changes the cursor's option
	 * 
	 * @param option
	 * 	The new option to be ste as the cursor's option
	 */
	public void setCursorOption(String option) {
		cursor.setOption(option);
	}
	
	/**Resets the cursor so it points to root
	 * 
	 * @Postcondition:
	 * 	cursor references the root node
	 */
	public void resetCursor() {
		if (root.getNumChildren() == 0)
			return;
		cursor = root.getLeftChild();
	}
	
	/**Returns whether or not cursor is referencing a leaf node
	 * 
	 * @return
	 * 	True if cursor references a leaf node, false otherwise
	 */
	public boolean cursorIsLeaf() {
		return (cursor.isLeaf());
	}
	
	/**Sets the cursor to the original cursor's child as indicated by the parameter
	 * 
	 * @param position
	 * 	The position of the Node to become the cursor
	 * 
	 * @Precondition:
	 * 	position is not null or empty/white space
	 * 	The child with the indicated position is a direct child of the cursor
	 * 
	 * @Postcondition:
	 * 	cursor references the indicated child
	 * 
	 * @exception IllegalArgumentException
	 * 	Indicates position is empty or null
	 * 
	 * @exception NodeNotPresentException
	 * 	Indicates the Node is not a valid child of the cursor
	 */
	public void selectChild(String position) throws NodeNotPresentException {
		if (position == null || position.isBlank())
			throw new IllegalArgumentException();
		if (!isValidChildOfCursor(position))
			throw new NodeNotPresentException();
		
		if (position.charAt(position.length() - 1) == '1')
			cursor = cursor.getLeftChild();
		if (position.charAt(position.length() - 1) == '2')
			cursor = cursor.getMiddleChild();
		if (position.charAt(position.length() - 1) == '3')
			cursor = cursor.getRightChild();
	}
	
	/**Sets the cursor to its parent. If the cursor is at the root, no change in made
	 * 
	 * @Postcondition:
	 * 	cursor now references its parent
	 */
	public void returnToParent() {
		if (cursor == root || cursor == root.getLeftChild())
			return;
			
		StoryTreeNode parent = root;
		String parentPosition = cursor.getPosition().substring(0, cursor.getPosition().length() - 2);
		int index = 0;
		int childNumber = parentPosition.charAt(index) - '0';
		
		while (index < parentPosition.length()) {
			childNumber = parentPosition.charAt(index) - '0';
			if (childNumber == 1)
				parent = parent.getLeftChild();
			else if (childNumber == 2)
				parent = parent.getMiddleChild();
			else if (childNumber == 3)
				parent = parent.getRightChild();
			index += 2;
		}
		
		cursor = parent;
	}
	
	/**For the given cursor, returns the chance of winning
	 * 
	 * @return
	 * 	The chance of winning as dictated by the number of winning leaves 
	 * 	divided by the number of total leaves of the subtree of the cursor
	 */
	public double winProbability() {
		return ((double)getNumOfWinningNodes(cursor) / (double)getNumOfLeaves(cursor)) * 100.0;
	}
	
	/**Adds a new child to the current cursor with the given option and message
	 * 
	 * @param option
	 * 	The option of the new child
	 * 
	 * @param message
	 * 	The message of the new child
	 * 
	 * @Precondition:
	 * 	cursor does not have 3 children
	 * 	option and message are not null
	 * 
	 * @Postcondition:
	 * 	cursor has a new child with the indicated option and message
	 * 
	 * @exception IllegalArgumentException
	 * 	Indicates option or message is null
	 * 
	 * @exception TreeFullException
	 * 	Indicates cursor has max children
	 */
	public void addChild(String option, String message) throws TreeFullException {
		if (option == null || message == null)
			throw new IllegalArgumentException();
		if (cursor.getNumChildren() == 3)
			throw new TreeFullException();
		
		String position = cursor.getPosition() + "-" + (cursor.getNumChildren() + 1);
		StoryTreeNode newNode = new StoryTreeNode();
		newNode.setMessage(message);
		newNode.setOption(option);
		if (cursor == root) {
			newNode.setPosition("1");
			root.setLeftChild(newNode);
			cursor = root.getLeftChild();
		} else {
			newNode.setPosition(position);
			if (position.charAt(position.length() - 1) == '1')
				cursor.setLeftChild(newNode);
			if (position.charAt(position.length() - 1) == '2')
				cursor.setMiddleChild(newNode);
			if (position.charAt(position.length() - 1) == '3')
				cursor.setRightChild(newNode);
		}
	}
	
	/**Removes the immediate child the cursor as indicated. Shifts remaining children and positions to they're all left-aligned
	 * 
	 * @param position
	 * 	The position of the child to remove
	 * 
	 * @Precondition:
	 * 	The child with the indicated position is a direct child of the cursor
	 * 
	 * @Postcondition:
	 * 	The indicated child and its subtree have been removed
	 *  and positions of affected nodes have been shifted accordingly
	 *  
	 * @return
	 * 	A reference to the removed Node and its subtree
	 * 
	 * @exception NodeNotFoundException
	 * 	Indicates the Node references is not an appropriate child of cursor
	 */
	public StoryTreeNode removeChild(String position) throws NodeNotPresentException {
		int childNumber = position.charAt(position.length() - 1) - '0';
		if (childNumber > cursor.getNumChildren() || !isValidChildOfCursor(position))
			throw new NodeNotPresentException();
		
		StoryTreeNode removedNode = new StoryTreeNode();
		if (childNumber == 1) {
			removedNode = cursor.getLeftChild();
			cursor.setLeftChild(cursor.getMiddleChild());
			cursor.setMiddleChild(cursor.getRightChild());
			cursor.setRightChild(null);
		} else if (childNumber == 2) {
			removedNode = cursor.getMiddleChild();
			cursor.setMiddleChild(cursor.getRightChild());
			cursor.setRightChild(null);
		} else if (childNumber == 3) {
			removedNode = cursor.getRightChild();
			cursor.setRightChild(null);
		}
		
		return removedNode;
	}
	
	/**Returns whether or not the passed text file has a valid format for a StoryTree
	 * 
	 * @param filename
	 * 	The file to check if valid
	 * 
	 * @Precondition:
	 * 	filename is nonnull and nonempty
	 * 
	 * @return
	 * 	True if the file has a valid "position | option | message" format
	 * 	False otherwise
	 * @throws FileNotFoundException 
	 */
	public static boolean hasValidFormat(String filename) throws FileNotFoundException {
		if (filename == null || filename.isEmpty())
			throw new IllegalArgumentException();
		
		Scanner file = new Scanner (new File(filename));
		while (file.hasNextLine()) {
			if (countOccurances('|', file.nextLine()) != 2)
				return false;
		}
		return true;
	}
	
	/**Counts and returns the number of the occurances of a character in a string
	 * 
	 * @param target
	 * 	The character is count
	 * 
	 * @param phrase
	 * 	The String to count in
	 * 
	 * @return
	 * 	The number of times target is found in phrase
	 */
	public static int countOccurances(char target, String phrase) {
		int counter = 0;
		for (int x = 0; x < phrase.length() - 1; x++) {
			if (phrase.charAt(x) == target)
				counter++;
		}
		return counter;
	}
	
	/**Returns the number of nodes in the tree starting from the passed Node
	 * 
	 * @param root
	 * 	The root of the tree to count the Nodes of
	 * 
	 * @return
	 * 	The number of nodes in the tree indicated by root
	 */
	public int getNumOfNodes(StoryTreeNode root) {
		if (root == null)
			return 0;
		return 1 + getNumOfNodes(root.getLeftChild()) + getNumOfNodes(root.getMiddleChild()) + getNumOfNodes(root.getRightChild());
	}
	
	/**Returns the total number of leaves within the tree represented indicated
	 * 
	 * @param root
	 * 	The root of the tree to count leaves from
	 * 
	 * @return
	 * 	The number of leaves within the indicated tree
	 */
	public int getNumOfLeaves(StoryTreeNode root) {
		if (root == null)
			return 0;
		if (root.isLeaf())
			return 1;
		else
			return getNumOfLeaves(root.getLeftChild()) + getNumOfLeaves(root.getMiddleChild()) + getNumOfLeaves(root.getRightChild());
	}
	
	/**Returns the number of winning nodes wihtin the indicated tree
	 * 
	 * @param root
	 * 	The root of the tree to count from
	 * 
	 * @return
	 * 	The number of winning nodes in the tree indicated by root
	 */
	public int getNumOfWinningNodes(StoryTreeNode root) {
		if (root == null)
			return 0;
		if (root.isWinningNode())
			return 1;
		else
			return getNumOfWinningNodes(root.getLeftChild()) + getNumOfWinningNodes(root.getMiddleChild()) + getNumOfWinningNodes(root.getRightChild());
	}
	
	/**Returns the beginning Node of the story that has a position of 1
	 * 
	 * @return
	 * 	The leftChild of the root
	 */
	public StoryTreeNode getStoryRoot() {
		return root.getLeftChild();
	}
	
	/**Returns whether or not the indicated position references an existing child of the cursor
	 * 
	 * @param position
	 * 	The position of the supposed child of the cursor
	 * 
	 * @return
	 * 	Whether or not the position's Node exists as a child of cursor
	 */
	public boolean isValidChildOfCursor(String position) {
		if (cursor == root || position.equals("1"))
			return true;
		String cursorChildOption[] = new String[cursor.getNumChildren()];
		for (int x = 0; x < cursorChildOption.length; x++) {
			cursorChildOption[x] = cursor.getPosition() + "-" + (x+1);
		}
		
		for (String accuratePosition : cursorChildOption) {
			if (position.equals(accuratePosition))
				return true;
		}
		return false;
	}
	
	/**Inputs the nodes in the tree indicated into an array in preorder
	 * 
	 * @param node
	 * 	The root of the tree to input into the array
	 */
	public void getNodes(StoryTreeNode node) {
		if (node == null)
			return;
		
		nodes[counter] = node;
		counter++;
		getNodes(node.getLeftChild());
		getNodes(node.getMiddleChild());
		getNodes(node.getRightChild());
	}
	
	/**Returns the nodes array
	 * 
	 * @return
	 * 	nodes array consisting of the nodes present in a tree in preorder
	 */
	public StoryTreeNode[] getNodesArray() {
		return nodes;
	}
	
	/**Sets the size of the nodes array depending on how many nodes are in the tree
	 */
	public void setNodeArraySize() {
		nodes = new StoryTreeNode[getNumOfNodes(getStoryRoot())];
		counter = 0;
	}
	
	/**Returns the number of children of the cursor
	 * 
	 * @return
	 * 	
	 */
	public int getCursorNumChildren() {
		return cursor.getNumChildren();
	}
}
