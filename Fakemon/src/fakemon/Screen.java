package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public abstract class Screen {
	protected int width;
	protected int height;

	public static void initGL() throws LWJGLException {
		System.out.println("Initializing OpenGL Context");
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("Fakemon");
		Display.setResizable(true);
		Display.create();


		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		GL11.glEnable(GL11.GL_TEXTURE_2D);               

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0,0,width,height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
		GL11.glOrtho(0, width, height, 0, -10, 10);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing

		System.out.println("Done Initializing OpenGL Context");

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
		//return (float) (x * width);
		return (float) x;
	}
	float mapY(double y) {
		//return (float) (y * height);
		return (float) y;
	}
	void mouseEvent(){
		if (Mouse.getEventButton() > -1){
			double x = (double)Mouse.getEventX()/width;
			double y = 1-(double)Mouse.getEventY()/height;
			processMouseEvent(x,y);
		}
	}	
	public abstract void processMouseEvent(double x, double y);
	public void renderScreen(int delta){
		
		if(width != Display.getWidth() || height != Display.getHeight())
		{
			width = Display.getWidth();
			height = Display.getHeight();
			GL11.glViewport(0, 0, width, height); //NEW
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
			System.out.println("Resolution changed to " + width + "x" + height);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((width-height)/2f,0,0);
		GL11.glScaled(height, height, 1);
		render(delta);
		GL11.glPopMatrix();

	}
	public abstract void render(int delta);
	public abstract void displayMessage(String s);

	public abstract void doLogic();

	public abstract boolean isFinished();
	public void drawString(TrueTypeFont f, float x, float y, String s, Color c){
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScalef(1f/512, 1f/512, 1);
		f.drawString(x,y,s, c);

		GL11.glPopMatrix();
	}


}
