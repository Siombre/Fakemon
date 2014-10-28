package fakemon;
import java.util.Random;


public class Util {
	private static Random rand = new Random();
	public static int rand(int min, int max){
		return rand.nextInt(max-min+1) + min;
	}
	public static boolean flip(float odds){
		return rand.nextFloat() < odds;
	}
}
