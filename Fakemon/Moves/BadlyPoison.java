
import effects.BadPoisonEffect;
import effects.PoisonEffect;
import fakemon.BattleScreen;
import fakemon.MoveInfo;
import fakemon.Pokemon;
import fakemon.Type;


public class BadlyPoison extends MoveInfo{

	public BadlyPoison (){
		init("Badly Poison", 10, 16, 100, 0, false, Category.STATUS, Type.getByName("poison"));
		
		
	}
	@Override
	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		target.addEffect(new BadPoisonEffect(),user,battle);
		return true;	
	}


}
