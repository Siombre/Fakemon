package fakemon;
import java.util.HashMap;
import java.util.HashSet;


public class Type {
	private static HashSet<Type> types = new HashSet<Type>();
	private final String name;
	private HashMap<Type, Double> effectiveness = new HashMap<Type, Double>();
	Type(String name)
	{
		if (name == null)
			name = "unknown";
		this.name = name;
		types.add(this);
	}
	public Type addSpecial(Type t , double multiplier)
	{
		
		if(effectiveness.containsKey(t))
			effectiveness.remove(t);
		
		effectiveness.put(t, multiplier);
		return this;
	}
	public static Type getByName(String s)
	{
		for(Type t : types)
			if(t.name.equalsIgnoreCase(s))
				return t;
		return null;
	}
	public double getEffectiveness(Type t)
	{
		if(effectiveness.containsKey(t))
			return effectiveness.get(t);
		return 1;
	}
	

}
