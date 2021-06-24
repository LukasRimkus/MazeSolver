package maze;

import java.io.Serializable;

/**
* Tile is a class which is used to describe a single tile in 
* the maze - it can represent corridor, entrance, exit, or wall of a maze.
*/
public class Tile implements Serializable {
	/**
	* Type is the inner enum which represents a specific type of a maze tile -
	* it can be one of corridor, entrance, exit, or wall.
	*/
	public enum Type{
		CORRIDOR, 
		ENTRANCE, 
		EXIT, 
		WALL;
	}

	private Type type;

	private Tile(Type type){
		this.type = type;
	}

	/**
	* fromChar is the method which takes a single character and if it of
	* type '.', 'e', 'x', or '#', the method will assign a particular type 
	* for this tile. If none of those is provided, the default value is WALL. 
	* 
	* @param c a character required to determine the type of the tile
	* @return a new Tile instance created using a given character
 	*/
	protected static Tile fromChar(char c){
		Type typeForNewTile;

		switch(c){
			case '.':
				typeForNewTile = Type.CORRIDOR;
				break;
			case 'e':
				typeForNewTile = Type.ENTRANCE;
				break;
			case 'x':
				typeForNewTile = Type.EXIT;
				break;
			case '#':
				typeForNewTile = Type.WALL;
				break;
			default:
				typeForNewTile = Type.WALL;
				break;
		}

		return new Tile(typeForNewTile);
	}

	/**
	* getType is the method to get a Type of a Tile instance
	*
	* @return an enum value of a specific Tile object
	*/
	public Type getType(){
		return type;
	}

	/**
	* isNavigable is the method which returns a boolean value
	* which shows if a tile can be visited
	*
	* @return <code>true</code> is it is of type CORRIDOR, ENTRANCE, or 
	* EXIT, otherwise - <code>false</code>
	*/
	public boolean isNavigable(){
		if (type == Type.CORRIDOR || 
			type == Type.ENTRANCE || 
			type == Type.EXIT){

			return true;
		}
		else{
			return false;
		}
	}

	/**
	* toString is the method which return a string representation
	* of a tile type
	*
	* @return String of tile type (".", "#", "e", or "x")
	*/
	@Override
	public String toString(){
		if (type == Type.CORRIDOR){
			return ".";
		}
		else if (type == Type.ENTRANCE){
			return "e";
		}
		else if (type == Type.EXIT){
			return "x";
		}
		else if (type == Type.WALL){
			return "#";
		}
		else{
			return "#";
		}
	}
}