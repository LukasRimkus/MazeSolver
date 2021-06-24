package maze.routing;

import maze.Maze;
import maze.Maze.Direction;
import maze.Tile;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import java.io.Serializable;
import java.io.FileInputStream;  
import java.io.ObjectInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
* RouteFinder is the class which is used to solve a maze by one step, 
* load or save a serialised object.
*/
public class RouteFinder implements Serializable{
	private Maze maze;
	private Stack <Tile> route;
	private boolean finished;
	private List<Tile> removedTiles;

	/**
	* The constructor which takes as its input Maze instance. 
	* 
	* @param maze Maze instance used to assign a maze to the RouteFinder object
	*/
	public RouteFinder(Maze maze){
		this.maze = maze;
		route = new Stack <Tile>();
		removedTiles = new ArrayList<Tile>();
	}

	/**
	* getMaze is the method used to return a Maze instance attribute 
	* of the RouteFinder instance.
	*
	* @return Maze instance of the RouteFinder instance. 
	*/
	public Maze getMaze(){
		return this.maze;
	}

	/**
	* getRoute is the method which return all the route Tile instances from the 
	* start to the last one in the List.
	* 
	* @return List of Tile instances which the route consists of.
	*/
	public List<Tile> getRoute(){
		return this.route;
	}

	/**
	* getRemovedTiles is the method which return all the removedTiles Tile instances from the 
	* start to the last one in the List.
	* 
	* @return List of Tile instances which the removedTiles consists of. 
	*/
	public List<Tile> getRemovedTiles(){
		return this.removedTiles;
	}

	/**
	* isFinished is the method which provides information if the maze has been completed.
	* 
	* @return boolean value <code>true</code> if the route List of Tile instances contains
	* both the entrance and exit Tile instances.
	*/
	public boolean isFinished(){
		this.finished = this.getRoute().contains(this.maze.getExit()) && this.getRoute().contains(this.maze.getEntrance());
		return this.finished;
	}

	/**
	* load is the static method which tries to take a RouteFinder instance from 
	* a file from the location given in the path variable. 
	*
	* @param path provides information where the path of an object to be deserialised is
	* @return a RouteFinder instance which was created from an object file or null if an exception
	* was caught.
	*/ 
	public static RouteFinder load(String path){
        try(
            ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(path));
        	) 
        {
            return (RouteFinder)objectStream.readObject();
        } 
        catch (FileNotFoundException e) {
        	return null;
        } 
        catch (IOException | ClassNotFoundException e) {
        	return null;
        } 
	}

	/**
	* save is the method which saves a current state of the RouteFinder instance in a particular location.
	* 
	* @param path is the location where a serialised object should be created.
	*
	* @throws InvalidMazeException if any error occurred while saving a new object to given location.
	*/
	public void save(String path){
	    try(
	    	ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(path));
	    	)
	    {
	        objectStream.writeObject(this);
	    }
	    catch (FileNotFoundException e) {
            throw new InvalidMazeException("That file was not found!");
	    } 
	    catch (IOException e) {
            throw new InvalidMazeException("Could not write to that file!");
	    } 
	}

	/**
	* step is the method which solves a maze step by step, that is adds or removes one Tile instance
	* from the List of Tile instances per method call.
	*
	* @return booelan value <code>true</code> if the maze has been solved, otherwise - <code>false</code>
	*
	* @throws NoRouteFoundException if the maze is unsolvable
	*/
	public boolean step(){
		if (!isFinished()){
			if (this.getRoute().isEmpty()){
				this.getRoute().add(this.getMaze().getEntrance());
			}
			else{
				// is required to determine if I need to remove a current Tile (if no neighbours)
				boolean foundTileWithUnvisitedNeighbour = false;
            	Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
            	
            	// get the top element of the route
				Tile element = this.getRoute().get(this.getRoute().size() - 1);
				foundTileWithUnvisitedNeighbour = false;

	            // get neighbours of the top element
	            List<Tile> neighbours = new ArrayList<Tile>();
	            
	            for (int i = 0; i < directions.length; i++) {
	            	Tile neighbour = this.getMaze().getAdjacentTile(element, directions[i]);
	            	if (neighbour != null && neighbour.isNavigable()){
						neighbours.add(neighbour);
					}
	            }

				for (int i = 0; i < neighbours.size() && !foundTileWithUnvisitedNeighbour; i++) {
					if (!this.getRoute().contains(neighbours.get(i)) && !this.getRemovedTiles().contains(neighbours.get(i))){
						this.getRoute().add(neighbours.get(i));
						foundTileWithUnvisitedNeighbour = true;
	            	}
				}

				// if none neighbours of a Tile instance were found then
				// all the Tiles of the route list are tested if any of them
				// have a neighbour; it it is not a case, then this maze is unsolvable
				if (!foundTileWithUnvisitedNeighbour){
					boolean anyNeighbours = false;

					for (int i = 0; i < this.getRoute().size() && !anyNeighbours; i++){
	            		for (int j = 0; j < directions.length && !anyNeighbours; j++) {
			            	Tile neighbour = this.getMaze().getAdjacentTile(this.getRoute().get(i), directions[j]);
			            	if (neighbour != null && neighbour.isNavigable() && !this.getRemovedTiles().contains(neighbour) && !this.getRoute().contains(neighbour)){
								anyNeighbours = true;
							}
			            }
					}

					if (!anyNeighbours){
						throw new NoRouteFoundException();
					}
					else{
						this.getRemovedTiles().add(element);
						this.getRoute().remove(this.getRoute().size() - 1);
					}
				}
			}
		}
		return this.isFinished();
	}

	/**
	* toString is the overriden method used to return the representation of the current state of maze solving
	*
	* @return String representation of the maze solving state.
	*/
	@Override
	public String toString(){
		String board = new String();
	 	List<List<Tile>> tiles = this.getMaze().getTiles();

	 	for (int i = 0; i < tiles.size(); i++ ) {
	 		board += (tiles.size()- 1 - i) + "     ";;
	 		for (int j = 0; j < tiles.get(0).size() ; j++ ) {
	 				if (this.getRoute().contains(tiles.get(i).get(j))){
	 					board += "*  ";
		 			}
		 			else if (this.getRemovedTiles().contains(tiles.get(i).get(j))){
	 					board += "-  ";
		 			}
		 			else{
	    				board += tiles.get(i).get(j).toString() + "  ";
		 			}
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