package fakemon;

import java.awt.Font;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.TrueTypeFont;


public class Fakemon {
	Screen currentScreen;
	static TrueTypeFont font;
	static TrueTypeFont smallFont;
	private boolean started;
	public void start(){
		if(this.started) return;
		started = true;
		
		PokemonInfo[] pokedex = PokemonInfo.getList();
		System.out.println(pokedex.length + " Pokemon loaded.");
		MoveInfo[] moves = MoveInfo.getList();
		System.out.println(moves.length + " Moves loaded.");

		Trainer enemy = new Trainer("Shut up, Fool");
		enemy.addPokemon(generatePokemon(10));

		Trainer you = new Trainer("Sully");
		you.addPokemon(generatePokemon(100));
		you.battleAI = new PlayerAI();
		Trainer[] t = { you, enemy };
		int[] is = { 1 , 1 };
		you.getPokemon()[0].damage(50);
		currentScreen =  new BattleScreen(t, false, is);
		int win = currentScreen.start();
		
		while(true){
			if(win != 0)
				you.getPokemon()[0].fullHeal();
			System.out.println(you.getPokemon()[0].getHealth());
			enemy = new Trainer("Shut up, Fool");
			enemy.addPokemon(generatePokemon(10));
			t[1] = enemy ;
			currentScreen =  new BattleScreen(t, false, is);
			win = currentScreen.start();
		}
	}
	
	public static Pokemon generatePokemon(int level){
		Random rand = new Random();

		PokemonInfo[] pokedex = PokemonInfo.getList();
		MoveInfo[] moves = MoveInfo.getList();
		PokemonInfo s = pokedex[rand.nextInt(pokedex.length)];
		Pokemon p = new Pokemon(s.name, s, s.levelingType.getExperience(level)-1, level-1, false, -1);
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));

		return p;
	}
	
	public void render(){
		if(currentScreen != null){
			currentScreen.render();
		}
	}

	public void mouseEvent() {
		if(currentScreen != null){
			currentScreen.mouseEvent();
		}
	}

	public void init() throws LWJGLException {
		Screen.initGL();
		
		Font awtFont = new Font("Times New Roman", Font.BOLD, 18); // name, style (PLAIN, BOLD, or ITALIC), size
		font = new TrueTypeFont(awtFont, true); // base Font, anti-aliasing true/false
		
		Font awtFont2 = new Font("Times New Roman", Font.BOLD, 12); // name, style (PLAIN, BOLD, or ITALIC), size
		smallFont = new TrueTypeFont(awtFont2, true);
	}
}
