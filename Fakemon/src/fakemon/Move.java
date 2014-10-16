package fakemon;


public class Move {
	private MoveInfo info;
	
	int curPP, maxPP;
	public Move(MoveInfo info){
		this(info, info.getBasePP(), info.getBasePP());
	}
	
	public Move(MoveInfo info, int curPP, int maxPP){
		if(info == null) throw new IllegalArgumentException("Move definition must not be null.");
		
		this.info = info;
		if(maxPP > info.getMaxPP())
			maxPP = info.getMaxPP();
		if(maxPP < info.getBasePP())
			maxPP = info.getBasePP();
		
		if(curPP > maxPP)
			curPP = maxPP;
		if(curPP < 0)
			curPP = 0;
		this.curPP = curPP;
		this.maxPP = maxPP;
		
	}
	public void onUse(Pokemon user, Pokemon target, BattleScreen battle){
		if(curPP > 0)
		{
			info.onUse(user, target, battle);
			curPP--;
		}else 
			System.err.println("Whoops. Out of PP");
			
	}
	public boolean isValid(){
		if(curPP > 0) return true;
		return info.isValid();
	}
	public MoveInfo getInfo(){
		return info;
	}
}
