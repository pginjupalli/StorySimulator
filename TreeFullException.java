/**This class represents an Exception which is thrown when the user tries
 *  to add a child to a node which already has the max children
 * 
 * @author Pooja Ginjupalli
 */

public class TreeFullException extends Exception{
	
	/**Default constructor of an Exception that simply throws an exception
	 */
	public TreeFullException() {
		super();
	}
	
	/**Default constuctor which throws an exception which also 
	 * displays a message
	 * 
	 * @param message
	 * 	The message to be displayed when exception is thrown
	 */
	public TreeFullException(String message) {
		super(message);
	}
}
