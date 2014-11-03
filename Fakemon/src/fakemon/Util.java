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
	public static String possessive(String name){
		if(name.endsWith("s"))
			return name + '\'';
		return name + "\'s";
	}
	public static class Pair<T,U>{
		private T first;
		private U second;
		public Pair(T first, U second){
			this.first = first;
			this.second = second;
		}
		
		public T getFirst(){
			return first;
		}
		public U getSecond(){
			return second;
		}
	}
	public static <T,U> Pair<T,U> o(T o1, U o2){
		return new Pair<T,U>(o1,o2);
	}
}
