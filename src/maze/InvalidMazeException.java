package maze;

import java.lang.RuntimeException;

/**
* InvalidMazeException is the class which describes an exception that is thrown 
* when there the maze is not syntactically valid (other characters then default ones). 
* It extends the RuntimeException, so it is an unchecked exception.
*/
public class InvalidMazeException extends RuntimeException {

	/**
	* The default constuctor without any parameters.
	*/
	public InvalidMazeException(){
		super("Invalid maze was provided");
	}

	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public InvalidMazeException(String message){
		super(message);
	}
}