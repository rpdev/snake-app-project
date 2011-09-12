package se.chalmers.snake.gameGUI.elementlib;

import java.util.HashMap;

public class GraphicLibrary {
	private  HashMap<String, String> graphicLib;
	
	/*
	 * Creates new library of files
	 */
	
	public GraphicLibrary(){
		graphicLib = new HashMap<String, String>();
		//graphicLib.put(logo, logo.gif);
	}
	
	/*
	 * Return the source-location for given file-id
	 */
	public String getSource(String name){
		return graphicLib.get(name);
	}
	
	
}
