package maze;

/**
* NoExitException is the class which describes an exception that is thrown 
* when there is none Tile of the Type exit. 
* It extends the InvalidMazeException. It is an uncheked exception.
*/
public class NoExitException extends InvalidMazeException {
	/**
	* The default constuctor without any parameters.
	*/
	public NoExitException(){
		super("That maze has no exit!");
	}
	
	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public NoExitException(String message){
		super(message);
	}
}