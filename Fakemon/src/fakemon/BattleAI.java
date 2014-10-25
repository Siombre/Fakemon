package fakemon;

public interface BattleAI {
	public BattleAction getAction(BattleScreen battle, int trainer, int pokemon);

	public Pokemon getNextPokemon(BattleScreen battle);
}
