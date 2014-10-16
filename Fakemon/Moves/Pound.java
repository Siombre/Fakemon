
import fakemon.MoveInfo;
import fakemon.Type;


public class Pound extends MoveInfo{
	public Pound() {
		init("Pound", 35, 56, 100, 40, true, Category.PHYSICAL,Type.getByName("normal"));
	}
}
