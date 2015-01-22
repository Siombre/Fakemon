package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

public class BlankScreen extends Screen {
	private float r,g,b;
	public BlankScreen(){
		this(0,0,0);
	}
	public BlankScreen(float r, float g, float b){
		super.init();
		this.r = r;
		this.b = b;
		this.g = g;
	}
	@Override
	public void processMouseEvent(double x, double y) {}

	@Override
	public void render(int delta) {

		glBegin(GL_QUADS);
		GL11.glColor3f(r,g,b);

		glVertex2f(0,0);
		glVertex2f(1,0);
		glVertex2f(1,1);
		glVertex2f(0,1);
		glEnd();

	}

	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic() {}
	@Override
	public boolean isFinished() {
		return true;
	}

}
