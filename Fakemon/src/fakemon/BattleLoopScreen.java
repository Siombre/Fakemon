package fakemon;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;


public class BattleLoopScreen extends Screen{
	private static final int INIT = -1;
	private static final int PREP = 0;
	private static final int WAITING = 1;
	private static final int READY = 2;
	private int state = INIT;
	private BattleScreen battle;
	private Trainer[] t = new Trainer[2];
	private Trainer you;
	private boolean won;
	private int[] is = { 1 , 1 };
	@Override
	public void processMouseEvent(double x, double y) {
		if(state == READY)
		{
			Fakemon.pushScreen(new FadeTransitionScreen(battle,FadeTransitionScreen.PUSH));
			state = WAITING;
		}
	}

	@Override
	public void render(int delta) {
		glBegin(GL_QUADS);
		glColor3d(.99, .99, .99);
		glVertex2d(0, 0);
		glColor3d(.85, .85, .85);
		glVertex2d(0, 1);
		glVertex2d(1, 1);
		glColor3d(.99, .99, .99);
		glVertex2d(1, 0);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		drawString(Fakemon.font,.5f,.5f,"Click to battle again",Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void displayMessage(String s) {}

	@Override
	public void doLogic(){
		switch(state){
			case INIT:
				
				you = new Trainer("Player");
				you.addPokemon(Fakemon.generatePokemon(10));
				you.battleAI = new PlayerAI();
				
				t[0] = you;
			case PREP:
				if(!won)
					you.getPokemon()[0].fullHeal();
				
				Trainer enemy = new Trainer("Opponent");
				enemy.addPokemon(Fakemon.generatePokemon(10));
				enemy.addPokemon(Fakemon.generatePokemon(10));

				t[1] = enemy ;
				battle = new BattleScreen(t, false, is);
				state = READY;
				break;
			case WAITING:
				if(battle != null && battle.isFinished())
				{
					state = PREP;
					won = battle.getWinner() == 0;
				}
			
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
