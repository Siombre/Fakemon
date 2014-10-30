package Moves;

import fakemon.MoveInfo;
import fakemon.Type;


public class BoneRattleChant extends MoveInfo {
	public BoneRattleChant (){
		init("Bone Rattle Chant", 10, 16, 100, 60, false, Category.SPECIAL, Type.getByName("dark"));
	}

}
