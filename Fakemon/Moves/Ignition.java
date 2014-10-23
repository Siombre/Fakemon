
import fakemon.MoveInfo;
import fakemon.Type;


public class Ignition extends MoveInfo {
	public Ignition (){
		init("Ignition", 10, 16, 100, 75, true, Category.PHYSICAL, Type.getByName("fire"));
	}

}

//This move will be a status move eventually. The description will be something like this: The user lights its body on fire.
//This makes opponent too afraid to use any contact moves. 

//This will be the signature move of the fire starter