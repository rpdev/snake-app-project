package se.chalmers.snake.gameGUI.elementlib;

import java.util.HashMap;

public class GraphicLibrary {
	private  HashMap<String, String> graphicLib;
	
	/*
	 * Creates new library of files!
	 */
	
	public GraphicLibrary(){
		graphicLib = new HashMap<String, String>();
		// Exempel på hur man lägger till en resurs i biblioteket. Görs eftersom man skapar GUI och vet vilka bilder vi ska använda.
		//graphicLib.put(logo, logo.gif);
	}
	
	/*
	 * Return the source-location for given file-id
	 */
	public String getSource(String name){
		return graphicLib.get(name);
	}
	
	
}
