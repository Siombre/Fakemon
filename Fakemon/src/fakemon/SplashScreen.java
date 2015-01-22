package fakemon;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import tiles.GrassTile;
import tiles.StoneTile;
import tiles.TallGrass;

public class SplashScreen extends Screen {

	private static Texture texture;

	private static int PRE = -1;
	private static int RUNNING  = 0;
	private static int FINISHING = 1;
	private static int FINISHED = 2;

	private long start = 0;
	private long runTime;
	private int state = PRE;
	public SplashScreen(){
		super.init();
		runTime = 3000;
		
	}
	@Override
	public void processMouseEvent(double x, double y) {
		//state = FINISHED;
	}

	@Override
	public void render(int delta) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();

		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/world/tallGrass.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		Color.white.bind();

		texture.bind(); 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(0,0);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(1,0);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(1,1);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0,1);
		GL11.glEnd();

		GL11.glPopMatrix();  
		int scale = 100;
		//GL11.glScalef(scale, scale, 1);

		GL11.glPushMatrix();
	//	new GrassTile().render();
		//new TallGrass().render();

		GL11.glPopMatrix();
	}

	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic() {
		if(state == PRE)
		{
			start = System.currentTimeMillis();
			state = RUNNING;
		}else if(state == RUNNING){
			if(System.currentTimeMillis() - start > runTime)
				state = FINISHING;
		}else if(state == FINISHING){
			Fakemon.pushScreen(new FadeTransitionScreen(new MainMenuScreen()));
			state = FINISHED;
		}		
	}

	@Override
	public boolean isFinished() {
		return state == FINISHED;
	}

}
