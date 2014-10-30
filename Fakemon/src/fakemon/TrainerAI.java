package fakemon;
import java.util.ArrayList;


public class TrainerAI implements BattleAI{
	Trainer trainer;
	public TrainerAI(Trainer t){
		trainer = t;
	}
	public BattleAction getAction(BattleScreen battle, int trainer, int pokemon){
		Pokemon p = battle.acPokemon[trainer][pokemon];
		Pokemon t = battle.acPokemon[1-trainer][0];
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Move[] moves = p.getMoves();
		for(Move m: moves)
			if(m != null && m.isValid())
				possibleMoves.add(m);
		if(possibleMoves.size() == 0)
			return null;
		
		BattleAction ba = new MoveBattleAction(possibleMoves.get(Util.rand(0, possibleMoves.size()-1)),p,t);
		
		return ba;
	}

	@Override
	public Pokemon getNextPokemon(BattleScreen b) {
		for(Pokemon p : trainer.getPokemon()){
			boolean found = false;
			if(p == null) continue;
			for(Pokemon[] t: b.acPokemon)
				for(Pokemon p2 : t)
				{
					if(p == p2)
						found = true;
				}
			if(!found)
				return p;
		}
		return null;
	}
}
