package Moves;

import fakemon.MoveInfo;
import fakemon.Type;


public class AutumnStorm extends MoveInfo {
	public AutumnStorm (){
		init("Autumn Storm", 10, 16, 100, 75, false, Category.SPECIAL, Type.getByName("grass"));
		
		
	}
	@Override
	public float getCritRateMod(){
		return 4.0f;
	}
}

//Secondary Effect: 50% chance of raising users evasion by 1 stage
//Secondary Effect: Has a high critical hit ratio

//This will be the Signature move of the grass starter.