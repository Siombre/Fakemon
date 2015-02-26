package fakemon;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.newdawn.slick.opengl.Texture;

public abstract class Entity {
	private double[] vx;
	private double[] vy;
	private double[] x,y;
	private double[] facing;
	private int[] animTime;
	private int[] animNum;
	private double height = 1, width = 1;
	private int textureID;
	private World world;
	public final static int CURR = 0;
	public final static int PREV = 1;
	public final static int RENDER = 2;
	
	/**
	 * Advances all actions of this entity, including movement.
	 * 
	 * @param delta The time passed since the last logic frame, in milliseconds.
	 */
	public Entity(){
		x = new double[3];
		y = new double[3];
		vx = new double[3];
		vy = new double[3];
		facing = new double[3];
		animTime = new int[3];
		animNum = new int[3];

	}
	
	public void tick(int delta){
		
		x[CURR] += vx[CURR]*delta;
		
		Rectangle2D col = world.getCollide(this);
		if(col != null)
		{
			if(vx[CURR] > 0)
			{
				x[CURR] = col.getMinX()-width;
			}else{
				x[CURR] = col.getMaxX();
			}
		}
		
		y[CURR] += vy[CURR]*delta;
		col = world.getCollide(this);
		if(col != null)
		{
			if(vy[CURR] > 0)
			{
				y[CURR] = col.getMinY()-height;
			}else{
				y[CURR] = col.getMaxY();
			}
		}
		
		if(y[CURR]!=y[PREV] || x[CURR]!= x[PREV])
			animTime[CURR] += delta;
		else
			animTime[CURR] = 0;
		
		
		
		if(vx[CURR] > .001)
			animNum[CURR] = 3;
		else if(vx[CURR] < -.001)
			animNum[CURR] = 2;
		else if(vy[CURR] > .001)
			animNum[CURR] = 0;
		else if(vy[CURR] < -.001)
			animNum[CURR] = 1;
	}
	
	/**
	 * Inserts the Entity into a world.
	 * 
	 */
	public void put(World w, double x, double y){
		world = w;
		this.x[CURR] = x;
		this.y[CURR] = y;
		this.x[PREV] = x;
		this.y[PREV] = y;
	}
	
	/**
	 * Copies the logical frame data from one frame to another
	 * When done in parallel, all Entities should be copied at the same time, synchronously.
	 * 
	 * @param a Sender frame
	 * @param b Receiver frame
	 */
	
	public void frameCopy(int a, int b){
		vx[b] = vx[a];
		vy[b] = vy[a];
		x[b] = x[a];
		y[b] = y[a];
		facing[b] = facing[a];
		animTime[b] = animTime[a];
		animNum[b] = animNum[a];
	}
	public boolean canCollide(){
		return true;
	}
	public void setVelocity(double x, double y){
		vx[CURR] = x;
		vy[CURR] = y;
	}
	public Rectangle2D getBounds() {
		Rectangle2D bounds = (Rectangle2D) new Rectangle();
		bounds.setFrame(x[CURR], y[CURR], width, height);
		return bounds;
	}

	public void render() {
		double texX = ((animTime[RENDER]+120)%500)/125/4.0;
		
		double texY = animNum[RENDER] * 1/4.0;
		double texX2 = texX +.25;
		double texY2 = texY +.25;
		double over = 0;
		
		double[] player = {x[RENDER]-over,-y[RENDER],x[RENDER]+1+over,-y[RENDER]+1+over+over,texX,texY,texX2,texY2,1,textureID};
		RenderManager.register(player);		
	}
	public double getX(int frame) {
		return x[frame];
	}
	public double getY(int frame) {
		return y[frame];
	}
	public void setTextureID(int id){
		textureID = id;
	}
}
