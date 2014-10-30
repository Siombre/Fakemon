
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
		hit(user, target, battle);
		hit(user, target, battle);

	}
}
