package fakemon;


public class Move {
	private MoveInfo info;
	
	private int curPP;

	private int maxPP;
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
		this.setCurPP(curPP);
		this.setMaxPP(maxPP);
		
	}
	public void onUse(Pokemon user, Pokemon target, BattleScreen battle){
		if(getCurPP() > 0)
		{
			info.onUse(user, target, battle);
			setCurPP(getCurPP() - 1);
		}else 
			System.err.println("Whoops. Out of PP");
			
	}
	public boolean isValid(){
		if(getCurPP() > 0) return true;
		return info.isValid();
	}
	public MoveInfo getInfo(){
		return info;
	}

	public int getCurPP() {
		return curPP;
	}

	public void setCurPP(int curPP) {
		this.curPP = curPP;
	}

	public int getMaxPP() {
		return maxPP;
	}

	public void setMaxPP(int maxPP) {
		this.maxPP = maxPP;
	}
}
