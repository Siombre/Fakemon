package fakemon;
import java.util.ArrayList;
import java.util.Random;

import effects.Effect;
import fakemon.Util.Pair;

public class Pokemon {
	private int[] stats;
	public int[] evs;
	public int[] ivs;

	private String name;
	private ArrayList<Effect> statEffects;
	private PokemonInfo info;
	private int exp;
	private int level;
	private boolean shiny;
	private Move[] moves;
	private int hp;
	private Nature nature;

	public Pokemon(String name, PokemonInfo info, int exp, int level, boolean shiny, int hp) {
		evs = new int[6];
		ivs = new int[6];
		Random rand = new Random();
		for(int i = 0; i< ivs.length;i++)
		{
			ivs[i] = rand.nextInt(32);
		}
		nature = Nature.getByName("default");

		this.stats = info.getStatsForLevel(level, this);

		this.name = name;
		this.info = info;
		this.exp = exp;

		this.level = level;
		

		
		this.shiny = shiny;

		if (hp < 0)
			this.hp = stats[PokemonInfo.MAX_HP];
		else
			this.hp = hp;

		moves = new Move[4];
		for(Pair<Integer,MoveInfo> p: info.moveList){
			if(p.getFirst() <= level)
				addMove(new Move(p.getSecond()));
		}
		statEffects = new ArrayList<Effect>();
	}
	
	public boolean addEffect(Effect statEffect,Pokemon user,Screen screen) {
		if(!statEffect.canBeApplied(this, screen))
			return false;
		for(Effect e : statEffects){
			if(e.prevents(statEffect, screen))
				return false;
		}
		for(Effect e : statEffects){
			if(e.add(e, screen))
				return true;
		}
		statEffects.add(statEffect);
		statEffect.onNewApply(screen,user,this);
		return true;
	}

	public boolean addMove(Move m) {
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] == null) {
				moves[i] = m;
				return true;
			}
		}
		return false;
	}

	public Move[] getMoves() {
		return moves;
	}

	public void levelUp() {
		level++;
		for(Pair<Integer,MoveInfo> p: info.moveList){
			if(p.getFirst() == level)
				addMove(new Move(p.getSecond()));
		}
		double healthRatio = (double)hp/stats[PokemonInfo.MAX_HP];
		stats = info.getStatsForLevel(level, this);
		
		hp = (int) (healthRatio * stats[PokemonInfo.MAX_HP]+.5);		
	}

	public void addExp(int amt) {
		exp += amt;
		while(checkLevel());
	}
	public int getHealth() {
		return hp;
	}

	public void damage(int damage) {
		hp -= damage;

		if (hp < 0)
			hp = 0;

		if (hp > stats[PokemonInfo.MAX_HP])
			hp = stats[PokemonInfo.MAX_HP];
	}

	public int getLevel() {
		return level;
	}

	public boolean checkLevel() {
		if (level >= 100)
			return false;
		if (exp > info.levelingType.getExperience(level + 1))
		{
			levelUp();
			return true;
		}
		return false;
	}
	public void fullHeal(){
		for(Move m: moves)
		{
			if(m!= null)
				m.setCurPP(m.getMaxPP());
		}
		hp = stats[PokemonInfo.MAX_HP];
		statEffects.clear();
	}
	public String getName() {
		return name;
	}

	public Nature getNature() {
		return nature;
	}

	public ArrayList<Type> getTypes() {
		return info.getTypes();
	}
	public PokemonInfo getInfo(){
		return info;
	}
	public float getStat(int stat){
		float mod = 1;
		for(Effect e : statEffects)
			mod *= e.getStatMod(stat);
		if(stat < 6)
			return (int)(stats[stat] * mod);
		else
			return mod;
	}
	public void checkEffects(){
		ArrayList<Effect> removed = new ArrayList<Effect>();
		for(Effect e : statEffects)
			if(e.isOver())
				removed.add(e);
		statEffects.removeAll(removed);
	}
	public int getBaseStat(int stat){
		return stats[stat];
	}
	public boolean isShiny(){
		return shiny;
	}

	public boolean canAttack(Screen screen, Move m) {
		boolean canAttack = true;
		for(Effect e : statEffects)
			if(!e.canAttack(screen,m))
				canAttack = false;
		return canAttack;
	}
	public void onTurnEnd(BattleScreen screen){
		for(Effect e : statEffects)
			e.onTurnEnd(screen);
		 checkEffects();
	}
	public void onTurnStart(BattleScreen screen){
		for(Effect e : statEffects)
			e.onTurnStart(screen);
		 checkEffects();
	}
	public float getDamMod()
	{
		float mod = 1;
		for(Effect e : statEffects)
			mod *= e.getDamMod();
		return mod;
	}
}
