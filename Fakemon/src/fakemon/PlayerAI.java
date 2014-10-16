package fakemon;

public class PlayerAI implements BattleAI{

	@Override
	public BattleAction getAction(BattleScreen battle, int trainer, int pokemon) {
		MoveMenuDialogBox box = new MoveMenuDialogBox(battle.dialogLoc,battle);
		
		box.moveInit(battle.trainers[trainer], battle.acPokemon[trainer][pokemon]);
		battle.dialog = box;
		box.go();
		
		return box.getMove();
	}
	
}
