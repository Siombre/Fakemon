package fakemon;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import tiles.Tile;

public class World {
	Tile[][][] map;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	public World(Tile[][][] map){
		this.map = map;
	}
	public Tile getTile(int x,int y, int l){
		if(l<0 || l >= map.length)
			return null;
		if(x<0 || x >= map[l].length)
			return null;
		if(y<0 || y >= map[l][x].length)
			return null;
		return map[l][x][y];
	}
	public ArrayList<Entity> getEntityList(){
		return entities;
	}
	public boolean addEntity(Entity e,double x, double y){
		if(e != null && getCollide(e)==null)
		{
			entities.add(e);
			e.put(this, x, y);
			return true;
		}
		return false;
	}
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	public Rectangle2D getCollide(Entity e){
		if(!e.canCollide()) return null;
		Rectangle2D r = e.getBounds();
		for(int x = (int)r.getMinX(); x <= (int)r.getMaxX();x++)
			for(int y = (int)r.getMinY(); y <= (int)r.getMaxY();y++)
				for(int l = 0; l< map.length;l++){
					if(getTile(x,y,l) != null && !getTile(x,y,l).isPassable())
					{
						
						Rectangle r2 = new Rectangle(x,y,1,1);
						if(r.intersects(r2))
							return r2;
					}
				}
		for(Entity e2:entities)
			if(e2.canCollide() && e!=e2 && e.getBounds().intersects(e2.getBounds()))
				return e2.getBounds();

		return null;
	}

}
