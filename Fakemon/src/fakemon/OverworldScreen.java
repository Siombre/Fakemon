package fakemon;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import tiles.GrassTile;
import tiles.HealTile;
import tiles.StoneTile;
import tiles.TallGrass;
import tiles.Tile;

public class OverworldScreen extends Screen{

	private Texture texture;
	private Tile[][][] map;
	float x,y;
	int totalTime;
	Trainer anchor;
	double scale = 1f/32;
	int direction;
	int animTime;
	public OverworldScreen(Trainer t){
		init();
		anchor = t;
	}
	public void init(){
		super.init();
		y=5;
		x=5;
		
		map = new Tile[3][10][10];

		for(int i = 0; i<map[0].length;i++)
			for(int i2 = 0; i2<map[0][0].length;i2++)
			{

				if(i==0 || i == map[0].length-1 ||i2 ==0 ||i2 == map[0][0].length-1)
					map[0][i][i2] = new StoneTile();
				else 
				{
					map[0][i][i2] = new GrassTile();
					if(i< 7 && i>2 &&i2>2 && i2<7)
						map[1][i][i2] = new TallGrass();

				}
			}
		map[0][2][2] = new HealTile();
	}
	@Override
	public void processMouseEvent(double x, double y) {
		
	}

	@Override
	public void render(int delta) {
		animTime += delta;
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/trainer/Trainer.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		boolean moving = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			y += .05;
			direction = 0;
			if(!map[0][(int) x][(int) (y+1)].isPassable()){
				y = (int) y;
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			y -= .05;
			direction = 1;
			if(!map[0][(int) x][(int) (y)].isPassable()){
				y = (int) (y+.75);
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			x += .05;
			direction = 3;
			if(!map[0][(int) (x+1)][(int) (y)].isPassable()){
				x = (int) x;
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			x -= .05;
			direction = 2;
			if(!map[0][(int) x][(int) y].isPassable()){
				x = (int) (x+.75);
			}else 
				moving = true;
		}
		if(!moving)
			animTime = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			scale *= 1.05;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			scale /= 1.05;
		
		totalTime+=delta;
		
		for(int l = 0; l< map.length;l++){
			if(map[l][(int)(x+.5)][(int)(y+.5)]!= null)
				map[l][(int)(x+.5)][(int)(y+.5)].onStep(anchor);
		}		
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5,.5, 0);
		
		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, 1);		
		GL11.glTranslated(-x-.5,y-.5, 0);
		

		for(int l = 0; l< map.length;l++){

			for(int i = 0; i<map[l].length;i++)
			{
				for(int i2 = 0; i2<map[l][0].length;i2++)
				{
					if(map[l][i][i2] != null){
						GL11.glPushMatrix();
						map[l][i][i2].render(delta);
						GL11.glPopMatrix();
					}
					GL11.glTranslatef(1, 0, 0);
				}
				GL11.glTranslatef(-map[l][0].length, -1, 0);
			}
			GL11.glTranslatef(0, map[l].length, 0);
		}

		GL11.glPushMatrix();
		GL11.glTranslated(x, -y, 0);
		
		double texX = (animTime%500)/125/4.0;
		double texY = direction/4.0;
		double texX2 = texX +.25;
		double texY2 = texY +.25;
		double size = 1.25;
		
		Color.white.bind();
		texture.bind(); 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(texX,texY);
		GL11.glVertex3d(1-size,2-size-size,0);
		GL11.glTexCoord2d(texX2,texY);
		GL11.glVertex3d(size,2-size-size,0);
		GL11.glTexCoord2d(texX2,texY2);
		GL11.glVertex3d(size,1,0);
		GL11.glTexCoord2d(texX,texY2);
		GL11.glVertex3d(1-size,1,0);
		GL11.glEnd();
		
		GL11.glPopMatrix();

		GL11.glPopMatrix();
		GL11.glPopMatrix();
		drawString( Fakemon.smallFont,.23f,.0265f, "(" + x+','+y+')', Color.gray);
		GL11.glDisable(GL11.GL_TEXTURE_2D);


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
