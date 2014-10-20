package fakemon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Start {

	public static void main(String[] args) {
		try {
			init();
			PokemonInfo[] pokedex = PokemonInfo.getList();
			System.out.println(pokedex.length + " Pokemon loaded.");
			MoveInfo[] moves = MoveInfo.getList();
			System.out.println(moves.length + " Moves loaded.");

			final Trainer enemy = new Trainer("Shut up, Fool");
			enemy.addPokemon(generatePokemon());

			final Trainer you = new Trainer("Sully");
			you.addPokemon(generatePokemon());
			you.battleAI = new PlayerAI();
			BattleScreen.initGL();
			Trainer[] t = { you, enemy };
			int[] is = { 1 , 1 };

			final BattleScreen screen = new BattleScreen(t, false, is);

			new Thread() {
				public void run() {
					screen.battleLogic();
				}
			}.start();
			while (!Display.isCloseRequested()) {
				screen.render();
				while(Mouse.next()) screen.mouseEvent();
				Display.update();
				Display.sync(60);
			}
			Display.destroy();
			System.exit(0);

		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

	}
	public static Pokemon generatePokemon(){
		Random rand = new Random();

		PokemonInfo[] pokedex = PokemonInfo.getList();
		MoveInfo[] moves = MoveInfo.getList();
		PokemonInfo s = pokedex[rand.nextInt(pokedex.length)];
		Pokemon yours = new Pokemon(s.name, s, s.levelingType.getExperience(10), 10, false, -1);
		yours.addMove(new Move(moves[rand.nextInt(moves.length)]));
		yours.addMove(new Move(moves[rand.nextInt(moves.length)]));

		return yours;
	}
	public static void init(){
		String base;
		try {
			String os = System.getProperty("os.name").toLowerCase();
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
			base = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
			System.setProperty("org.lwjgl.librarypath", base + "/libs/lwjgl-2.8.4/native/"+osName);
			new Fast();
			new MediumFast();
			new MediumSlow();

			double[] mods = {1,1,1,1,1,1}; 
			new Nature("Default",mods);
			
			loadTypes(base + "/res/Types.csv");	
			loadPokemon(base + "/res/Pokemon/Test Dex.csv");
			loadMoves(base + "/res/Moves");
		
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
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
				System.out.println(s);
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
	}

	public static void loadPokemon(String path){
		try {
			Scanner scan = new Scanner(new FileReader(path));
			scan.nextLine();
			scan.nextLine();

			String line;
			while(scan.hasNextLine())
			{
				line = scan.nextLine();
				if(line.startsWith("#")) continue;

				if(line.replace(',', ' ').trim().isEmpty())
					continue;
				//System.out.println(line);
				new PokemonInfo(line);
			}

			scan.close();

		} catch (FileNotFoundException e) {
			System.err.println("Type file not found");
		}
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
				// file:/c:/myclasses/
				

				// Create a new class loader with the directory
				ClassLoader cl = URLClassLoader.newInstance(new URL[]{url});

				for(File f : moves)
				{
					try {

						MoveInfo m = (MoveInfo) cl.loadClass(f.getName().replace(".class", "")).newInstance();
						System.out.println("Move \"" + m.getName() + "\" loaded");
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}         
	}
}
