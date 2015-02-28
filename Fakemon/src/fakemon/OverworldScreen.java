package fakemon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import tiles.FlowerTile;
import tiles.GrassTile;
import tiles.HealTile;
import tiles.StoneTile;
import tiles.TallGrass;
import tiles.Tile;
import tiles.TreeTile;

public class OverworldScreen extends Screen{

	private World world;
	int mapNum;
	int totalTime;
	Trainer anchor;
	double scale = 1f/8;
	
	
	public OverworldScreen(Trainer t){
		anchor = t;
		init();
	}
	private World loadMap(String name){

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
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Tile[][][] map = new Tile[3][mapData.length][mapData[0].length];

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
		return new World(map);
	}
	public void init(){
		super.init();
		double y=5;
		double x=5;
		world = loadMap("map1");
		world.addEntity(anchor, x, y);
	}
	@Override
	public void processMouseEvent(double x, double y) {

	}

	@Override
	public void render(int delta) {
		
		frameCopy(Entity.PREV,Entity.RENDER);
		double x = anchor.getX(Entity.RENDER);
		double y = anchor.getY(Entity.RENDER);

		World world = this.world;
		GL11.glEnable(GL11.GL_TEXTURE_2D);


		GL11.glPushMatrix();
		GL11.glTranslated(.5,.5, 0);

		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, 1);		
		GL11.glTranslated(-x-.5,y-.5, 0);

		GL11.glPushMatrix();
		int drawSize = 10;
		int midX = (int) x;
		int midY = (int) y;
		Tile t;
		for(int l = 0; l < 3;l++){
			GL11.glTranslatef(0, -2*drawSize, 0);
			for(int i = midY-drawSize; i<midY+drawSize;i++)
			{
				GL11.glTranslatef(2*drawSize, 1, 0);

				for(int i2 = midX-drawSize; i2< midX+drawSize;i2++)
				{
					GL11.glTranslatef(-1, 0, 0);

					if((t=world.getTile(i2, i, l)) != null){
						GL11.glPushMatrix();
						t.render(delta,i2,i,l);
						GL11.glPopMatrix();
					}
				}
			}
		}
		GL11.glPopMatrix();
		for(Entity e : world.getEntityList())
		{
			e.render();
		}
		
		RenderManager.render();
		GL11.glPopMatrix();
		
		GL11.glPopMatrix();

		drawString( Fakemon.smallFont,.23f,.0265f, "(" + x+','+y+')', Color.gray);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		int err;
		if((err = GL11.glGetError()) != 0)
			System.err.println("####### GL Error : "+err+ " #######");


	}
	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic(int delta) {

		if(Keyboard.isKeyDown(Keyboard.KEY_1) && mapNum != 1)
		{
			world.removeEntity(anchor);
			world = loadMap("map1");
			world.addEntity(anchor, 5, 5);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_2) && mapNum != 2)
		{
			world.removeEntity(anchor);
			world = loadMap("map2");
			world.addEntity(anchor, 5, 5);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_3) && mapNum != 3)
		{
			world.removeEntity(anchor);
			world = loadMap("map3");
			world.addEntity(anchor, 10, 10);
		}
		
		double d = 4/1000f;
		double vx = 0, vy = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			vy += d;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			vy -= d;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			vx += d;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			vx -= d;
		}
		anchor.setVelocity(vx, vy);

		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			scale *= 1 + delta/1000f;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			scale /= 1 + delta/1000f;
		
		for(int l = 0; l< 3;l++){
			if(world.getTile((int)(anchor.getX(Entity.PREV)+.5),(int)(anchor.getY(Entity.PREV)+.5),l)!= null)
				world.getTile((int)(anchor.getX(Entity.PREV)+.5),(int)(anchor.getY(Entity.PREV)+.5),l).onStep(anchor, delta);
		}
		for(Entity e : world.getEntityList())
		{
			e.tick(delta);
		}
		totalTime+=delta;
		
		
		frameCopy(Entity.CURR,Entity.PREV);
	}
	@Override
	public int getLogicDelay() {
		return 5;
	}
	public boolean isFinished(){
		return false;
	}

	private synchronized void frameCopy(int a, int b){
		ArrayList<Entity> entities = world.getEntityList();
		for(int i = 0;i<entities.size();i++)
		{
			entities.get(i).frameCopy(a, b);
		}
	}
}
