package effects;

import fakemon.Pokemon;
import fakemon.Screen;
import fakemon.Util;

public class StatModEffect extends Effect {
	private int[] mods = new int[ACCURACY+1];
	public StatModEffect(int type, int mod){
		mods[type] = mod;
	}
	@Override
	public boolean add(Effect e,Screen screen) {
		if(e instanceof StatModEffect)
		{
			StatModEffect eff = (StatModEffect) e;
			for(int i = 0; i <= ACCURACY;i++)
			{
				int newMod = Math.min(6,Math.max(mods[i]+eff.mods[i],-6));
				int diff = newMod - mods[i];
				mods[i] = newMod;
				
				
				if(eff.mods[i] == 0) continue;
				screen.displayMessage(getMessage(target,diff,mods[i],i));
			}
			return true;
		}
		return false;
	}
	public void onBattleEnd(){
		end();
	}
	public float getStatMod(int type){
		if(type < ATTACK || type > ACCURACY) return 1;
		return getModPercent(type);
	}
	@Override
	public boolean conflicts(Effect e,Screen screen) {
		return false;
	}
	private float getModPercent(int type){
		int i = mods[type];
		if(type > 4)
			return (float)Math.max(3, 3+i)/Math.max(3, 3-i);
		return (float)Math.max(2, 2+i)/Math.max(2, 2-i);
	}
	public void onNewApply(Screen screen,Pokemon user, Pokemon target){
		super.onNewApply(screen, user, target);
		
		for(int i = 0; i <= ACCURACY;i++)
		{
			int diff =  Math.min(6,Math.max(mods[i],-6));
			if(diff == 0) continue;
			screen.displayMessage(getMessage(target,diff,mods[i],i));
		}
		for(int i = 0; i <= ACCURACY;i++)
		{
			mods[i] = Math.min(6,Math.max(mods[i],-6));
		}
	}
	public String getMessage(Pokemon target, int diff , int baseDiff, int statNum){
		
		String message = Util.possessive(target.getName()) + " " + statNames[statNum];
		
		if(baseDiff <= -12) 
			 message += " was minimized!";
		else if (baseDiff >= 12) 
			 message += " was maximized!";
		else if(diff < 0)
		{
			if(diff == -1)
				message += " fell!";
			else if(diff == -2)
				message += " harshly fell!";
			else if(diff <= -3)
				message += " severely fell!";
		}
		else if(diff > 0)
		{
			if(diff == 1)
				message += " rose!";
			else if(diff == 2)
				message += " rose sharply!";
			else if(diff <= 3)
				message += " rose drastically!";
		}
		
		else if(baseDiff < 0 && diff == 0)
			message += " cannot go any lower!";
		else if(baseDiff > 0 && diff == 0)
			message += " cannot go any higher!";
		
		return message;
	}
}
