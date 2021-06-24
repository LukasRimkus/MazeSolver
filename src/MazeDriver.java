import maze.Maze;
import maze.Tile;
import maze.Tile.Type;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;
import java.io.FileNotFoundException;

import maze.routing.RouteFinder;
import maze.routing.NoRouteFoundException;
import maze.visualisation.Square;
import maze.visualisation.TextLabel;;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
* MazeDriver is the class which allows to read from a text file and solve the maze in the console. 
*/
public class MazeDriver {
    
    /**
    * main is the method used to let the user to display and solve the maze in the console. 
    * @param args default main parameter.
    */
    public static void main(String args[]) {
    	try{
    		Maze maze = Maze.fromTxt("../resources/mazes/maze1.txt");
    		System.out.println(maze.toString());
    	}
    	catch (MultipleEntranceException e){
    		System.out.println(e);
		}
		catch (MultipleExitException e){
    		System.out.println(e);
		}
		catch (NoEntranceException e){
    		System.out.println(e);
		}
		catch (NoExitException e){
    		System.out.println(e);
		}
		catch (RaggedMazeException e){
    		System.out.println(e);
		}
        catch (InvalidMazeException e){
            System.out.println(e);
        }
		catch (Exception e){
        	System.out.println("Some unforseen error");
    		System.out.println(e);
        }
    }
}
