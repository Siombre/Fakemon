package effects;

import fakemon.Pokemon;
import fakemon.Screen;
import fakemon.Util;

public class ParalyzeEffect extends Effect{
	public boolean canAttack(Screen screen){
		boolean canAttack = Util.flip(.75f);
		if(!canAttack)
			screen.displayMessage(target.getName() + " couldn't move!");

		return canAttack;
	}
	public float getStatMod(int stat){
		if(stat == SPEED)
			return .25f;
		return 1;
	}
	@Override
	public boolean add(Effect e,Screen screen) {
		
		return false;
	}
	@Override
	public boolean conflicts(Effect e,Screen screen) {
		return false;
	}
	public boolean prevents(Effect e,Screen screen)
	{
		if(e instanceof ParalyzeEffect)
		{
			return true;
		}
		return false;
	}
	
	public void onRemove(Screen screen){
		
	}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		screen.displayMessage(target.getName() + " was paralyzed!");
	}

}
