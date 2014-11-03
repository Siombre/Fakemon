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
class Slow extends LevelType{
	public Slow() {
		init("Slow");
	}
	public int getExperience(int n) {
		return (n*n*n*5)/4;
	}
}
class Erratic extends LevelType{
	public Erratic() {
		init("Erratic");
	}
	public int getExperience(int n) {
		if(n<=50){
			return (int) ((n*n*n*(100f-n))/50);
		} else if(n<=68){
			return (int) ((n*n*n*(150f-n))/100);
		} else if(n<=98){
			return (int) ((n*n*n*(1911f-10*n)/3)/500);
		} else {
			return (int) ((n*n*n*(160f-n))/100);
		}
		
	}
}
class Fluctuating extends LevelType{
	public Fluctuating() {
		init("Fluctuating");
	}
	public int getExperience(int n) {
		if(n<=15){
			return (int) (n*n*n*(((n+1)/3f+24)/50));
		} else if(n<=36){
			return (int) (n*n*n*((n+14f)/50));
		} else {
			return (int) (n*n*n*((n/2f+32)/50));
		}
		
	}
}