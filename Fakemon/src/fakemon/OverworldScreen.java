package fakemon;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import tiles.FlowerTile;
import tiles.GrassTile;
import tiles.HealTile;
import tiles.StoneTile;
import tiles.TallGrass;
import tiles.Tile;
import tiles.TreeTile;

public class OverworldScreen extends Screen{

	private Texture texture;
	private Tile[][][] map;
	int mapNum;
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
	private void loadMap(String name){

		char[][] mapData = null;
		File f = new File(Start.getPath("res/maps/"+name+".txt"));
		try {
			Scanner s = new Scanner(f);
			int maxLen = 0;
			ArrayList<String> lines = new ArrayList<String>();
			while(s.hasNextLine())
			{
				String st = s.nextLine();
				if(st.length()>maxLen)
					maxLen = st.length();
				lines.add(st);
			}
			mapData = new char[maxLen][lines.size()];
			for(int i = 0; i < lines.size();i++)
			{
				String st = lines.get(i);
				for(int i2 = 0; i2< st.length();i2++)
				{
					mapData[i2][i] = st.charAt(i2);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		map = new Tile[3][mapData.length][mapData[0].length];

		for(int i = 0; i<mapData.length;i++)
			for(int i2 = 0; i2<mapData[i].length;i2++)
			{

				map[0][i][i2] = new GrassTile();
				switch (mapData[i][mapData[i].length-i2-1]){
				case 't':
					map[1][i][i2] = new TreeTile();
					break;
				case 'g':
					map[1][i][i2] = new TallGrass();
					break;
				case 'f':
					map[1][i][i2] = new FlowerTile();
					break;
				case 'h':
					map[0][i][i2] = new HealTile();
					break;
				case 's':
					map[0][i][i2] = new StoneTile();
					break;
				}
			}
	}
	public void init(){
		super.init();
		y=5;
		x=5;
		loadMap("map1");
	}
	@Override
	public void processMouseEvent(double x, double y) {

	}

	@Override
	public void render(int delta) {
		Tile[][][] map = this.map;
		animTime += delta;
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/trainer/Trainer.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if(Keyboard.isKeyDown(Keyboard.KEY_1) && mapNum != 1)
		{
			loadMap("map1");
			y=5;
			x=5;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2) && mapNum != 2)
		{
			loadMap("map2");
			y=5;
			x=5;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3) && mapNum != 3)
		{
			loadMap("map3");
			y=10;
			x=10;
		}
		boolean moving = false;
		Rectangle2D bounds = new Rectangle();
		double d = 4*delta/1000f;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			y += d;
			direction = 0;
			bounds.setFrame(x, y, 1, 1);
			if(doesCollide(bounds)){
				y = (int) y;
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			y -= d;
			direction = 1;
			bounds.setFrame(x, y, 1, 1);
			if(doesCollide(bounds)){
				y = (int) (y+.75);
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			x += d;
			direction = 3;
			bounds.setFrame(x, y, 1, 1);
			if(doesCollide(bounds)){
				x = (int) x;
			}else
				moving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			x -= d;
			direction = 2;
			bounds.setFrame(x, y, 1, 1);
			if(doesCollide(bounds)){
				x = (int) (x+.75);
			}else 
				moving = true;
		}

		if(!moving)
			animTime = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			scale *= 1 + delta/1000f;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			scale /= 1 + delta/1000f;
	
		for(int l = 0; l< map.length;l++){
			if(map[l][(int)(x+.5)][(int)(y+.5)]!= null)
				map[l][(int)(x+.5)][(int)(y+.5)].onStep(anchor, delta);
		}
		totalTime+=delta;


		GL11.glPushMatrix();
		GL11.glTranslated(.5,.5, 0);
		// TODO Auto-generated method stub

		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, 1);		
		GL11.glTranslated(-x-.5,y-.5, 0);

		GL11.glPushMatrix();
		for(int l = 0; l< map.length;l++){
			GL11.glTranslatef(0, -map[l].length, 0);
			for(int i = map[l][0].length-1; i>=0;i--)
			{
				GL11.glTranslatef(map[l][0].length, 1, 0);

				for(int i2 = map[l].length-1; i2>= 0;i2--)
				{
					GL11.glTranslatef(-1, 0, 0);

					if(map[l][i2][i] != null){
						GL11.glPushMatrix();
						map[l][i2][i].render(delta,i2,i,l);
						GL11.glPopMatrix();
					}
				}
			}
		}
		GL11.glPopMatrix();

		double texX = (animTime%500)/125/4.0;
		double texY = direction/4.0;
		double texX2 = texX +.25;
		double texY2 = texY +.25;
		double over = 0;

		double[] player = {x-over,-y,x+1+over,-y+1+over+over,texX,texY,texX2,texY2,1,texture.getTextureID()};
		RenderManager.register(player);
		RenderManager.render();
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();

		texture.bind();
		drawString( Fakemon.smallFont,.23f,.0265f, "(" + x+','+y+')', Color.gray);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		int err;
		if((err = GL11.glGetError()) != 0)
			System.err.println("####### GL Error : "+err+ " #######");


	}
	public boolean doesCollide(Rectangle2D r){
		for(int x = (int)r.getMinX(); x <= (int)(r.getMaxX());x++)
			for(int y = (int)r.getMinY(); y <= (int)r.getMaxY();y++)
				for(int l = 0; l< map.length;l++){
					if(map[l][x][y] != null && !map[l][x][y].isPassable())
					{
						Rectangle r2 = new Rectangle(x,y,1,1);
						if(r.intersects(r2))
							return true;
					}
				}
		return false;
	}
	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic(int delta) {}
	@Override
	public int getLogicDelay() {
		return 100;
	}
	public boolean isFinished(){
		return false;
	}


}
