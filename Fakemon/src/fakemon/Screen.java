package fakemon;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class Screen {
	private int width;
	private int height;
	
	public static void initGL() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("Fakemon");
		Display.create();

		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		glViewport(0, 0, width, height); // Reset The Current Viewport
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		//glEnable(GL_POLYGON_SMOOTH);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glEnable(GL_BLEND);

		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	}
	public void renderBorder(double x, double y, double width, double height) {
		renderBorder((float) x, (float) y, (float) width, (float) height);
	}
	public void renderBorder(float x, float y, float width, float height){
		
		x = mapX(x);
		y = mapY(y);
		
		width = mapX(width);
		height = mapY(height);
		float xThick = mapX(.005f);
		float yThick = mapX(.005f);
		glBegin(GL_QUADS);

		glColor3d(.99, .99, .99);
		glVertex2f(x, y);
		glVertex2f(x, y + height);
		glVertex2f(x + width, y + height);
		glVertex2f(x + width, y);

		glColor3d(.2, .2, .2);
		glVertex2f(x, y);
		glVertex2f(x, y + height);
		glVertex2f(x + xThick, y + height);
		glVertex2f(x + xThick, y);

		glVertex2f(x, y);
		glVertex2f(x, y + yThick);
		glVertex2f(x + width, y + yThick);
		glVertex2f(x + width, y);

		glVertex2f(x, y + height - yThick);
		glVertex2f(x, y + height);
		glVertex2f(x + width, y + height);
		glVertex2f(x + width, y + height - yThick);

		glVertex2f(x + width - xThick, y);
		glVertex2f(x + width - xThick, y + height);
		glVertex2f(x + width, y + height);
		glVertex2f(x + width, y);
		glEnd();
	}
	public void init(){
		width = Display.getDisplayMode().getWidth();
		height = Display.getDisplayMode().getHeight();
	}
	float mapX(double x) {
		return (float) (x * width);
	}
	float mapY(double y) {
		return (float) (y * height);
	}
	void mouseEvent(){
		double x = (double)Mouse.getEventX()/width;
		double y = 1-(double)Mouse.getEventY()/height;
		processMouseEvent(x,y);
	}	
	public abstract void processMouseEvent(double x, double y);
	public abstract void render();
	public abstract int start();

}
