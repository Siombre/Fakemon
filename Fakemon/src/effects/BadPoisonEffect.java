package effects;

import fakemon.BattleScreen;
import fakemon.Pokemon;
import fakemon.PokemonInfo;
import fakemon.Screen;
import fakemon.Type;

public class BadPoisonEffect extends Effect{
	
	double psnAmount = 1/16f;
	
	public void onTurnEnd(BattleScreen screen){
		screen.displayMessage(target.getName() + " took damage from poison!");

		screen.damage(target,(int) (psnAmount * target.getStat(PokemonInfo.MAX_HP)));
		psnAmount += 1/16f;
	}

	@Override
	public boolean add(Effect e, Screen screen) {
		if(e instanceof BadPoisonEffect)
		{
			psnAmount += 1/16f;
			return true;
		}
		return false;
	}
	@Override
	public boolean canBeApplied(Pokemon p,Screen screen)
	{
		boolean isPoison = p.getTypes().contains(Type.getByName("Poison"));
		boolean isSteel = p.getTypes().contains(Type.getByName("Steel"));

		return !isPoison && !isSteel;
	}
	@Override
	public boolean conflicts(Effect e, Screen screen) {
		return false;
	}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		screen.displayMessage(target.getName() + " was badly poisoned!");
	}
}
