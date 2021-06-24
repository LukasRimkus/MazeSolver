package maze;

/**
* RaggedMazeException is the class which describes an exception that is thrown 
* when there the legths of rows and columns is not consistent. 
* It extends the InvalidMazeException. It is an uncheked exception.
*/
public class RaggedMazeException extends InvalidMazeException {
	/**
	* The default constuctor without any parameters.
	*/
	public RaggedMazeException(){
		super("Ragged maze was provided");
	}

	/**
	* Constructor which takes a specific message as its input.
	*
	* @param message to be printed out then the exception occurs.
	*/
	public RaggedMazeException(String message){
		super(message);
	}
}