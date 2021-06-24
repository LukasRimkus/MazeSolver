import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;  
import javafx.scene.text.TextAlignment;  
import javafx.scene.text.Font;  
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

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
import maze.visualisation.TextLabel;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
* MazeApplication is the class used to create a window of the required UI elements 
* to solve the maze. The process of solving of maze is controlled and displayed in the 
* created window. 
* Also, it extends Application to use the JavaFX elements. 
*/
public class MazeApplication extends Application{
	private Maze maze;
	private RouteFinder routeFinder; 
	private final int SQUARE_SIZE = 45;

	/**
	* start method is an overriden method which initiliases the window
	* using the JavaFX elements determined in the code.
	*
	* @param stage is the top level JavaFX container which consists
	* of other containers or UI elements added in the code.
	* @throws InvalidMazeException if an invalid maze was provided.
	*/
	@Override
	public void start(Stage stage) throws FileNotFoundException{
		int[] windowDimensions = {0, 0};

		VBox topPane = new VBox(10);
		topPane.setAlignment(Pos.CENTER);

		GridPane mazePane = new GridPane();
		mazePane.setAlignment(Pos.CENTER);

		TextLabel bottomText = new TextLabel(Color.WHITE, SQUARE_SIZE/5*2, "", TextAlignment.CENTER);

		Button loadMap = new Button("Load Map");
		loadMap.setFont(new Font(SQUARE_SIZE/2));

		loadMap.setOnAction(event->{
			try{
				FileChooser loadMapFileChooser = new FileChooser();
				loadMapFileChooser.setTitle("Open Maze Txt File");

				loadMapFileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
				File newSelectedFile = loadMapFileChooser.showOpenDialog(stage);
			 	if (newSelectedFile != null) {
		 			
					maze = Maze.fromTxt(newSelectedFile.getAbsolutePath());

					if (maze == null){
						throw new InvalidMazeException("Could not load maze!");
					}

					routeFinder = new RouteFinder(maze);
					
					mazePane.getChildren().clear();
					updateMazeRepresentation(routeFinder, maze, mazePane);
					
					windowDimensions[0] = SQUARE_SIZE*maze.getTiles().size() + 250;
					windowDimensions[1] = SQUARE_SIZE*maze.getTiles().get(0).size();
					
					stage.setWidth(windowDimensions[1]);
					stage.setHeight(windowDimensions[0]);
					stage.setMinWidth(windowDimensions[1]);
					stage.setMinHeight(windowDimensions[0]);

		    		bottomText.setText("");
	    		}
			}
			catch (MultipleEntranceException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
			}
			catch (MultipleExitException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
			}
			catch (NoEntranceException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
			}
			catch (NoExitException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
			}
			catch (RaggedMazeException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
			}
	        catch (InvalidMazeException e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
	        }
	        catch (Exception e){
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
	        }
	 	});

		Button loadRoute = new Button("Load Route");
		loadRoute.setFont(new Font(SQUARE_SIZE/2));
		loadRoute.setOnAction(event->{
			FileChooser loadRouteFileChooser = new FileChooser();
			loadRouteFileChooser.setTitle("Open Maze Object File");

			loadRouteFileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("All files", "*.*")
			);

			File newSelectedFile = loadRouteFileChooser.showOpenDialog(stage);

		 	if (newSelectedFile != null) {
				String path = newSelectedFile.getAbsolutePath();
				bottomText.setText("");
	            
	            try{
	           	 	routeFinder = RouteFinder.load(path);
	            	
		            if (routeFinder != null){
		            	
	            		maze = routeFinder.getMaze();

			            mazePane.getChildren().clear();
						updateMazeRepresentation(routeFinder, maze, mazePane);

			            windowDimensions[0] = SQUARE_SIZE*maze.getTiles().size() + 250;
						windowDimensions[1] = SQUARE_SIZE*maze.getTiles().get(0).size();

						stage.setWidth(windowDimensions[1]);
						stage.setHeight(windowDimensions[0]);
						stage.setMinWidth(windowDimensions[1]);
						stage.setMinHeight(windowDimensions[0]);
		            }
		            else{
		            	throw new InvalidMazeException("Could not open that file!");
		            }
	        	}
	            catch (InvalidMazeException e){
		    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
	        	}
				catch (Exception e) {
		    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
		        }
			}
	 	});

		Button saveRoute = new Button("Save Route");
		saveRoute.setFont(new Font(SQUARE_SIZE/2));
		saveRoute.setOnAction(event->{
			try{
		 		FileChooser saveRouteFileChooser = new FileChooser();
				saveRouteFileChooser.setTitle("Save the Maze Object File as .obj, .ser or .route");

				File newSelectedFile = null;

				if (maze != null && routeFinder != null){
					newSelectedFile = saveRouteFileChooser.showSaveDialog(stage);
				}

				if (newSelectedFile != null) {
					String path = newSelectedFile.getAbsolutePath();

					routeFinder.save(path);
				}
			}
			catch (InvalidMazeException e){
    			updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
	        }
	        catch (Exception e) {
	    		updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
	        }
	 	});

		Button stepBtn = new Button("Step");
		stepBtn.setFont(new Font(SQUARE_SIZE/2));
		stepBtn.setOnAction(event->{
			if (maze != null && routeFinder != null){
				if (!routeFinder.isFinished())
				{
					try{
						boolean finished = routeFinder.step();
						updateMazeRepresentation(routeFinder, maze, mazePane);

						bottomText.setText("");

						if (finished){
							String successText = new String();

							// displaying text differently regarding the size of the maze
							if (maze.getTiles().get(0).size() < 8){
								successText = "Congratulations for\ncompleting this\n Maze! Now you can\nselect another\nchallenging maze to\nsolve!";
								stage.setHeight(windowDimensions[0] + 140);
								stage.setMinHeight(windowDimensions[0] + 140);
							}
							else{
								successText = "Congratulations for completing this\nMaze! Now you can select\nanother challenging maze to solve!";
								stage.setHeight(windowDimensions[0] + 100);
								stage.setMinHeight(windowDimensions[0] + 100);
							}

							bottomText.setText(successText);
							bottomText.setColour(Color.WHITE);
							
							stage.setWidth(windowDimensions[1]);
							stage.setMinWidth(windowDimensions[1]);
						}
					}
					catch (NoRouteFoundException e){
		    			bottomText.setText("");
		    			updatebottomTextWithExceptionText(e.toString(), windowDimensions, stage, bottomText);
					}
				}
			}
	 	});

	 	topPane.getChildren().addAll(loadMap, loadRoute, saveRoute);
		topPane.setStyle("-fx-padding: 5;");

		// step button
		VBox bottomPane = new VBox(10);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.getChildren().addAll(stepBtn);

		VBox errorPane = new VBox(10);
		errorPane.setAlignment(Pos.CENTER);
		errorPane.getChildren().addAll(bottomText.getTextLabel());
		
		if (maze != null)
		{
			updateMazeRepresentation(routeFinder, maze, mazePane);
		}

		VBox root = new VBox(10);
		root.getChildren().addAll(topPane, mazePane, bottomPane, errorPane);

		root.setBackground(new Background(new BackgroundFill(Color.rgb(73, 163, 253), CornerRadii.EMPTY, Insets.EMPTY)));

		Scene scene = new Scene(root, Color.BLUE);
		stage.setScene(scene);

		if (windowDimensions[0] != 0 && windowDimensions[1] != 0){
			stage.setWidth(windowDimensions[1]);
			stage.setHeight(windowDimensions[0]);

			stage.setMinWidth(windowDimensions[1]);
			stage.setMinHeight(windowDimensions[0]);
		}
		else{
			stage.setMinWidth(320);
			stage.setMinHeight(260);
		}
		stage.setTitle("Maze Solver"); 
		stage.show(); 
	}

	/**
    * main method for lauching the start class (javafx... )
    * @param args
    */
	public static void main(String args[]) {
		launch(args);
	}

	private void updatebottomTextWithExceptionText(String e, int[] windowDimensions, Stage stage, TextLabel bottomText){
		String errorText = e.split(":", 0)[1] + "\nTry another maze!";
		
		if (windowDimensions[0] != 0){
			stage.setHeight(windowDimensions[0] + 60);
			stage.setWidth(windowDimensions[1]);

			stage.setMinWidth(windowDimensions[1]);
			stage.setMinHeight(windowDimensions[0] + 60);
		}
		else{
			stage.setHeight(300);
			stage.setMinHeight(300);
		}

		bottomText.setText(errorText);
		bottomText.setColour(Color.rgb(202, 12, 12));
	}

	private void updateMazeRepresentation(RouteFinder routeFinder, Maze maze, GridPane mazePane){
		List<Tile> currentRoute = routeFinder.getRoute();
		List<Tile> removedTiles = routeFinder.getRemovedTiles();

		int length = maze.getTiles().get(0).size();
		int width = maze.getTiles().size();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				boolean isInRoute = currentRoute.contains(maze.getTiles().get(i).get(j));
				boolean isRemoved = removedTiles.contains(maze.getTiles().get(i).get(j));

				if (maze.getTiles().get(i).get(j).getType() == Type.WALL){
					Square wall = new Square(SQUARE_SIZE, Color.BLUE);
					mazePane.add(wall.getSquare(), j, i);
				}
				else if (maze.getTiles().get(i).get(j).getType() == Type.EXIT && !isInRoute){
					Square corridor = new Square(SQUARE_SIZE, Color.GRAY);
					mazePane.add(corridor.getSquare(), j, i);

					TextLabel exit = new TextLabel(Color.BLACK, 20, "X", TextAlignment.CENTER);
					mazePane.add(exit.getTextLabel(), j, i);
				}
				else if (maze.getTiles().get(i).get(j).getType() == Type.ENTRANCE && !isInRoute){
					Square corridor = new Square(SQUARE_SIZE, Color.GRAY);
					mazePane.add(corridor.getSquare(), j, i);

					TextLabel entrance = new TextLabel(Color.RED, 20, "e", TextAlignment.CENTER);
					mazePane.add(entrance.getTextLabel(), j, i);
				}
				else{
					Square corridor = new Square(SQUARE_SIZE, Color.GRAY);
					mazePane.add(corridor.getSquare(), j, i);
				}

				if (isInRoute && !isRemoved){
					TextLabel visitedSquare = new TextLabel(Color.YELLOW, SQUARE_SIZE/2, "*", TextAlignment.CENTER);
					mazePane.add(visitedSquare.getTextLabel(), j, i);
				}
				else if (isRemoved && !isInRoute){
					TextLabel visitedSquare = new TextLabel(Color.YELLOW, SQUARE_SIZE/2, "-", TextAlignment.CENTER);
					mazePane.add(visitedSquare.getTextLabel(), j, i);
				}
			}
		}	
	}
}

