

import fakemon.MoveInfo;
import fakemon.Type;


public class Creep extends MoveInfo {
	public Creep (){
		init("Creep", 20, 36, 100, 40, true, Category.PHYSICAL, Type.getByName("bug"));
	}

}

//Secondary Effect: Has a 50% of making the opponent flinch. However, if the target flinches, it raises the
//targets speed by 2 stages

//This will be the signature move of Bugpag