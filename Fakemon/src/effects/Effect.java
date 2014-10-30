package effects;

import fakemon.Move;
import fakemon.Type;

public abstract class Effect {
	public final static int ATTACK = 1;
	public final static int DEFENSE = 2;
	public final static int SPECIAL_ATTACK = 3;
	public final static int SPECIAL_DEFENSE = 4;
	public final static int SPEED = 5;
	public final static int EVASION = 6;
	public final static int ACCURACY = 7;
	private boolean done;
	public void onDefend(){}
	public void onAttack(){}
	public void onBattleEnd(){}
	public boolean canAttack(Move m){
		return true;
	}
	public boolean canBeAttacked(Move m){
		return true;
	}
	public Type getTypeMod(){
		return null;
	}
	public float getStatMod(int stat){
		return 1;
	}
	public boolean prevents(Effect e)
	{
		return false;
	}
	public boolean canSwap(){
		return true;
	}
	public abstract boolean add(Effect e);
	public abstract boolean conflicts(Effect e);
	public final boolean isOver(){
		return done;
	}
	public final void end(){
		done = true;
	}
}
