package tiles;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fakemon.RenderManager;
import fakemon.Start;
import fakemon.Trainer;

public class TallGrass extends Tile{
	private static Texture texture;
	public void render(int delta, int x, int y, int z){
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(Start.getPath("res/world/tallGrass.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		float over = .25f;
		double[] data = {x-over,-y-over,x+1+over,-y+1+over,0,0,1,1,z,texture.getTextureID()};
		RenderManager.register(data);
		//double[] data2 = {x-over,-y+(1+over)/2,x+1+over,-y+1+over,0,.5,1,1,z,texture.getTextureID()};
		//RenderManager.register(data2);
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
