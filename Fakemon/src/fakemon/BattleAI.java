package fakemon;

public interface BattleAI {
	/**
	 * Requests an action for this pokemon owned by this trainer.
	 * 
	 * @param battle
	 * @param trainer
	 * @param pokemon
	 */
	public void requestBattleAction(BattleScreen battle, int trainer,int pokemon);
	/**
	 * Returns whether this AI object is now free to decide on another action.
	 * 
	 * @param battle
	 * @param trainer
	 * @param pokemon
	 * @return 
	 */
	public boolean canRequestBattleAction(BattleScreen battle, int trainer,int pokemon);
	/**
	 * If a pokemon is knocked out, return a pokemon to replace it.
	 * 
	 * @param battle
	 * @return The replacement Pokemon, null if no replacement.
	 */
	public Pokemon getNextPokemon(BattleScreen battle);
}
