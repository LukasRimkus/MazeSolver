package maze;

/**
* NoEntranceException is the class which describes an exception that is thrown 
* when there is none Tile of the Type entrance. 
* It extends the InvalidMazeException. It is an uncheked exception.
*/
public class NoEntranceException extends InvalidMazeException{
	/**
	* The default constuctor without any parameters.
	*/
	public NoEntranceException(){
		super("That maze has no entrance!");
	}
	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public NoEntranceException(String message){
		super(message);
	}
}