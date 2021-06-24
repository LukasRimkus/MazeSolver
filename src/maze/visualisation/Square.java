package maze.visualisation;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
* Square is the class which sets some properties for JavaFX shape rectangle
* which is required to display one element (Corridor or Wall) of the maze 
* in the window.
*/
public class Square {
	private Rectangle square;

	/**
	* The Constructor which takes as its inputs the size and
	* the color of a rectangle.
	*/
	public Square(int size, Color color){
		this.square = new Rectangle();
		this.setDimensions(size);
		this.setColour(color);
	}

	private void setDimensions(int size){
		this.getSquare().setHeight(size);
		this.getSquare().setWidth(size);
	}

	private void setColour(Color color){
		this.getSquare().setFill(color);
	}

	/**
	* getSquare is the method which return a single Tile of the maze.
	*
	* @return the JavaFX Rectangle which represent a Tile in the window.
	*/
	public Rectangle getSquare(){
		return this.square;
	}
}