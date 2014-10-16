package fakemon;

public class MoveBattleAction extends BattleAction {

	Move m;
	Pokemon user;
	Pokemon[] targets;
	public MoveBattleAction(Move m, Pokemon p, Pokemon... targets){
		this.m = m;
		user = p;
		this.targets = targets;
	}
	public float getSpeed() {
		return user.getStats()[PokemonInfo.SPEED];
	}
	
	public void doAction(BattleScreen bsc) {
		for(Pokemon target : targets)
			m.onUse(user, target, bsc);
	}
	@Override
	public boolean validate() {
			
		return user.getHealth() > 0;
	}

}
