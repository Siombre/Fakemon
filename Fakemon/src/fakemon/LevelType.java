package fakemon;
import java.util.HashSet;


abstract class LevelType {
	private static HashSet<LevelType> types = new HashSet<LevelType>();
	private String name;
	public void init(String name){

		types.add(this);

		if (name == null)
			name = this.getClass().getSimpleName();
		this.name = name;
	}
	public static LevelType getByName(String name)
	{
		for(LevelType l : types)
			if(l.name.equalsIgnoreCase(name))
				return l;
		System.err.println("Leveling Type \""+ name+"\" not found.");
		return null;
	}
	public abstract int getExperience(int level);

}
class Fast extends LevelType{
	public Fast() {
		init("Fast");
	}
	public int getExperience(int level) {
		return (4*level*level*level)/5;
	}
}
class MediumFast extends LevelType{
	public MediumFast() {
		init("Medium Fast");
	}
	public int getExperience(int level) {
		return (level*level*level);
	}
}
class MediumSlow extends LevelType{
	public MediumSlow() {
		init("Medium Slow");
	}
	public int getExperience(int n) {
		return (6/5*n*n*n-15*n*n+100*n-140);
	}
}