/**This class represents an Exception which is thrown when a certain Node indicated
 *  is not present when it should be
 * 
 * @author Pooja Ginjupalli
 */

public class NodeNotPresentException extends Exception{
	
	/**Default constructor of an Exception that simply throws an exception
	 */
	public NodeNotPresentException() {
		super();
	}
	
	/**Default constuctor which throws an exception which also 
	 * displays a message
	 * 
	 * @param message
	 * 	The message to be displayed when exception is thrown
	 */
	public NodeNotPresentException(String message) {
		super(message);
	}
}
