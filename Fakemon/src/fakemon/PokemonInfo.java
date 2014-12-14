package fakemon;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fakemon.Util.Pair;

public class PokemonInfo {
	
	private static HashSet <PokemonInfo> pokemon = new HashSet <PokemonInfo>();
	
	
	private ArrayList<Type> types;
	public static final int MAX_HP = 0;
	public static final int ATTACK = 1;
	public static final int DEFENSE = 2;
	public static final int SPECIAL_ATTACK = 3;
	public static final int	SPECIAL_DEFENSE = 4;
	public static final int SPEED = 5;

	public final int id;
	public final int[] stats;
	public final String name;
	public final String description;
	public final LevelType levelingType;
	public final int baseExp;
	public final ArrayList<Pair<Integer,MoveInfo>> moveList;
	
	public PokemonInfo(JsonObject def){
		id = def.get("id").getAsInt();
		name = def.get("name").getAsString();
		description = def.get("description").getAsString();
		levelingType = LevelType.getByName(def.get("level type").getAsString());
		baseExp = def.get("base exp").getAsInt();
		stats = new int[6];
		JsonObject statList = def.get("stats").getAsJsonObject();
		stats[0] = statList.get("Health").getAsInt();
		stats[1] = statList.get("Attack").getAsInt();
		stats[2] = statList.get("Defense").getAsInt();
		stats[3] = statList.get("Special Attack").getAsInt();
		stats[4] = statList.get("Special Defense").getAsInt();
		stats[5] = statList.get("Speed").getAsInt();
		types = new ArrayList<Type>();
		JsonArray typeList = def.get("types").getAsJsonArray();
		for(int i = 0; i< typeList.size();i++)
		{
			types.add(Type.getByName(typeList.get(i).getAsString()));
		}
		
		
		moveList = new ArrayList<Pair<Integer,MoveInfo>>();
		
		JsonArray moves = def.get("moves").getAsJsonArray();
		for(int i = 0;i< moves.size();i++){
			int level = ((JsonObject)moves.get(i)).get("level").getAsInt();
			String moveName = ((JsonObject)moves.get(i)).get("name").getAsString();
			MoveInfo move = MoveInfo.getByName(moveName);
			moveList.add(new Pair<Integer,MoveInfo>(level,move));
		}
		
		
		pokemon.add(this);

	}
	/*public PokemonInfo(String data) {
		// Species Name, Pokedex id, HP, Defense, Attack, Special Atk., Special
		// Def., Speed, Leveling Type , Elemental Type(s)
		String[] tokens = data.split(",");
		//if (tokens.length != 9)
		//	throw new IllegalArgumentException("Wrong number of fields in Pokemon Definition.");
		name = tokens[1];
		System.out.println(name);
		try {
			//TODO add other sanity checks
			id = Integer.parseInt(tokens[0]);
			stats = new int[6];
			for(int i = 0;i< 6;i++)
			{
				stats[i] = Integer.parseInt(tokens[i+9]);
				if(stats[i] < 0)
					throw new IllegalArgumentException("Base stat must be > 0 in \"" + name + "\".");
			}
			levelingType = LevelType.getByName(tokens[8].replace('_',' '));
			if(levelingType == null)
				throw new IllegalArgumentException("Invalid Leveling type in \"" + name + "\".");
			this.types = new ArrayList<Type>();
			
			//String[] typeList = tokens[2].split("(\\\\|/)");
			String[] typeList = {tokens[2],tokens[3]};
			for(String t:typeList)
				if(!t.equalsIgnoreCase("None"))
					types.add(Type.getByName(t));
			
			baseExp = Integer.parseInt(tokens[16]);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid field value in \"" + name + "\".");
		}
		description = "";
		pokemon.add(this);
	}*/
	public int[] getStatsForLevel(int level, Pokemon p){
		
		int[] newStats = new int[6];
		
		newStats[MAX_HP] = (int) ((p.ivs[MAX_HP] + (2 * stats[MAX_HP]) + p.evs[MAX_HP]/4 +100) * level / 100.0 + 10);
		for(int i = 1; i < 6;i++)
		{
			newStats[i] = (int) (((p.ivs[i] + (2 * stats[i]) + p.evs[i]/4.0) * level / 100.0 + 5) * p.getNature().getMod(i));
		}
		return newStats;
	}
	public ArrayList<Type> getTypes()
	{
		return types;
	}
	public static PokemonInfo getByName(String name)
	{
		for(PokemonInfo p : pokemon)
			if(p.name.equalsIgnoreCase(name))
				return p;
		return null;
			
	}
	public static PokemonInfo[] getList()
	{
		PokemonInfo[] t = new PokemonInfo[pokemon.size()]; 
		return pokemon.toArray(t);			
	}
}
