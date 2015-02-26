package fakemon;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Trainer extends Entity{	
	private ArrayList<Item> bag = new ArrayList<Item>();
	private Pokemon[] pokemon = new Pokemon[6];
	private String name;
	private int id;
	BattleAI battleAI;
	OverworldAI overAI;
	Texture texture;
	
	public Trainer(String name) {
		super();
		this.name = name;
		battleAI = new TrainerAI(this);
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/trainer/Trainer.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		super.setTextureID(texture.getTextureID());
	}

	public boolean addPokemon(Pokemon p) {
		for (int i = 0; i < pokemon.length; i++) {
			if (pokemon[i] == null) {
				pokemon[i] = p;
				return true;
			}
		}
		return false;
	}

	public Pokemon[] getPokemon() {
		return pokemon;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		for (Pokemon p : pokemon)
			if (p != null && p.getHealth() > 0)
				return true;
		return false;
	}

	public BattleAI getBattleAI() {
		return battleAI;
	}
	public int getID(){
		return id;
	}
}
