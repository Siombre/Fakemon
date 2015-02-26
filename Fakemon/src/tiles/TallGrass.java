package tiles;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import fakemon.BattleScreen;
import fakemon.FadeTransitionScreen;
import fakemon.Fakemon;
import fakemon.OverworldScreen;
import fakemon.RenderManager;
import fakemon.Start;
import fakemon.Trainer;
import fakemon.Util;

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
	public void onStep(Trainer t, int delta) {
		if(t.isAlive() && Util.flip(.1*delta/1000f) && Fakemon.getCurrentScreen().getClass() == OverworldScreen.class){
			Trainer enemy = new Trainer("Opponent");
			enemy.addPokemon(Fakemon.generatePokemon(10));
			Trainer[] trainers = {t, enemy};
			int[] is = { 1 , 1 };
			BattleScreen battle = new BattleScreen(trainers, false, is);
			Fakemon.pushScreen(new FadeTransitionScreen(battle,FadeTransitionScreen.PUSH));
		}
	}
	@Override
	public boolean isPassable() {
		return true;
	}
}
