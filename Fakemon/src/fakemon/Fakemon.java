package fakemon;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.Font;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.TrueTypeFont;


public class Fakemon {
	private static Screen currentScreen;
	static TrueTypeFont font;
	static TrueTypeFont smallFont;
	private boolean started;
	public Fakemon(){
		try {
			init();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	public void start() throws LWJGLException{
		if(this.started) return;
		started = true;
		setCurrentScreen(new BlankScreen());
		PokemonInfo[] pokedex = PokemonInfo.getList();
		System.out.println(pokedex.length + " Pokemon loaded.");
		
		MoveInfo[] moves = MoveInfo.getList();
		System.out.println(moves.length + " Moves loaded.");
		
		Trainer you = new Trainer("Player");
		you.addPokemon(generatePokemon(10));
		you.battleAI = new PlayerAI();
		
		//setCurrentScreen(new OverworldScreen(you));

		//while(!currentScreen.isFinished());
		
		
		
		Trainer[] t = { you, null};
		Trainer enemy;
		
		int[] is = { 1 , 1 };
		int win = 1;
		
		while(true){
			if(win != 0)
				you.getPokemon()[0].fullHeal();
			enemy = new Trainer("Opponent");
			enemy.addPokemon(generatePokemon(10));
			enemy.addPokemon(generatePokemon(10));

			t[1] = enemy ;
			BattleScreen s = new BattleScreen(t, false, is);
			FadeTransitionScreen ts = new FadeTransitionScreen(s);
			setCurrentScreen(ts);
			while(!s.isFinished())
			{
				getCurrentScreen().doLogic();
			}
			win = s.getWinner();


			
			
		}
	}
	
	public static Pokemon generatePokemon(int level){
		Random rand = new Random();

		PokemonInfo[] pokedex = PokemonInfo.getList();
		MoveInfo[] moves = MoveInfo.getList();
		PokemonInfo s = pokedex[rand.nextInt(pokedex.length)];
		Pokemon p = new Pokemon(s.name, s, s.levelingType.getExperience(level), level, false, -1);
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));
		p.addMove(new Move(moves[rand.nextInt(moves.length)]));
		//p.addMove(new Move(MoveInfo.getByName("Antibodies")));

		return p;
	}
	
	public void render(int delta){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

		if(currentScreen != null){
			
			currentScreen.render(delta);
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

	public static Screen getCurrentScreen() {
		return currentScreen;
	}

	public static void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}
}
