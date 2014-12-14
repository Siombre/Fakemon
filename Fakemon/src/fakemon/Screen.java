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
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class Screen {
	protected int width;
	protected int height;

	public static void initGL() throws LWJGLException {
		System.out.println("Initializing OpenGL Context");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("Fakemon");
		Display.create();

		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		/*glViewport(0, 0, width, height); // Reset The Current Viewport
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, 640, 480, 0, 1, -1);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          

		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL11.GL_ALPHA);

		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);*/
		GL11.glEnable(GL11.GL_TEXTURE_2D);               

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		System.out.println("Done Initializing OpenGL Context");

	/*	GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 300.0f); // Calculate The Aspect Ratio Of The Window
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix

		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
		GL11.glEnable(GL11.GL_TEXTURE_2D);*/


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
	public abstract void render(int delta);
	public abstract void displayMessage(String s);

	public abstract void doLogic();

	public void start(){
		Screen s = Fakemon.getCurrentScreen();
		Fakemon.setCurrentScreen(this);
		while(!isFinished())
			doLogic();
		Fakemon.setCurrentScreen(s);
	}
	public boolean isFinished() {
		return true;
	}



}
