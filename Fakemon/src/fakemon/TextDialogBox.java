package fakemon;
import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class TextDialogBox extends DialogBox{
	String message;
	String currentLine;
	boolean skip;
	boolean active;
	public TextDialogBox(Rectangle2D bounds,BattleScreen screen) {
		this(bounds,screen,"<Insert Text Here>");
		
	}
	public TextDialogBox(Rectangle2D bounds,BattleScreen screen, String message) {
		super(bounds, screen);
		this.message = message;
	}
	public void render(BattleScreen screen){
		super.render(screen);
		Rectangle2D bounds = getBounds();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		screen.drawString(screen.font,screen.mapX(bounds.getMinX() + .01), screen.mapY(bounds.getMinY() + .01), message, Color.black);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	public void go(){
		active = true;
		for(int i=0;i< 400 && (!skip || i < 50);i++)
		{
			try{
			Thread.sleep(5);
			} catch (InterruptedException e) {}

		}
		skip = false;
		active = false;
	}
	public boolean isActive(){
		return active;
	}
	public void onMousePress(double x, double y, int button,BattleScreen screen){
		if(button != -1 && Mouse.isButtonDown(0))
		{
			skip = true;
		}
	}
}
