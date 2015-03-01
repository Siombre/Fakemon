package fakemon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Start {
	private static long lastFrame;
	private static String basePath;
	public static void main(String[] args) {
		init();


		final Fakemon game = new Fakemon();
		Thread logic = new Thread() {
			public void run() {
				game.start();
			}
		};
		logic.start();
		while (!Display.isCreated());
		while (!Display.isCloseRequested()) {

			game.render(getDelta());
			Display.update();
			while(Mouse.next()) game.mouseEvent();
			Display.sync(60);
		}
		game.requestStop();
		while(logic.isAlive());
		Display.destroy();
		System.exit(0);

	}
	public static String getPath(String s){
		if(!s.startsWith("/"))
			s = "/" +s;
		return basePath + s;
	}
	public static void init(){
		try {
			basePath = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
			String os = System.getProperty("os.name").toLowerCase();
			Logger.log("System : " + os);

			String osName = "";
			if(os.startsWith("linux")){
				osName = "linux";
			} else if(os.startsWith("windows")){
				osName = "windows";
			}else if(os.startsWith("mac")|| os.startsWith("darwin")){
				osName = "macosx";
			} else if (os.startsWith("sunos")) {
				osName = "solaris";
			}

			basePath = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();

			System.setProperty("org.lwjgl.librarypath", getPath("/libs/lwjgl-2.8.4/native/"+osName));

			//Init leveling types
			new Fast();
			new MediumFast();
			new MediumSlow();
			new Slow();
			new Fluctuating();
			new Erratic();

			loadTypes(getPath("/res/Types.csv"));	
			loadMoves(getPath("/res/Moves"));
			loadPokemon(getPath("/res/Pokemon"));
			loadNatures(getPath("/res/natures.json"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}


	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public static void loadTypes(String path){
		try {
			Scanner scan = new Scanner(new FileReader(path));
			scan.nextLine();
			scan.nextLine();


			ArrayList<String> types = new ArrayList<String>();

			Scanner lineScan = new Scanner(scan.nextLine());
			lineScan.useDelimiter(",");

			while(lineScan.hasNext())
				types.add(lineScan.next());
			lineScan.close();
			for(String s: types)
			{
				new Type(s);
			}

			while(scan.hasNextLine()){
				String line = scan.nextLine();
				if(line.matches("^[,\\s]*$")) continue;

				lineScan = new Scanner(line);
				lineScan.useDelimiter(",");

				String name = lineScan.next();
				Type t = Type.getByName(name);

				if(t == null) System.err.println("Error on " + name);

				int count = 0;

				while(lineScan.hasNext())
				{
					t.addSpecial(Type.getByName(types.get(count)), Double.parseDouble(lineScan.next()));
					count++;
				}
			}
			scan.close();

		} catch (FileNotFoundException e) {
			System.err.println("Type file not found");
		}
		if(Type.getList().length == 0)
		{
			System.err.println("No types found");
		} else {
			System.out.println(Type.getList().length + " types loaded.");
		}
	}
	private static void loadNatures(String path) {
		JsonParser parser = new JsonParser();
		JsonObject file = parser.parse(loadFile(new File(path))).getAsJsonObject();
		JsonArray list = file.get("natures").getAsJsonArray();
		for(JsonElement a : list){
			JsonObject def = a.getAsJsonObject();
			String name = def.get("name").getAsString();
			JsonArray jmods = def.get("mods").getAsJsonArray();
			double[] mods = new double[6];
			for(int i = 0;i<6;i++)
			{
				mods[i] = jmods.get(i).getAsDouble();
			}
			new Nature(name, mods);
		}
		if(Nature.getList().length == 0)
		{
			System.err.println("No natures found");
		} else {
			System.out.println(Nature.getList().length + " natures loaded.");
		}
	}
	public static void loadPokemon(String path){
		Pattern p = Pattern.compile(".*\\.json");

		ArrayList<File> files = new ArrayList<File>();
		ArrayList<File> dirs = new ArrayList<File>();
		dirs.add(new File(path));

		while(dirs.size()>0){
			File dir = dirs.get(0);
			dirs.remove(0);
			String[] possible = dir.list();
			for(String s : possible){
				File f = new File(dir.getAbsolutePath()+ "/" +s);
				if(f.isDirectory()){
					dirs.add(f); 
				}else if(p.matcher(s).matches())
				{
					files.add(f);

				}
			}
		}
		JsonParser parser = new JsonParser();
		for(File f : files)
		{
			new PokemonInfo(parser.parse(loadFile(f)).getAsJsonObject(),f.getParentFile());
		}
		if(PokemonInfo.getList().length == 0)
		{
			System.err.println("No Pokemon found");
		} else {
			System.out.println(PokemonInfo.getList().length + " Pokemon loaded.");
		}
	}
	public static String loadFile(File f){
		StringBuilder contents = new StringBuilder();
		Scanner s;
		try {
			s = new Scanner(new FileReader(f));
			while(s.hasNextLine())
				contents.append(s.nextLine());
			s.close();
		} catch (FileNotFoundException e) {}

		return contents.toString();

	}
	public static void loadMoves(String dirPath)
	{
		File dir = new File(dirPath);
		FilenameFilter filter = new FilenameFilter(){
			Pattern p = Pattern.compile(".*\\.class");
			@Override
			public boolean accept(File arg0, String arg1) {
				return p.matcher(arg1).matches();
			}};
		File[] moves = dir.listFiles(filter);
		URL url;
		try {
			url = new URL("file:" + dir.getAbsolutePath()+"/");


			// Create a new class loader with the directory
			ClassLoader cl = URLClassLoader.newInstance(new URL[]{url});

			for(File f : moves)
			{
				try {

					cl.loadClass(f.getName().replace(".class", "")).newInstance();
				} catch (ClassNotFoundException e) {
					System.err.println("Loading move " + f.getName() + " failed.");
					e.printStackTrace();
				} catch (InstantiationException e) {
					System.err.println("Initializing move " + f.getName() + " failed.");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		if(MoveInfo.getList().length == 0)
		{
			System.err.println("No moves found");
		} else {
			System.out.println(MoveInfo.getList().length + " moves loaded.");
		}
	}
	
}
