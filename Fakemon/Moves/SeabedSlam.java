

import fakemon.BattleScreen;
import fakemon.Move;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;

//This will be the signature move of the Nepo

public class SeabedSlam extends MoveInfo {
	public SeabedSlam (){
		init("Seabed Slam", 10, 16, 100, 75, true, Category.PHYSICAL, Type.getByName("water"));
	}
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		super.onHit(user, target, battle);
		
		
		//Secondary Effect: Every move of the target loses 10% PP 
		
		battle.displayMessage(target.getName() + " is fatigued.");
		boolean success = false;
		for(Move m : target.getMoves()){
			if(m == null) continue;
			int newPP = (int) (m.getCurPP()*.9);
			if(newPP != m.getCurPP())
			{
				success = true;
				m.setCurPP(newPP);
			}
		}
		return success;
	}
	
}


