package fakemon;
import java.awt.geom.Rectangle2D;

public abstract class DialogBox {

	private Rectangle2D bounds;
	private BattleScreen screen;

	public DialogBox(Rectangle2D bounds, BattleScreen screen) {
		this.bounds = bounds;
		this.screen = screen;
	}

	public Rectangle2D getBounds() {
		return bounds;
	}

	public void render(BattleScreen screen) {
		screen.renderBorder(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}
	public void onMousePress(double x, double y, int button, BattleScreen screen){};
	public abstract void go();
	public abstract boolean isActive();
}
