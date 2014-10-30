package Moves;

import fakemon.MoveInfo;
import fakemon.Type;


public class ExecutionOrder extends MoveInfo {
	public ExecutionOrder(){
		init("Execution Order", 10, 16, 100, 70, false, Category.PHYSICAL, Type.getByName("ghost"));

	}
}
