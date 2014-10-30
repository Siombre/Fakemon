package effects;

public class StatModEffect extends Effect {
	private int[] mods = new int[ACCURACY+1];
	public StatModEffect(int type, int mod){
		mods[type] = mod;
	}
	@Override
	public boolean add(Effect e) {
		if(e instanceof StatModEffect)
		{
			StatModEffect eff = (StatModEffect) e;
			for(int i = 0; i <= ACCURACY;i++)
			{
				mods[i] += eff.mods[i];
				
				mods[i] = Math.min(6,Math.max(mods[i],-6));
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
	public boolean conflicts(Effect e) {
		return false;
	}
	
	private float getModPercent(int type){
		int i = mods[type];
		if(type > 4)
			return (float)Math.max(3, 3+i)/Math.max(3, 3-i);
		return (float)Math.max(2, 2+i)/Math.max(2, 2-i);
	}
}
