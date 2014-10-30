package fakemon;

public abstract class BattleAction {
	public abstract float getSpeed();
	public abstract int getPriority();
	public abstract void doAction(BattleScreen bsc);
	public abstract boolean validate();
}
