package maze.routing;

import java.lang.RuntimeException;

/**
* NoRouteFoundException is the class which describes an exception which is thrown when there is no 
* solution to the maze (no access to the exit from the entrance Tile). 
* It extends the RuntimeException, so it is uncheked exception.
*/
public class NoRouteFoundException extends RuntimeException {
	/**
	* The default constuctor without any parameters.
	*/
	public NoRouteFoundException(){
		super("No Route was Found");
	}
	
	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public NoRouteFoundException(String message){
		super(message);
	}
}