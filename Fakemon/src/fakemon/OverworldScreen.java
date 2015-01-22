package fakemon;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import tiles.GrassTile;
import tiles.StoneTile;
import tiles.TallGrass;
import tiles.Tile;

public class OverworldScreen extends Screen{

	private Texture texture;
	private Tile[][][] map;
	float x,y;
	int totalTime;
	Trainer anchor;
	float scale = 1f/32;
	
	public OverworldScreen(Trainer t){
		init();
		anchor = t;
	}
	public void init(){
		super.init();
		y=10;
		x=-10;
		map = new Tile[3][40][40];

		for(int i = 0; i<map[0].length;i++)
			for(int i2 = 0; i2<map[0][0].length;i2++)
			{

				if(i==0 || i == map[0].length-1 ||i2 ==0 ||i2 == map[0][0].length-1)
					map[0][i][i2] = new StoneTile();
				else 
				{
					map[0][i][i2] = new GrassTile();
					if(i< 10)
						map[1][i][i2] = new TallGrass();

				}
			}
		map[0][0][0] = new GrassTile();
	}
	@Override
	public void processMouseEvent(double x, double y) {
		
	}

	@Override
	public void render(int delta) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		totalTime+=delta;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			y += .05;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			y -= .05;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			x += .05;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			x -= .05;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			scale *= 1.05;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			scale /= 1.05;
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5,.5, 0);

		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 1);
		GL11.glTranslated(x, y, 0);
		for(int l = 0; l< map.length;l++){

			for(int i = 0; i<map[l].length;i++)
			{
				for(int i2 = 0; i2<map[l][0].length;i2++)
				{
					if(map[l][i][i2] != null){
						GL11.glPushMatrix();
						map[l][i][i2].render();
						GL11.glPopMatrix();
					}
					GL11.glTranslatef(1, 0, 0);
				}
				GL11.glTranslatef(-map[l][0].length, -1, 0);
			}
			GL11.glTranslatef(0, map[l].length, 0);
		}


		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic() {
		
	}
	public boolean isFinished(){
		return false;
	}

}
