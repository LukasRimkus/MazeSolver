package maze.visualisation;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;  
import javafx.scene.text.TextAlignment;  
import javafx.scene.text.Font; 

/**
* TextLabel is the class which sets some properties for JavaFX text object 
* which is required to display text for the window. 
* in the window.
*/
public class TextLabel {
	private Text textLabel;

	/**
	* The Constructor which takes as its inputs the color, size, text and
	* alignment (for example, Center) of a text object.
	*/
	public TextLabel(Color color, int size, String text, TextAlignment textAlignment){
		this.textLabel = new Text();
		this.setColour(color);
		this.setFont(size);
		this.setText(text);
		this.setAlignment(textAlignment);
	}

	/**
	* setColour is the method which sets new colour for the instance of this object.
	*
	* @param color provides new colour to be be set.
	*/
	public void setColour(Color color){
		this.getTextLabel().setFill(color);
	}

	private void setFont(int size){
		this.getTextLabel().setFont(new Font(size));
	}

	/**
	* setText is the method which sets new text for the instance of this object.
	*
	* @param text provides new text to be be set.
	*/
	public void setText(String text){
		this.getTextLabel().setText(text);
	}

	private void setAlignment(TextAlignment textAlignment){
		this.getTextLabel().setTextAlignment(textAlignment);
	}

	/**
	* getTextLabel is the method which returns a JavaFX Text object.
	*
	* @return Text object.
	*/
	public Text getTextLabel(){
		return this.textLabel;
	}
}