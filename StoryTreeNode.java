/**This is a Node in a tree which represents a segment of the story. 
 * It may contain references to other Nodes and has 3 String variables 
 * for position, choice, and message.
 * 
 * @author Pooja Ginjupalli
 */

public class StoryTreeNode {
	private static final String WIN_MESSAGE = "YOU WIN"; //Special sequence which shows game was won
	private static final String LOSE_MESSAGE = "YOU LOSE"; //Special sequence which shows game was lost
	
	private String position; //Indicates the position of the Node within the tree
	private String option; //To be displayed when presented with this Node as a chocie
	private String message; //To be displayed when this Node has been selected
	
	private StoryTreeNode leftChild; //Child 1 of the Node
	private StoryTreeNode middleChild; //Child 2 of the Node
	private StoryTreeNode rightChild; //Child 3 of the Node
	
	/**Default constructor that makes an instance of a Node
	 * 
	 */
	public StoryTreeNode() {
		leftChild = null;
		middleChild = null;
		rightChild = null;
	}
	
	/**Determines if the Node is the dummy root for a StoryTree
	 * 
	 * @return
	 * 	True if this Node is a dummy root, false otherwise
	 */
	public boolean isRootNode() {
		return (position.equals("root") && option.equals("root") && message.equals("Hello, welcome to Zork!"));
	}
	
	/**Determines if the Node has any children
	 * 
	 * @Precondition:
	 * 	This Node has been initialized
	 * 
	 * @Postcondition:
	 * 	The tree remains unchanged	
	 * 
	 * @return
	 * 	True is there are any children,
	 * 	False is there are no chldren of this Node
	 */
	public boolean isLeaf() {
		return (leftChild == null && middleChild == null && rightChild == null);
	}
	
	/**Determines if the Node is a winning node, 
	 * meaning getting to this Node indicates victory
	 * 
	 * @Precondition:
	 * 	This Node has been initialized
	 * 
	 * @Postcondition:
	 * 	The tree remains unchanged	
	 * 
	 * @return
	 * 	True if the Node is a leaf and contains the WIN_MESSAGE
	 * 	False otherwise
	 */
	public boolean isWinningNode() {
		return (this.isLeaf() && message.contains(WIN_MESSAGE));
	}
	
	/**Determines if the Node is a losing node, 
	 * meaning getting to this Node indicates loss of game
	 * 
	 * @Precondition:
	 * 	This Node has been initialized
	 * 
	 * @Postcondition:
	 * 	The tree remains unchanged	
	 * 
	 * @return
	 * 	True if the Node is a leaf and does NOT contains the WIN_MESSAGE
	 * 	False otherwise
	 */
	public boolean isLosingNode() {
		return (this.isLeaf() && !message.contains(WIN_MESSAGE));
	}
	
	/**Returns the position of the Node
	 * 
	 * @return
	 * 	Returns where the Node is on the tree
	 */
	public String getPosition() {
		return position;
	}
	
	/**Returns what is displayed when picking this Node
	 * 
	 * @return
	 * 	Returns what will be shown to players when choosing options
	 */
	public String getOption() {
		return option;
	}
	
	/**Returns what is to be displayed when this Node is selected
	 * 
	 * @return
	 * 	Returns the message to be displayed to progress the game
	 */
	public String getMessage() {
		return message;
	}
	
	/**Returns the left-most child of the Node
	 * 
	 * @return
	 * 	Returns the left-most child of this Node
	 * 	Returns false if Node does not have any children
	 */
	public StoryTreeNode getLeftChild() {
		return leftChild;
	}
	
	/**Returns the middle child of the Node
	 * 
	 * @return
	 * 	Returns the middle child of this Node
	 * 	Returns false if Node has 1 or less children
	 */
	public StoryTreeNode getMiddleChild() {
		return middleChild;
	}
	
	/**Returns the right-most child of the Node
	 * 
	 * @return
	 * 	Returns the right-most child of this Node
	 * 	Returns false if Node has 2 or less children
	 */
	public StoryTreeNode getRightChild() {
		return rightChild;
	}
	
	/**Changes the position of the Node to the specified sequence
	 * 
	 * @param newPosition
	 * 	The new sequence to be assigned to position
	 * 
	 * @Precondition:
	 * 	newPosition is not null
	 * 
	 * @exception IllegalArgumentException
	 * 	Indicates newPosition is null
	 */
	public void setPosition(String newPosition) {
		if (newPosition == null)
			throw new IllegalArgumentException();
		position = newPosition;
	}
	
	/**Changes the option of the Node to the specified sequence
	 * 
	 * @param newOption
	 * 	The new sequence to be displayed when this Node is presented as an option
	 * 
	 * @Precondition:
	 * 	newOption is not null
	 * 
	 * @exception IllegalArgumentException
	 * 	Indicates newOption is null
	 */
	public void setOption(String newOption) {
		if (newOption == null)
			throw new IllegalArgumentException();
		option = newOption;
	}
	
	/**Changes the message of the Node to the specified sequence
	 * 
	 * @param newMessage
	 * 	The new sequence to be displayed when this Node is selected
	 * 
	 * @Precondition:
	 * 	newMessage is not null
	 * 
	 * @exception IllegalArgumentException
	 * 	Indicates newMessage is null
	 */
	public void setMessage(String newMessage) {
		if (newMessage == null) 
			throw new IllegalArgumentException();
		message = newMessage;
	}
	
	/**Sets the specified Node as the left child of this Node
	 * 
	 * @param newChild
	 * 	The Node to become the left child of this Node
	 */
	public void setLeftChild(StoryTreeNode newChild) {
		leftChild = newChild;
		if (leftChild != null && !isRootNode()) {
			leftChild.setPosition(position + "-1");
			
			if (!leftChild.isLeaf()) {
				updateSubtreePosition(leftChild, leftChild.getPosition());
			}
		}
	}
	
	/**Sets the specified Node as the middle child of this Node
	 * 
	 * @param newChild
	 * 	The Node to become the middle child of this Node
	 */
	public void setMiddleChild(StoryTreeNode newChild) {
		middleChild = newChild;
		if (middleChild != null && !isRootNode()) {
			middleChild.setPosition(position + "-2");
			
			if (!middleChild.isLeaf()) {
				updateSubtreePosition(middleChild, middleChild.getPosition());
			}
		}
	}
	
	/**Sets the specified Node as the right child of this Node
	 * 
	 * @param newChild
	 * 	The Node to become the right child of this Node
	 */
	public void setRightChild(StoryTreeNode newChild) {
		rightChild = newChild;
		if (rightChild != null && !isRootNode()) {
			rightChild.setPosition(position + "-3");
			
			if (!rightChild.isLeaf()) {
				updateSubtreePosition(rightChild, rightChild.getPosition());
			}
		}
	}
	
	/**Returns the number of children the Node currently has
	 * 
	 * @return
	 * 	The amount of the children that are not null for this Node
	 */
	public int getNumChildren() {
		int counter = 0;
		if (leftChild != null)
			counter++;
		if (middleChild != null)
			counter++;
		if (rightChild != null)
			counter++;
		return counter;
	}
	
	/**Changes the position of each descendent of the given node to as specified
	 * 
	 * @param root
	 * 	The Node whose descendants' positions (and itself's) will be changed
	 * 
	 * @param rootPosition
	 * 	The correct position to be updated with
	 * 
	 * @Postcondition:
	 * 	The position of the root and its descendants will be correctly updated
	 * 
	 */
	public void updateSubtreePosition(StoryTreeNode root, String initialPosition) {
		if (root == null)
			return;
		int startingIndex = initialPosition.length();
		String appendPosition = root.getPosition().substring(startingIndex);
		root.setPosition(initialPosition + appendPosition);
		
		updateSubtreePosition(root.getLeftChild(), initialPosition);
		updateSubtreePosition(root.getMiddleChild(), initialPosition);
		updateSubtreePosition(root.getRightChild(), initialPosition);
	}
}
