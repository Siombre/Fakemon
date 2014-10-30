
import effects.Effect;
import effects.ParalyzeEffect;
import effects.StatModEffect;
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;


public class Paralyze extends MoveInfo{

	public Paralyze (){
		init("Paralyze", 10, 16, 100, 0, false, Category.STATUS, Type.getByName("electric"));
		
		
	}
	@Override
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		target.addEffect(new ParalyzeEffect(),user,battle);
		return true;	
	}


}
