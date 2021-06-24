package maze;

/**
* MultipleEntranceException is the class which describes an exception that is thrown 
* when there are more than one Tile of the Type entrance. 
* It extends the InvalidMazeException. It is an uncheked exception.
*/
public class MultipleEntranceException extends InvalidMazeException {
	/**
	* The default constuctor without any parameters.
	*/
	public MultipleEntranceException(){
		super("That maze has multiple entrances!");
	}
	
	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public MultipleEntranceException(String message){
		super(message);
	}
}