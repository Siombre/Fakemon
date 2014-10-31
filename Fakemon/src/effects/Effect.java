package effects;

import fakemon.BattleScreen;
import fakemon.Move;
import fakemon.Pokemon;
import fakemon.Screen;
import fakemon.Type;

public abstract class Effect {
	Pokemon user;
	Pokemon target;
	public final static int ATTACK = 1;
	public final static int DEFENSE = 2;
	public final static int SPECIAL_ATTACK = 3;
	public final static int SPECIAL_DEFENSE = 4;
	public final static int SPEED = 5;
	public final static int EVASION = 6;
	public final static int ACCURACY = 7;
	public final static String[] statNames = {"Health","Attack","Defense","Special Attack","Special Defense","Speed","Evasion","Accuracy"};
	private boolean done;
	public void onDefend(){}
	public void onAttack(){}
	public void onBattleEnd(){}
	public void onTurnEnd(BattleScreen screen){}
	public boolean canAttack(Screen screen, Move m){
		return true;
	}
	public boolean canBeAttacked(Move m,BattleScreen screen){
		return true;
	}
	public Type getTypeMod(){
		return null;
	}
	public float getStatMod(int stat){
		return 1;
	}
	public boolean prevents(Effect e,Screen screen)
	{
		return false;
	}
	public boolean canSwap(BattleScreen screen){
		return true;
	}
	public abstract boolean add(Effect e,Screen screen);
	public abstract boolean conflicts(Effect e,Screen screen);
	public boolean canBeApplied(Pokemon p,Screen screen)
	{
		return true;
	}
	public final boolean isOver(){
		return done;
	}
	public final void end(){
		done = true;
	}
	public void onRemove(Screen screen){}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		this.user = user;
		this.target = target;
	}
	public float getDamMod(){
		return 1;
	}
	public void onTurnStart(BattleScreen screen){}
}
