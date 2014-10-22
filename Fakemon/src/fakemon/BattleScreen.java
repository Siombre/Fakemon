package fakemon;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import static org.lwjgl.opengl.GL11.*;

public class BattleScreen extends Screen {
	Trainer[] trainers;
	Pokemon[][] acPokemon;
	double[][] hpRatio;
	TrueTypeFont font;
	TrueTypeFont smallFont;
	

	DialogBox dialog;
	DialogBox dialog2;

	int fainted = 0;
	Rectangle2D dialogLoc = new Rectangle2D.Float(.01f, .7f, .68f, .29f);
	Rectangle2D dialog2Loc = new Rectangle2D.Float(.69f, .7f, .28f, .29f);

	BattleScreen(Trainer[] trainers, boolean wild, int[] pokemonOut) {
		super.init();
		
		this.trainers = trainers;
		
		Font awtFont = new Font("Times New Roman", Font.BOLD, 18); // name, style (PLAIN, BOLD, or ITALIC), size
		font = new TrueTypeFont(awtFont, true); // base Font, anti-aliasing true/false
		
		Font awtFont2 = new Font("Times New Roman", Font.BOLD, 12); // name, style (PLAIN, BOLD, or ITALIC), size
		smallFont = new TrueTypeFont(awtFont2, true);
		

		acPokemon = new Pokemon[trainers.length][];
		hpRatio = new double[trainers.length][];

		for (int i = 0; i < trainers.length; i++) {
			int length;
			if (pokemonOut.length <= i)
				length = 1;
			else
				length = pokemonOut[i];
			acPokemon[i] = new Pokemon[length];
			hpRatio[i] = new double[length];
		}
		
		fillPokemon();

	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
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
				(" " + (int) (pm.getStats()[PokemonInfo.MAX_HP] * hpRatio[t][p] + .5) + "/" + pm.getStats()[PokemonInfo.MAX_HP]), Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	void battleLogic() {
		boolean finished = false;

		fillPokemon();
		while (!Display.isCloseRequested()) {

			// For each Pokemon, check to see if a next move has been
			// registered, and is valid (For multi-turn moves)
			// For each trainer, get an action
			
			ArrayList<BattleAction> actions = new ArrayList<BattleAction>();
			for (int t = 0; t < trainers.length; t++) {
				for (int p = 0; p < acPokemon[t].length; p++) {
					actions.add(trainers[t].getBattleAI().getAction(this, t, p));
				}
			}
			
			
			try {
				
				sortActions(actions);
				
				
				for(BattleAction ba : actions)
				{
					if(ba.validate()){
						ba.doAction(this);
						checkFainted();
						while(dialog.isActive())
							Thread.sleep(5);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	private void sortActions(ArrayList<BattleAction> actions){
		//Insertion sort by priority then speed
		
		for(int i = 0;i<actions.size();i++)
		{
			int p = actions.get(i).getPriority();
			int s = actions.get(i).getSpeed();
			int i2;
			for(i2 = 0;i2< i;i2++)
			{
				int p2 = actions.get(i2).getPriority();
				int s2 = actions.get(i2).getSpeed();
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
		int sPrev = actions.get(0).getSpeed();
		
		for(int i = 1;i<actions.size();i++)
		{
			int p = actions.get(i).getPriority();
			int s = actions.get(i).getSpeed();
			
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
						acPokemon[t][p] = Start.generatePokemon();
						hpRatio[t][p] = (double) acPokemon[t][p].getHealth() / acPokemon[t][p].getStats()[PokemonInfo.MAX_HP];
						fainted++;
						
						for (int t2 = 0; t2 < trainers.length; t2++) {
							for (int p2 = 0; p2 < acPokemon[t2].length; p2++) {
								if(t2 == t) continue;
								int exp = (int) (pm.getLevel()*pm.getInfo().baseExp/5.0*Math.pow(2*pm.getLevel()+10,2.5)/Math.pow(pm.getLevel()+acPokemon[t2][p2].getLevel()+10,2.5));
								displayMessage(String.format("%s gained %d exp.", acPokemon[t2][p2].getName(),exp));
								acPokemon[t2][p2].addExp(exp);
							}
						}
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
							hpRatio[t][p] = (double) tPoke[p2].getHealth() / tPoke[p2].getStats()[PokemonInfo.MAX_HP];
							break;
						}
					}
				}
			}
		}
	}

	void damage(Pokemon poke, int damage) {
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
		double delta = ((double) damage / poke.getStats()[PokemonInfo.MAX_HP]) / steps;
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
		hpRatio[t][p] = (double) poke.getHealth() / poke.getStats()[PokemonInfo.MAX_HP];

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
	
}
