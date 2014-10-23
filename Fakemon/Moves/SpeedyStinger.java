
import fakemon.MoveInfo;
import fakemon.Type;


public class SpeedyStinger extends MoveInfo {
	public SpeedyStinger (){
		init("Speedy Stinger", 15, 25, 100, 55, true, Category.SPECIAL, Type.getByName("bug"));
	}

}

//This move will have no secondary effect besides it is a high priority move (Priority = 1)

//It is the signature move of Mulmoryx