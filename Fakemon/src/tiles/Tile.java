package tiles;

import fakemon.Trainer;

public abstract class Tile {
	public abstract void render();
	public abstract void tick();
	public abstract void onStep(Trainer t);
	public abstract boolean isPassable();
	
}
