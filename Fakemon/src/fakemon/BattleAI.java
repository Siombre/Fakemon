package fakemon;

public interface BattleAI {
	public void requestBattleAction(BattleScreen battle, int trainer,int pokemon);
	public boolean canRequestBattleAction(BattleScreen battle, int trainer,int pokemon);
	public Pokemon getNextPokemon(BattleScreen battle);
}
