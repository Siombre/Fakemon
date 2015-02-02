package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

public class FadeTransitionScreen extends Screen {
	private Screen to, from;
	private int time;
	private int totalTime = 3000;
	private int endBehavior;
	public static final int POP = -1;
	public static final int SWITCH = 0;
	public static final int PUSH = 1;
	private boolean finished;
	public FadeTransitionScreen(Screen to){
		this(to, SWITCH);
	}
	public FadeTransitionScreen(Screen to, int behavior){
		super.init();
		this.to = to;
		this.from = Fakemon.getCurrentScreen();
		endBehavior = behavior;
		if(behavior == POP)
			to = Fakemon.peek(-1);
	}
	@Override
	public void processMouseEvent(double x, double y) {}

	@Override
	public void render(int delta) {
		time += delta;
		if(time < totalTime/2)
			from.render(delta);
		else
			to.render(delta);
		
		float x = (float)time/totalTime;
		
		
		float alpha = -8*x*(x-1);
		//System.out.printf("%.2f\n",alpha);
		if(alpha > 1)
			alpha = 1;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor4f(0f,0f,0f,alpha);
		glBegin(GL_QUADS);
		glVertex2f(0,0);
		glVertex2f(1,0);
		glVertex2f(1,1);
		glVertex2f(0,1);
		
		GL11.glEnd();

	}

	@Override
	public void displayMessage(String s) {
		to.displayMessage(s);
	}

	@Override
	public void doLogic() {
		/*if(time < totalTime/2)
			from.doLogic();
		else
			to.doLogic();*/
		if(time > totalTime && !finished)
		{
			switch(endBehavior){
				case POP:
					Fakemon.popScreen();     //pop self
					Fakemon.popScreen();     //pop originator
					break;
				case PUSH:
					Fakemon.popScreen();     //pop self
					Fakemon.pushScreen(to);  //push destination
					break;
				default:
					Fakemon.popScreen();     //pop self
					Fakemon.popScreen();     //pop originator
					Fakemon.pushScreen(to);  //push destination
					
			}
			finished = true;
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
