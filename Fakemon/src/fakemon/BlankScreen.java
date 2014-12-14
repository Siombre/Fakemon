package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

public class BlankScreen extends Screen {

	public BlankScreen(){
		super.init();
	}
	@Override
	public void processMouseEvent(double x, double y) {}

	@Override
	public void render(int delta) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		GL11.glColor3f(0f,0f,0f);

		glVertex2f(mapX(0),mapY(0));
		glVertex2f(mapX(1),mapY(0));
		glVertex2f(mapX(1),mapY(1));
		glVertex2f(mapX(0),mapY(1));
		glEnd();

	}

	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic() {}

}
