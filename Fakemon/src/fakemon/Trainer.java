package fakemon;
import java.util.ArrayList;


public class Trainer {
	private float x,y;
	private ArrayList<Item> bag = new ArrayList<Item>();
	private Pokemon[] pokemon = new Pokemon[6];
	private String name;
	private int id;
	BattleAI battleAI;
	public Trainer(String name)
	{
		this.name = name;
		battleAI = new TrainerAI(this);
	}
	public boolean addPokemon(Pokemon p)
	{
		for(int i = 0;i< pokemon.length;i++)
		{
			if(pokemon[i] == null)
			{
				pokemon[i] = p;
				return true;
			}
		}
		return false;
	}
	public Pokemon[] getPokemon()
	{
		return pokemon;
	}
	public String getName()
	{
		return name;
	}
	public boolean isAlive(){
		for(Pokemon p:pokemon)
			if(p != null && p.getHealth() > 0)
				return true;
		return false;
	}
	public BattleAI getBattleAI(){
		return battleAI;
	}
}
