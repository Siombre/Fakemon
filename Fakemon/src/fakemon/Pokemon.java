package fakemon;
import java.util.ArrayList;
import java.util.Random;

public class Pokemon {
	public int[] stats;
	public int[] evs;
	public int[] ivs;

	private String name;
	private Status statEffect;
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

	}

	public boolean setStatusEffect(Status statEffect) {
		return false;
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
		double healthRatio = (double)hp/stats[PokemonInfo.MAX_HP];
		stats = info.getStatsForLevel(level, this);
		
		hp = (int) (healthRatio * stats[PokemonInfo.MAX_HP]+.5);		
	}

	public void addExp(int amt) {
		exp += amt;
		while(checkLevel());
	}

	public int[] getStats() {
		return stats;
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
		statEffect = null;
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
}
