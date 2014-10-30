package Moves;

import fakemon.MoveInfo;
import fakemon.Type;


public class Antibodies extends MoveInfo {
	public Antibodies (){
		init("Antibodies", 20, 36, 100, 40, false, Category.SPECIAL, Type.getByName("Poison"));
	}

}

//This move will eventually be a status move. The user poisons itself, which maximizes its Special Defense.

//This will be the Signature move of Igaken
