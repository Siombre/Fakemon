package fakemon;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class BattleScreen extends Screen {
	Trainer[] trainers;
	Pokemon[][] acPokemon;
	BattleAction[][] actions;
	double[][] hpRatio;
	TrueTypeFont font = Fakemon.font;
	TrueTypeFont smallFont = Fakemon.smallFont;
	private int state = 0;
	private int winner = 0;
	DialogBox dialog;
	DialogBox dialog2;

	int fainted = 0;
	Rectangle2D dialogLoc = new Rectangle2D.Float(.01f, .7f, .68f, .29f);
	Rectangle2D dialog2Loc = new Rectangle2D.Float(.69f, .7f, .28f, .29f);

	BattleScreen(Trainer[] trainers, boolean wild, int[] pokemonOut) {
		super.init();
		this.trainers = trainers;




		acPokemon = new Pokemon[trainers.length][];
		actions = new BattleAction[trainers.length][];
		hpRatio = new double[trainers.length][];

		for (int i = 0; i < trainers.length; i++) {
			int length;
			if (pokemonOut.length <= i)
				length = 1;
			else
				length = pokemonOut[i];
			acPokemon[i] = new Pokemon[length];
			actions[i] = new BattleAction[length];
			hpRatio[i] = new double[length];
		}

		fillPokemon();

	}

	public void render(int delta) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		glBegin(GL_QUADS);
		glColor3d(.99, .99, .99);
		glVertex2d(0, 0);
		glColor3d(.85, .85, .85);
		glVertex2d(0, mapY(1));
		glVertex2d(mapX(1), mapY(1));
		glColor3d(.99, .99, .99);
		glVertex2d(mapX(1), 0);

		if(dialog != null)
			dialog.render(this);
		if(dialog2 != null)
			dialog2.render(this);
		glEnd();

		renderInfo(1, 0, .015, .01, true);
		renderInfo(0, 0, .685, .54, true);
	}
	public void renderInfo(int t, int p, double x, double y, boolean renderXP) {
		Pokemon pm = acPokemon[t][p];
		if (pm == null)
			return;
		renderBorder((float)x,(float)y,.3f,.15f);

		glBegin(GL_QUADS);


		glColor3d(.3 + .5 * (1 - hpRatio[t][p]), .3 + .5 * hpRatio[t][p], .3);

		glVertex2d(mapX(x + .05), mapY(y + .10));
		glVertex2d(mapX(x + .05), mapY(y + .105));
		glVertex2d(mapX(x + .05 + (hpRatio[t][p] * .2)), mapY(y + .105));
		glVertex2d(mapX(x + .05 + (hpRatio[t][p] * .2)), mapY(y + .10));

		glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(mapX(x + .01), mapY(y + .01), pm.getName(), Color.black);
		smallFont.drawString(mapX(x + .23), mapY(y + .0265), "Lv. " + pm.getLevel(), Color.black);

		smallFont.drawString(mapX(x + .1), mapY(y + .11),
				(" " + (int) (pm.getStat(PokemonInfo.MAX_HP) * hpRatio[t][p] + .5) + "/" + (int)pm.getStat(PokemonInfo.MAX_HP)), Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	
	
	
	private static final int INIT_STATE = 0;
	private static final int REQUEST_STATE = 1;
	private static final int REQUEST_WAIT = 2;
	private static final int DO_ACTION_STATE = 3;
	private static final int FINISH = 4;
	private static final int ENDED = 5;

	public void doLogic() {
		if(state == INIT_STATE){
			fillPokemon();
			state = REQUEST_STATE;
		}

		// For each Pokemon, check to see if a next move has been
		// registered, and is valid (For multi-turn moves)
		// For each trainer, get an action
		if(state == REQUEST_STATE){
			for (int t = 0; t < trainers.length; t++) {
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null && this.actions[t][p] == null)
						trainers[t].battleAI.requestBattleAction(this, t, p);
				}
			}
			state = REQUEST_WAIT;
		}
		
		if(state == REQUEST_WAIT){

			boolean missing = false;
			for (int t = 0; t < trainers.length; t++) {
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null && this.actions[t][p] == null)
						missing = true;
				}
			}

			if(!missing)
				state = DO_ACTION_STATE;
		}
		
		if(state == DO_ACTION_STATE){

			ArrayList<BattleAction> actions = new ArrayList<BattleAction>();

			for (int t = 0; t < trainers.length; t++) {
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null)
					{
						actions.add(this.actions[t][p]);
						this.actions[t][p] = null;
					}
				}
			}


			try {

				sortActions(actions);

				for(BattleAction ba : actions)
				{
					if(ba.validate(this)){
						ba.doAction(this);
						checkFainted();
						while(dialog.isActive())
							Thread.sleep(5);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (int t = 0; t < trainers.length; t++) {
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null)
					{
						acPokemon[t][p].onTurnEnd(this);
						checkFainted();
						
					}
				}
			}

			int battlers = 0;
			for (int t = 0; t < trainers.length; t++) {
				boolean alive = false;
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null)
						alive = true;
				}
				if(alive)
					battlers++;

			}
			if(battlers <= 1)
				state = FINISH;
			else
				state = 1;


		} 
		
		
		if(state == FINISH)
		{
			winner = -1;
			for (int t = 0; t < trainers.length; t++) {
				boolean alive = false;
				for (int p = 0; p < acPokemon[t].length; p++) {
					if(acPokemon[t][p] != null)
						alive = true;
				}
				if(alive)
				{
					winner = t;
					break;
				}
			}
			if(winner == 0)
				displayMessage("You win!");
			else
				displayMessage("You lose!");
			state = ENDED;
		}
		//System.out.println(state);
	}
	public boolean isFinished(){
		return state == ENDED;
	}
	public int getWinner(){
		return winner;
	}
	private void sortActions(ArrayList<BattleAction> actions){
		//Insertion sort by priority then speed

		for(int i = 0;i<actions.size();i++)
		{
			int p = actions.get(i).getPriority();
			float s = actions.get(i).getSpeed();
			int i2;
			for(i2 = 0;i2< i;i2++)
			{
				int p2 = actions.get(i2).getPriority();
				float s2 = actions.get(i2).getSpeed();
				if(p>p2)
					break;
				if(p == p2 && s>s2)
					break;
			}
			if(i != i2)
			{
				actions.add(i2, actions.remove(i));
			}
		}

		//Shuffle sets of actions of the same priority and speed
		int start = 0;
		int pPrev = actions.get(0).getPriority();
		float sPrev = actions.get(0).getSpeed();

		for(int i = 1;i<actions.size();i++)
		{
			int p = actions.get(i).getPriority();
			float s = actions.get(i).getSpeed();

			if(p != pPrev || s != sPrev && i - start > 1){
				//shuffle

				for(int i2 = start;i < i;i2++)
				{
					BattleAction temp = actions.get(i2);
					int r = Util.rand(i2+1, i-1);
					actions.set(i2, actions.get(r));
					actions.set(r, temp);		
				}

				start = i;
				pPrev = p;
				sPrev = s;
			}
		}

	}

	private void checkFainted(){
		for (int t = 0; t < trainers.length; t++) {
			for (int p = 0; p < acPokemon[t].length; p++) {
				if (acPokemon[t][p] != null) {
					Pokemon pm = acPokemon[t][p];
					if(pm.getHealth() == 0)
					{
						displayMessage(pm.getName() + " fainted!");	
						for (int t2 = 0; t2 < trainers.length; t2++) {
							for (int p2 = 0; p2 < acPokemon[t2].length; p2++) {
								if(t2 == t  || acPokemon[t2][p2] == null) continue;
								int exp = (int) (pm.getLevel()*pm.getInfo().baseExp/5.0*Math.pow(2*pm.getLevel()+10,2.5)/Math.pow(pm.getLevel()+acPokemon[t2][p2].getLevel()+10,2.5));
								displayMessage(String.format("%s gained %d exp.", acPokemon[t2][p2].getName(),exp));
								acPokemon[t2][p2].addExp(exp);
							}
						}

						acPokemon[t][p] = trainers[t].getBattleAI().getNextPokemon(this);

						if(acPokemon[t][p] != null)
						{
							displayMessage(String.format("%s sent out %s!", trainers[t].getName(),acPokemon[t][p].getName()));
							hpRatio[t][p] = (double) acPokemon[t][p].getHealth() / acPokemon[t][p].getStat(PokemonInfo.MAX_HP);
						}
						fainted++;
					}
				}

			}
		}
	}
	private void fillPokemon() {
		for (int t = 0; t < trainers.length; t++) {
			for (int p = 0; p < acPokemon[t].length; p++) {
				if (acPokemon[t][p] == null) {
					Pokemon[] tPoke = trainers[t].getPokemon();
					for (int p2 = 0; p2 < tPoke.length; p2++) {
						if (tPoke[p2] != null && tPoke[p2].getHealth() > 0) {
							boolean alreadyOut = false;
							for (int p3 = 0; p3 < acPokemon[t].length; p3++) {
								if (tPoke[p2] == acPokemon[t][p3])
									alreadyOut = true;
							}
							if (alreadyOut)
								continue;
							acPokemon[t][p] = tPoke[p2];
							hpRatio[t][p] = (double) tPoke[p2].getHealth() / tPoke[p2].getStat(PokemonInfo.MAX_HP);
							break;
						}
					}
				}
			}
		}
	}

	public void damage(Pokemon poke, int damage) {
		int t = 0;
		int p = 0;
		boolean found = false;
		poke.damage(damage);
		for (t = 0; t < trainers.length; t++) {
			for (p = 0; p < acPokemon[t].length; p++) {
				if (acPokemon[t][p] == poke) {
					found = true;
					break;
				}
			}
			if (found)
				break;
		}
		if (!found)
			return;
		int steps = 50;
		double delta = ((double) damage / poke.getStat(PokemonInfo.MAX_HP)) / steps;
		try {

			for (int i = 0; i < steps; i++) {
				hpRatio[t][p] -= delta;
				if (hpRatio[t][p] < 0) {
					hpRatio[t][p] = 0;
					break;
				}
				Thread.sleep(20);

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hpRatio[t][p] = (double) poke.getHealth() / poke.getStat(PokemonInfo.MAX_HP);

	}

	public void displayMessage(String s) {
		dialog = new TextDialogBox(dialogLoc, this, s);
		dialog.go();
	}




	public void processMouseEvent(double x, double y){
		if (dialogLoc.contains(x, y))
		{
			if (dialog != null)
			{
				double scaleX = (x-dialogLoc.getX())/dialogLoc.getWidth();
				double scaleY = (y-dialogLoc.getY())/dialogLoc.getHeight();
				dialog.onMousePress(scaleX, scaleY, Mouse.getEventButton(),this);
			}
		}
		if (dialog2Loc.contains(x,y))
		{
			if (dialog2 != null)
			{
				double scaleX = (x-dialog2Loc.getX())/dialog2Loc.getWidth();
				double scaleY = (y-dialog2Loc.getY())/dialog2Loc.getHeight();
				dialog2.onMousePress(scaleX, scaleY, Mouse.getEventButton(),this);
			}
		}
	}

	public void addAction(int trainer, int pokemon, BattleAction move) {
		actions[trainer][pokemon] = move;
	}	

}
