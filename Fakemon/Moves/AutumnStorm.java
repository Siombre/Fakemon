

import effects.Effect;
import effects.StatModEffect;
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;
import fakemon.Util;


public class AutumnStorm extends MoveInfo {
	public AutumnStorm (){
		init("Autumn Storm", 10, 16, 100, 75, false, Category.SPECIAL, Type.getByName("grass"));
		
		
	}
	@Override
	public float getCritRateMod(){
		return 4.0f;
	}
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		hit(user, target, battle);
		if(Util.flip(.5))
			user.addEffect(new StatModEffect(Effect.EVASION,1), user, battle);
		return true;
	}
}

//Secondary Effect: 50% chance of raising users evasion by 1 stage
//Secondary Effect: Has a high critical hit ratio

//This will be the Signature move of the grass starter.