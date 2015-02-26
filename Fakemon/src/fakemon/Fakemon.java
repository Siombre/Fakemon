package fakemon;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;


public class Fakemon {
	private static ArrayList<Screen> screenStack;
	static TrueTypeFont font;
	static TrueTypeFont smallFont;
	private boolean started;
	private boolean stopRequested;
	public Fakemon(){
		try {
			init();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	public void start(){
		if(this.started) return;
		started = true;
		pushScreen(new BlankScreen());
		pushScreen(new MainMenuScreen());
		//	pushScreen(new OverworldScreen(null));
		long time;
		int dT = 0;
		while(!stopRequested)
		{
			time = System.currentTimeMillis();
			long delay = 100;

			if(getCurrentScreen() != null)
			{
				getCurrentScreen().doLogic(dT);
				delay = getCurrentScreen().getLogicDelay();
			} 
			delay -= System.currentTimeMillis() - time;
			if(delay > 0)
			{
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			dT = (int) (System.currentTimeMillis() - time);
		}
	}

	public static Pokemon generatePokemon(int level){
		Random rand = new Random();

		PokemonInfo[] pokedex = PokemonInfo.getList();
		PokemonInfo s = pokedex[rand.nextInt(pokedex.length)];
		Pokemon p = new Pokemon(s.name, s, s.levelingType.getExperience(level), level, false, -1);
		return p;
	}

	public void render(int delta){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

		if(getCurrentScreen() != null){

			getCurrentScreen().renderScreen(delta);
		}
	}

	public void mouseEvent() {
		if(getCurrentScreen() != null){
			getCurrentScreen().mouseEvent();
		}
	}

	public void init() throws LWJGLException {
		Screen.initGL();

		Font awtFont = new Font("Times New Roman", Font.BOLD, 18); // name, style (PLAIN, BOLD, or ITALIC), size
		font = new TrueTypeFont(awtFont, true); // base Font, anti-aliasing true/false

		Font awtFont2 = new Font("Times New Roman", Font.BOLD, 12); // name, style (PLAIN, BOLD, or ITALIC), size
		smallFont = new TrueTypeFont(awtFont2, true);

		screenStack = new ArrayList<Screen>();
	}

	public static Screen getCurrentScreen() {
		if(screenStack.size() <= 0) return null;
		return screenStack.get(screenStack.size()-1);
	}
	public static void pushScreen(Screen screen){
		screenStack.add(screen);
	}
	public static void popScreen(){
		if(screenStack.size() > 0)
			screenStack.remove(screenStack.size()-1);
	}
	public static Screen peek(int rIndex){
		if(rIndex + screenStack.size() <= 0) return null;
		return screenStack.get(screenStack.size()-1 + rIndex);
	}
	public static void printScreenStack(){
		String s = "";
		for(Screen sc : screenStack){
			s += sc.getClass().getSimpleName() + " ";
		}
		System.out.println(s);
	}
	public void requestStop(){
		stopRequested = true;
	}
}
