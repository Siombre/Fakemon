package Moves;

import fakemon.MoveInfo;
import fakemon.Type;


public class Petrification extends MoveInfo {
	public Petrification (){
		init("Petrification", 15, 25, 100, 55, true, Category.PHYSICAL, Type.getByName("rock"));
	}

}

//This will eventually be a Status move with a rather complicated effect. It has a three turn effect.
//Turn One: The opponent's speed is reduced by 1
//Turn Two: The opponent's speed is reduced by an additional 2
//Turn Three: The opponent's speed is reduced by an additional 3 and the type of the target becomes pure
//rock type.

//It is the signature move of Petrifly