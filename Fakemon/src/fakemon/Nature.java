package fakemon;
import java.util.HashSet;


public class Nature {
	private static HashSet <Nature> natures = new HashSet <Nature>();
	private String name;
	private double[] mods;
	public Nature(String name, double[] mods)
	{
		if(name == null) name = "Unknown";
		this.name = name;
		this.mods = mods;
		natures.add(this);
	}
	public double getMod(int statNum)
	{
		return mods[statNum];
	}
	public String getName()
	{
		return name;
	}
	public static Nature getByName(String name)
	{
		for(Nature n : natures)
			if(n.name.equalsIgnoreCase(name))
				return n;
		return null;
	}
	public static Nature[] getList()
	{
		Nature[] t = new Nature[natures.size()]; 
		return natures.toArray(t);			
	}
}
