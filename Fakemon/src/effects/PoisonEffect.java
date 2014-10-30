package effects;

import fakemon.BattleScreen;
import fakemon.Pokemon;
import fakemon.PokemonInfo;
import fakemon.Screen;

public class PoisonEffect extends Effect {

	@Override
	public boolean add(Effect e, Screen screen) {
		
		return false;
	}
	
	public boolean prevents(Effect e,Screen screen)
	{
		if(e instanceof PoisonEffect)
		{
			return true;
		}
		return false;
	}
	
	public void onTurnEnd(BattleScreen screen){
		screen.displayMessage(target.getName() + " took damage from poison!");

		screen.damage(target,(int) ((1f/16) * target.getStat(PokemonInfo.MAX_HP)));
	}

	@Override
	public boolean conflicts(Effect e, Screen screen) {
		return false;
	}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		screen.displayMessage(target.getName() + " was poisoned!");
	}

}
