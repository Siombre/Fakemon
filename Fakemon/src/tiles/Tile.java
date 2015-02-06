package tiles;

import fakemon.Trainer;

public abstract class Tile {
	public abstract void render(int delta, int x, int y, int z);
	public abstract void tick();
	public abstract void onStep(Trainer t);
	public abstract boolean isPassable();
	
}
