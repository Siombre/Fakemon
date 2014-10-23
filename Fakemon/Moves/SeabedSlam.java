
import fakemon.MoveInfo;
import fakemon.Type;


public class SeabedSlam extends MoveInfo {
	public SeabedSlam (){
		init("Seabed Slam", 10, 16, 100, 75, true, Category.PHYSICAL, Type.getByName("water"));
	}

}

//Secondary Effect: Every move of the target loses 1-2 PP

//This will be the signature move of the water starter