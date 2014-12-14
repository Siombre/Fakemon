package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

public class FadeTransitionScreen extends Screen {
	private Screen to, from;
	int time;
	int totalTime = 3000;
	
	public FadeTransitionScreen(Screen to){
		super.init();
		this.to = to;
		this.from = Fakemon.getCurrentScreen();
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
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0f,0f,0f,alpha);

		glBegin(GL_QUADS);

		
		glVertex2f(mapX(0),mapY(0));
		glVertex2f(mapX(1),mapY(0));
		glVertex2f(mapX(1),mapY(1));
		glVertex2f(mapX(0),mapY(1));
		
		GL11.glEnd();

	}

	@Override
	public void displayMessage(String s) {
		to.displayMessage(s);
	}

	@Override
	public void doLogic() {
		if(time < totalTime/2)
			from.doLogic();
		else
			to.doLogic();
		if(time > totalTime)
			Fakemon.setCurrentScreen(to);
	}

}
