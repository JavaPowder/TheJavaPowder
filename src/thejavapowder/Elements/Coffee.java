package thejavapowder.Elements;

import thejavapowder.Elements.categories.Powder; // Imports powder code to be used in the element. Some default code too
import thejavapowder.Elements.categories.ElementDefault; // Makes the element follow a set of rules that it MUST have, such as an update function.
//public class -element name- implements ElementDefault extends -element state-
  public class Coffee implements ElementDefault extends Powder {
	//This is the code run when the element is found in the map. Return 1 if the loop was successful.
	public int update() { return 1; 
	}
	//Add your own functions and variables where you want them.
	
	
	
}