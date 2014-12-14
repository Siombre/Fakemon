package fakemon;

public class PlayerAI implements BattleAI{
	
	@Override
	public Pokemon getNextPokemon(BattleScreen battle) {
		//TODO Add in pokemon select screen
		return null;
	}
	@Override
	public void requestBattleAction(final BattleScreen battle, final int trainer,final int pokemon) {
		new Thread() {
			public void run() {
				MoveMenuDialogBox box = new MoveMenuDialogBox(battle.dialogLoc,battle);
				
				box.moveInit(battle.trainers[trainer], battle.acPokemon[trainer][pokemon]);
				battle.dialog = box;
				box.go();
				                           
				battle.addAction(trainer, pokemon,box.getMove());
			}
		}.start();
	}
	public boolean canRequestBattleAction(BattleScreen battle, int trainer,int pokemon){
		return !battle.dialog.isActive();
	}
}
