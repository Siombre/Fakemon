package fakemon;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class MoveMenuDialogBox extends DialogBox {
	private Move[] moves  = new Move[4]; 
	private boolean active = false;
	MoveBattleAction move;
	Trainer t;
	Pokemon p;
	public MoveMenuDialogBox(Rectangle2D bounds,BattleScreen screen) {
		super(bounds, screen);
	}
	public void moveInit(Trainer t, Pokemon p){
		moves = p.getMoves();
		this.t = t;
		this.p = p;
	}
	public void render(BattleScreen screen){
		super.render(screen);
		Rectangle2D bounds = getBounds();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		for(int i = 0; i< 4; i++){
			if(moves[i] != null)
			{
				double x = bounds.getMinX() + .03 * bounds.getWidth() + (i%2)*.5*bounds.getWidth();
				double y = bounds.getMinY() + .05 * bounds.getHeight() + (i/2)*.5*bounds.getHeight();

				screen.drawString(screen.font, screen.mapX(x), screen.mapY(y), moves[i].getInfo().getName(), Color.black);
				
				x = bounds.getMinX() + .03 * bounds.getWidth() + (i%2)*.5*bounds.getWidth();
				y = bounds.getMinY() + .20 * bounds.getHeight() + (i/2)*.5*bounds.getHeight();
				
				screen.drawString(screen.font, screen.mapX(x), screen.mapY(y), moves[i].getCurPP() + " / " + moves[i].getMaxPP(), Color.black);

			}
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		
		glBegin(GL_QUADS);
		double x1 = screen.mapX(bounds.getMinX() + .49 * bounds.getWidth());
		double x2 = screen.mapX(bounds.getMinX() + .51 * bounds.getWidth());
		double y1 = screen.mapY(bounds.getMinY());
		double y2 = screen.mapY(bounds.getMinY() + bounds.getHeight());
		
		glColor3d(.2, .2, .2);

		glVertex2d(x1, y1);
		glVertex2d(x2, y1);
		glVertex2d(x2, y2);
		glVertex2d(x1, y2);
		
		x1 = screen.mapX(bounds.getMinX());
		x2 = screen.mapX(bounds.getMinX() + bounds.getWidth());
		y1 = screen.mapY(bounds.getMinY() + .48 * bounds.getHeight());
		y2 = screen.mapY(bounds.getMinY() + .52 * bounds.getHeight());
		
		glVertex2d(x1, y1);
		glVertex2d(x2, y1);
		glVertex2d(x2, y2);
		glVertex2d(x1, y2);
		glEnd();

	}
	public MoveBattleAction getMove(){
		return move;
	}
	public void go() {
		active = true;
		while (active)
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	public void onMousePress(double x, double y, int button,BattleScreen screen){
		if(active && button != -1 && Mouse.isButtonDown(0))
		{
			Move m = moves[(int) (x+.5) + 2*(int)(y+.5)];
			if(m!= null && m.isValid())
			{
				move =  new MoveBattleAction(m,p,screen.acPokemon[1][0]);
				active = false;
			}
		}
	}
	@Override
	public boolean isActive() {
		return active;
	}
	
}
