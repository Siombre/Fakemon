package effects;

import fakemon.BattleScreen;
import fakemon.Move;
import fakemon.Pokemon;
import fakemon.Screen;
import fakemon.Util;

public class SleepEffect extends Effect{
	int turnsLeft;
	@Override
	public boolean canAttack(Screen screen, Move m){
		boolean canAttack = false;
		if(!canAttack)
			screen.displayMessage(target.getName() + " is fast asleep!");

		return canAttack;
	}
	@Override
	public boolean add(Effect e,Screen screen) {
		
		return false;
	}
	@Override
	public boolean prevents(Effect e,Screen screen)
	{
		if(e instanceof SleepEffect)
		{
			return true;
		}
		return false;
	}
	@Override
	public void onRemove(Screen screen){
		screen.displayMessage(target.getName() + " wakes up!");
	}
	@Override
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		screen.displayMessage(target.getName() + " falls asleep!");
		turnsLeft = Util.rand(1, 3);
	}
	@Override
	public void onTurnStart(BattleScreen screen){
		turnsLeft--;
		if(turnsLeft <= 0)
			end();
	}

	@Override
	public boolean conflicts(Effect e, Screen screen) {
		return false;
	}

}
