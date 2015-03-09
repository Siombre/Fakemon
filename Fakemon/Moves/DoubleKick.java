
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;


public class DoubleKick extends MoveInfo {

	public DoubleKick(){
		init("Double Kick", 30, 48, 100, 30, true, Category.PHYSICAL, Type.getByName("fighting"));

	}
	public void onUse(Pokemon user, Pokemon target, BattleScreen battle){
		battle.displayMessage(user.getName() + " used " + getName() + ".");
		int hits = 0;
		for(int i = 0; i<2 ;i++)
		{
			if(target.getHealth() <= 0) continue;
			if(doesHit(user, target, battle)){
				onHit(user, target, battle);
				hits++;
			}else{
				onMiss(user, target, battle);
				
			}
		}
		
		battle.displayMessage("It hit " + hits + ((hits!=1)?" times.":" time."));

	}
}
