package tiles;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fakemon.RenderManager;
import fakemon.Start;
import fakemon.Trainer;

public class TreeTile extends Tile {
	private static Texture texture;
	public void render(int delta, int x, int y, int z){
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/world/tree.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		float over = .5f;
		double[] data = {x-over,-y-over-over/2,x+1+over,-y+1+over/2,0,0,1,1,z,texture.getTextureID()};
		RenderManager.register(data);
	}
	@Override
	public void tick() {}
	@Override
	public void onStep(Trainer t, int delta) {}
	@Override
	public boolean isPassable() {
		return false;
	}
}
