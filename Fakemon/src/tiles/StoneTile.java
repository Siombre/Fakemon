package tiles;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fakemon.Trainer;

public class StoneTile extends Tile{
	private static Texture texture;
	public void render(){
		if(texture == null)
			try {
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/world/stone.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		Color.white.bind();
		texture.bind(); 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(0,0);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(1,0);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(1,1);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0,1);
		GL11.glEnd();
	}
	@Override
	public void tick() {}
	@Override
	public void onStep(Trainer t) {}
	@Override
	public boolean isPassable() {
		return false;
	}
}
