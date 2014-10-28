package fakemon;

import java.util.ArrayList;
import java.util.HashSet;


public abstract class MoveInfo {
	private static HashSet<MoveInfo> moves = new HashSet<MoveInfo>();
	private String name;
	public enum Category {PHYSICAL,SPECIAL,STATUS};
	private ArrayList<Type> types;
	private int maxPP;
	private int basePP;
	private int accuracy;
	private int power;
	private boolean contact;
	private Category cat;
	protected int priority;

	protected void init(String name, int basePP, int maxPP, int accuracy, int power, boolean contact, Category cat, Type... types)
	{

		if (name == null)
			name = "???";
		this.name = name;
		moves.add(this);

		this.types = new ArrayList<Type>();
		for(Type t:types)
			this.types.add(t);

		this.accuracy = accuracy;
		this.power = power;
		this.maxPP = maxPP;
		this.basePP = basePP;
		this.cat = cat;

		moves.add(this);
	}

	public String getName()
	{
		return name;
	}
	public static MoveInfo getByName(String name)
	{
		for(MoveInfo m : moves)
			if(m.name.equalsIgnoreCase(name))
				return m;
		return null;

	}
	public static MoveInfo[] getList()
	{
		MoveInfo[] m = new MoveInfo[moves.size()]; 
		return moves.toArray(m);			
	}
	public int getMaxPP() {
		return maxPP;
	}
	public int getBasePP() {
		return basePP;
	}
	public boolean isValid() {
		return true;
	}
	public int getPriority() {
		return priority;
	}
	public void hit(Pokemon user, Pokemon target, BattleScreen battle){

		double critBonus;
		if(isCritical()){
			critBonus = getCritDamMod();
			battle.displayMessage("Critical hit!");

		} else {
			critBonus = 1;
		}


		if(target == null || user == null) return;

		double stabBonus = 1;
		for(Type t : types)
			if(user.getTypes().contains(t))
				stabBonus *= 1.5;

		double typeBonus = 1;
		for(Type t : types)
			for(Type t2 : target.getTypes())
				typeBonus *= t.getEffectiveness(t2);

		if(typeBonus > 1.00001 && typeBonus <= 2.00001)
			battle.displayMessage(String.format("It was super effective. (%.2fx)",typeBonus));
		if (typeBonus > 2.00001)
			battle.displayMessage(String.format("Wow! It was incredibly effective!. (%.2fx)",typeBonus));
		if(typeBonus < .99999 && typeBonus > 0.49999)
			battle.displayMessage(String.format("But it wasn't very effective. (%.2fx)",typeBonus));
		if(typeBonus < 0.49999 && typeBonus >= 0.00001)
			battle.displayMessage(String.format("Oh. It was very ineffective. (%.2fx)",typeBonus));

		if(typeBonus < 0.00001)
			battle.displayMessage(String.format("But it had no effect . (%.2fx)",typeBonus));
		
		double mod = stabBonus*typeBonus*critBonus;
		
		if(cat == Category.PHYSICAL){
			int attack = user.getStats()[PokemonInfo.ATTACK];
			int defense = target.getStats()[PokemonInfo.DEFENSE];
			int damage = (int) (((((2.0*user.getLevel()/5+2)*attack*power/defense)/50.0)+2)*mod*(Math.random()*.15 + .85));
			battle.damage(target, damage);

		} else if (cat == Category.SPECIAL){

			int attack = user.getStats()[PokemonInfo.SPECIAL_ATTACK];
			int defense = target.getStats()[PokemonInfo.SPECIAL_DEFENSE];
			int damage = (int) (((((2.0*user.getLevel()/5+2)*attack*power/defense)/50.0)+2)*mod*(Math.random()*.15 + .85));
			battle.damage(target, damage);
		}

	}
	public boolean isCritical(){
		return Util.flip(.0625f * getCritRateMod());
	}

	public void onUse(Pokemon user, Pokemon target, BattleScreen battle){}

	public boolean onHit(Pokemon user, Pokemon target, BattleScreen battle){
		hit(user, target, battle);
		return true;	
	}

	public void onMiss(Pokemon user, Pokemon target2, BattleScreen battle){}

	public boolean doesHit(Pokemon user, Pokemon target, BattleScreen battle){
		return Util.flip(accuracy/100f);
	}

	public float getCritRateMod(){
		return 1;
	}
	
	public float getCritDamMod(){
		return 1.5f;
	}
}
