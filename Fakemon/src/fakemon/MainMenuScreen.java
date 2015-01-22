package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class MainMenuScreen extends Screen {
	private boolean running = true;
	@Override
	public void processMouseEvent(double x, double y) {
		if(running)
		{
			if(y > .5)
			{
				Fakemon.pushScreen(new FadeTransitionScreen(new OverworldScreen(null)));
			} else{
				Fakemon.pushScreen(new FadeTransitionScreen(new BattleLoopScreen()));
			}
			running = false;
		}

	}

	@Override
	public void render(int delta) {
		glBegin(GL_QUADS);
		glColor3d(.99, .99, .99);
		glVertex2d(0, 0);
		glColor3d(.85, .85, .85);
		glVertex2d(0, 1);
		glVertex2d(1, 1);
		glColor3d(.99, .99, .99);
		glVertex2d(1, 0);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		drawString(Fakemon.font,.3f,.3f,"Click for battles",Color.black);
		drawString(Fakemon.font,.3f,.8f,"Click for overworld",Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void displayMessage(String s) {

	}

	@Override
	public void doLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
