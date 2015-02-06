package tiles;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fakemon.RenderManager;
import fakemon.Start;
import fakemon.Trainer;

public class StoneTile extends Tile{
	private static Texture texture;
	public void render(int delta, int x, int y, int z){
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/world/stone.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		double[] data = {x,-y,x+1,-y+1,0,0,1,1,z,texture.getTextureID()};
		RenderManager.register(data);
	}
	@Override
	public void tick() {}
	@Override
	public void onStep(Trainer t) {}
	@Override
	public boolean isPassable() {
		return true;
	}
}
