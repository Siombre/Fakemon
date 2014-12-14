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

		if(texture == null) //TODO Replace with texture manager
			try {
				// load texture from PNG file
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/trainer/Trainer.png"));

				System.out.println("Texture loaded: "+texture);
				System.out.println(">> Image width: "+texture.getImageWidth());
				System.out.println(">> Image height: "+texture.getImageHeight());
				System.out.println(">> Texture width: "+texture.getTextureWidth());
				System.out.println(">> Texture height: "+texture.getTextureHeight());
				System.out.println(">> Texture ID: "+texture.getTextureID());

			} catch (IOException e) {
				e.printStackTrace();
			}
		GL11.glPushMatrix();
		GL11.glTranslated(width/2f,height/2f, 0);
		//GL11.glTranslated(10,10, 0);

		GL11.glScaled(height, height, 1);
		//GL11.glTranslated(.5,.5, 0);

		GL11.glPushMatrix();
		//GL11.glRotated(1, 1, 0, 0);
		GL11.glScalef(scale, scale, 1);
		GL11.glTranslated(x, y, 0);
		//	GL11.glRotated(-Math.PI, 1, 0, 0);
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

		/*Color.white.bind();
		texture.bind(); // or GL11.glBind(texture.getTextureID());

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(100,100);
		GL11.glTexCoord2f(124f/128,0);
		GL11.glVertex2f(100+texture.getTextureWidth(),100);
		GL11.glTexCoord2f(124f/128,254/256f);
		GL11.glVertex2f(100+texture.getTextureWidth(),100+texture.getTextureHeight());
		GL11.glTexCoord2f(0,254/256f);
		GL11.glVertex2f(100,100+texture.getTextureHeight());
		GL11.glEnd();*/
	}
	@Override
	public void displayMessage(String s) {

	}

	@Override
	public void doLogic() {

	}
	public boolean isFinished(){
		return false;
	}

}
