package maze;

/**
* MultipleExitException is the class which describes an exception that is thrown 
* when there are more than one Tile of the Type exit. 
* It extends the InvalidMazeException. It is an uncheked exception.
*/
public class MultipleExitException extends InvalidMazeException{
	/**
	* The default constuctor without any parameters.
	*/
	public MultipleExitException(){
		super("That maze has multiple exits!");
	}

	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public MultipleExitException(String message){
		super(message);
	}
}