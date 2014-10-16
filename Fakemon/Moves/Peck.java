
import fakemon.MoveInfo;
import fakemon.Type;


public class Peck extends MoveInfo{
	public Peck()
	{
		init("Peck", 35, 56, 100, 35, true, Category.PHYSICAL, Type.getByName("flying"));
	}
}
