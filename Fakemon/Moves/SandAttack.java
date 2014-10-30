
import effects.Effect;
import effects.ParalyzeEffect;
import effects.StatModEffect;
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;


public class SandAttack extends MoveInfo{

	public SandAttack (){
		init("Sand Attack", 10, 16, 100, 0, false, Category.STATUS, Type.getByName("ground"));
		
		
	}
	@Override
	public float getCritRateMod(){
		return 4.0f;
	}
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		target.addEffect(new StatModEffect(Effect.ACCURACY,-1),user,battle);
		return true;	
	}


}
