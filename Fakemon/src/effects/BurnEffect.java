package effects;

import fakemon.BattleScreen;
import fakemon.Pokemon;
import fakemon.PokemonInfo;
import fakemon.Screen;
import fakemon.Type;

public class BurnEffect extends Effect {

	@Override
	public boolean add(Effect e, Screen screen) {
		
		return false;
	}
	
	public boolean prevents(Effect e,Screen screen)
	{
		if(e instanceof BurnEffect)
		{
			return true;
		}
		return false;
	}
	
	public void onTurnEnd(BattleScreen screen){
		screen.displayMessage(target.getName() + " took damage from burn!");

		screen.damage(target,(int) ((1f/8) * target.getStat(PokemonInfo.MAX_HP)));
	}
	public boolean canBeApplied(Pokemon p,Screen screen)
	{
		return !p.getTypes().contains(Type.getByName("Fire"));
	}
	@Override
	public boolean conflicts(Effect e, Screen screen) {
		return false;
	}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		screen.displayMessage(target.getName() + " was burned!");
	}
	public float getDamMod(){
		return .5f;
	}
}
