package fakemon;

public abstract class BattleAction {
	public abstract float getSpeed();
	public abstract int getPriority();
	public abstract void doAction(BattleScreen bsc);
	/**
	 * Checks whether this action can still be performed
	 * 
	 * @param bsc The relevant Battle Screen
	 * @return true iff the action can still be performed
	 */
	public abstract boolean validate(BattleScreen bsc);
}
