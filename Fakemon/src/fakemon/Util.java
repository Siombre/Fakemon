package fakemon;
import java.util.Random;


public class Util {
	static Random rand = new Random();
	public static int rand(int min, int max){
		return rand.nextInt(max-min+1) + min;
	}
}
