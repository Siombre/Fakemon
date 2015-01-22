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
		pushScreen(new BlankScreen());
		pushScreen(new SplashScreen());
		while(true)
		{
			getCurrentScreen().doLogic();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			GL11.glPushMatrix();
			GL11.glTranslatef((getCurrentScreen().width-getCurrentScreen().height)/2f,0,0);
			GL11.glScaled(getCurrentScreen().height, getCurrentScreen().height, 1);
			getCurrentScreen().renderScreen(delta);
			GL11.glPopMatrix();
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
		return screenStack.get(screenStack.size()-1);
	}

	/*public static void setCurrentScreen(Screen screen) {
		screenStack.set(screenStack.size()-1,screen);
	}*/
	public static void pushScreen(Screen screen){
		screenStack.add(screen);
	}
	public static void popScreen(){
		screenStack.remove(screenStack.size()-1);
	}
	public static Screen peek(int rIndex){
		return screenStack.get(screenStack.size()-1 + rIndex);
	}
	public static void printScreenStack(){
		String s = "";
		for(Screen sc : screenStack){
			s += sc.getClass().getSimpleName() + " ";
		}
		System.out.println(s);
	}
}
