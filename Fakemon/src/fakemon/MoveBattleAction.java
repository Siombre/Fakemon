package fakemon;

public class MoveBattleAction extends BattleAction {

	Move m;
	MoveInfo mi;
	Pokemon user;
	Pokemon[] targets;
	public MoveBattleAction(Move m, Pokemon p, Pokemon... targets){
		this.m = m;
		mi = m.getInfo();
		user = p;
		this.targets = targets;
	}
	public float getSpeed() {
		return user.getStat(PokemonInfo.SPEED);
	}
	
	public void doAction(BattleScreen bsc) {
		for(Pokemon target : targets)
		{
			bsc.displayMessage(user.getName() + " used " + mi.getName() + ".");
			m.onUse(user, target, bsc);
		}
	}
	public int getPriority(){
		return m.getInfo().getPriority();
	}
	@Override
	public boolean validate(BattleScreen screen) {
		user.onTurnStart(screen);
		return user.getHealth() > 0 && m.isValid() && user.canAttack(screen,m);
	}

}
