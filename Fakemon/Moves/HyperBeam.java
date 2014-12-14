import fakemon.BattleAction;
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;


public class HyperBeam extends MoveInfo{
	public HyperBeam(){
		init("Hyper Beam", 10, 16, 100, 60, false, Category.SPECIAL, Type.getByName("dark"));
	}
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		super.onHit(user, target, battle);
		
		return true;
	}
	public class Recharge extends BattleAction{
		@Override
		public float getSpeed() {
			return 0;
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public void doAction(BattleScreen bsc) {
			
		}

		@Override
		public boolean validate(BattleScreen bsc) {
			return true;
		}
	}

}
