package tiles;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import fakemon.Pokemon;
import fakemon.Trainer;

public class HealTile extends Tile {
	int timer = 0;
	@Override
	public void render(int delta) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		timer+= delta;
		if(timer > 3000)
			timer = 3000;
		Color.white.bind();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3d(1, 1-.6*(timer/3000f),1-.6*(timer/3000f));
		GL11.glVertex2f(0,0);
		GL11.glVertex2f(1,0);
		GL11.glVertex2f(1,1);
		GL11.glVertex2f(0,1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	@Override
	public void tick() {

	}

	@Override
	public void onStep(Trainer t) {
		if(timer == 3000)
		{
			if(t!= null)
				for(Pokemon p:t.getPokemon())
					p.fullHeal();
			timer = 0;
		}
	}

	@Override
	public boolean isPassable() {
		return true;
	}

}
