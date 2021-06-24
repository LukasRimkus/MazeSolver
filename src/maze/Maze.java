package maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.List;
import java.lang.IndexOutOfBoundsException;
import maze.InvalidMazeException;

import java.io.Serializable;

/**
* Maze is the class which describes a maze object which consists of 
* a number of Tile instances (composition relationship) that determine the 
* type of every Tile of the Maze.
* Also, this class implements the Serializable interface.
*/
public class Maze implements Serializable{

	/**
	* Coordinate is the inner class which describes the coordinate of 
	* a single Tile instance from the Maze instance.
	*/
	public class Coordinate implements Serializable{
		private int x;
		private int y;

		/**
		* Coordinate constructor which asigns the coordinates the instance
		* @param x assigns x coordinaete to the object
		* @param y assigns y coordinaete to the object
		*/
		public Coordinate(int x, int y){
			this.x = x;
			this.y = y;
		}

		/**
		* getX is the method which return the x coordinate
		* @return the coordinate x of the instance
		*/
		public int getX(){
			return x;
		}

		/**
		* getY is the method which return the y coordinate
		* @return the coordinate y of the instance
		*/
		public int getY(){
			return y;
		}

		/**
		* toString is the overriden method which provides String representation
		* of a specific instance
		* @return String representation of the Coordinate instance
		*/
		@Override
		public String toString(){
			return "(" + this.getX() + ", " + this.getY() + ")";
		}
	}

	/**
	* Direction is the inner enum which represents all directions (North, South, East, West)
	*/
	public enum Direction{
		NORTH, 
		SOUTH, 
		EAST, 
		WEST;
	}

	private Tile entrance;
	private Tile exit;
	private List<List<Tile>> tiles;

	private Maze(){
		tiles = new ArrayList<List<Tile>>();
	}

	/**
	* The method fromTxt is a static method which can be called to create a 
	* maze from a text file using the given path to it.
	*
	* @param filePath Provides the path to the txt file to be read from.
	* @return the Maze instance after building a maze from a text file if 
	* none exceptions were thrown.
	*
	* @throws RaggedMazeException if the number of different rows/columns differ.
	* @throws InvalidMazeException if the maze is empty or an invalid character 
	* appeared in the maze text file.
	* @throws NoEntranceException if an entrance Tile was not provided.
	* @throws NoExitException if an exit Tile was not provided.
	*/
	public static Maze fromTxt(String filePath){
		Maze maze = new Maze();

	 	try (
            BufferedReader bufferedReader = new BufferedReader(
                new FileReader(filePath)
            )
        ) {
		 	String line = bufferedReader.readLine();

		 	// required to check if the maze is not ragged
		 	int length = line.length();

		 	while (line != null){
		 		maze.tiles.add(new ArrayList<Tile>());

        		for (int i = 0; i < line.length(); i++) { 
        			if (length != line.length()){ 
        				throw new RaggedMazeException();
        			}

        			if ((line.charAt(i) != 'e' &&
        				line.charAt(i) != 'x' &&
        				line.charAt(i) != '.' &&
        				line.charAt(i) != '#')){

        				throw new InvalidMazeException();
        			}

        			char symbol = line.charAt(i);
        			
        			maze.tiles.get(maze.tiles.size()-1).add(Tile.fromChar(symbol));
        			
        			if (symbol == 'e'){
    					maze.setEntrance(maze.tiles.get(maze.tiles.size()-1).get(i));
        			}
        			else if (symbol == 'x'){
    					maze.setExit(maze.tiles.get(maze.tiles.size()-1).get(i));
        			}
        		}
    			line = bufferedReader.readLine();
		 	}

		 	if (maze.getEntrance() == null){
		 		throw new NoEntranceException();
		 	}

		 	if (maze.getExit() == null){
		 		throw new NoExitException();
		 	}

	 	} 
	 	catch (FileNotFoundException e) {
        	return null;
        } 
        catch (IOException e) {
        	return null;
        }

        if (maze.tiles.size() == 0){
        	throw new InvalidMazeException();
        }

		return maze;
	}

	/**
	* getAdjacentTile is the method which is used to find an adjacent Tile instance
	* of the given Tile object in the provided direction.
	*
	* @param tile The instance is given to find a particular neighbouring Tile of it. 
	* @param direction Provides the direction to search for an adjacent Tile instance.
	* @return Tile instance which is adjecent to the given one in that direction
	* , if there is no such tile, null is returned.
	*/
	public Tile getAdjacentTile(Tile tile, Direction direction){
		Coordinate tileLocation = this.getTileLocation(tile);

		if (direction == Direction.NORTH){
			return getTileAtLocation(new Coordinate(tileLocation.getX(), tileLocation.getY() + 1));
		}
		else if (direction == Direction.SOUTH){
			return getTileAtLocation(new Coordinate(tileLocation.getX(), tileLocation.getY() - 1));
		}
		else if (direction == Direction.EAST){
			return getTileAtLocation(new Coordinate(tileLocation.getX() + 1, tileLocation.getY()));
		}
		else if (direction == Direction.WEST){
			return getTileAtLocation(new Coordinate(tileLocation.getX() - 1, tileLocation.getY()));
		}
		else{
			return null;
		}
	}

	/**
	* getEntrance is the method used to return the entrance Tile instance.
	* @return the Tile instance which is described by the tile representation 
	* "e" (entrance)
	*/
	public Tile getEntrance(){
		return this.entrance;
	}

	/**
	* getExit is the method used to return the exit Tile instance.
	* @return the Tile instance which is described by the tile representation 
	* "x" (exit)
	*/
	public Tile getExit(){
		return this.exit;
	}

	/**
	* getTileAtLocation is the method used to find a specific Tile instance
	* at provided Coordinates from the Coordinate instance.
	*
	* @param c Provides the instance of Coordinate class. 
	* @return Tile instance at specific coordinates is those are valid, otherwise - null. 
	*/
	public Tile getTileAtLocation(Coordinate c){
		try{
			int x = this.getTiles().size() - c.getY() - 1;
			int y = c.getX();

			if (x < 0 || y < 0 || x >= this.getTiles().size() || y >= this.getTiles().get(0).size()){
				return null;
			}

			return this.getTiles().get(x).get(y);
		}
		catch (IndexOutOfBoundsException e){
			return null;
		}
		catch (Exception e){
			return null;
		}
	}

	/**
	* getTileLocation is the method to get a Coordinate instance of a specific 
	* provided tile instance.
	*
	* @param tile Tile instance is provided.
	* @return Coordinate instance if the given Tile instance was in the List,
	* if it was not - null is returned.
	*/
	public Coordinate getTileLocation(Tile tile){
		for (int i = 0; i < this.getTiles().size(); i++ ) {
	 		for (int j = 0; j < this.getTiles().get(0).size() ; j++ ) {
	 			if (tile == this.getTiles().get(i).get(j)){
	 				return new Coordinate(j, this.getTiles().size() - i - 1);
	 			}
	 		}
	 	}
		return null;
	}

	/**
	* getTiles is the method used to return the tiles of the current Maze instance. 
	* 
	* @return a two dimensional List of Tile instances. 
	*/
	public List<List<Tile>> getTiles(){
		return tiles;
	}

	private void setEntrance(Tile tile){
		if (this.entrance == null){
			for (int i = 0; i < this.getTiles().size(); i++) {
				if (this.getTiles().get(i).contains(tile))
				{
					this.entrance = tile;
					return;
				}
			}
		}
		else{
			throw new MultipleEntranceException();
		}
	}

	private void setExit(Tile tile){
		if (this.exit == null){
			for (int i = 0; i < this.getTiles().size(); i++) {
				if (this.getTiles().get(i).contains(tile))
				{
					this.exit = tile;
					return;
				}
			}
		}
		else{
			throw new MultipleExitException();
		}
	}

	/**
	* toString is the overriden method which is used to provide a String representation
	* of a created Maze object.
	*
	* @return String representation of a Maze instance. 
	*/
	@Override
	public String toString(){
	 	String board = new String();
	 	List<List<Tile>> tiles = this.getTiles();

	 	for (int i = 0; i < tiles.size(); i++) {
	 		board += (tiles.size()- 1 - i) + "     ";;
	 		for (int j = 0; j < tiles.get(0).size() ; j++) {
    			board += tiles.get(i).get(j).toString() + "  ";
	 		}
	 		board += "\n";
	 	}
	 	board += "\n\n      ";
	 	for (int j = 0; j < tiles.get(0).size() ; j++ ) {
    			String emptySpace = "  ";
    			if (j > 9){
    				emptySpace = " ";
    			}
    			board += j + emptySpace;
	 		}

		return board;
	}
}