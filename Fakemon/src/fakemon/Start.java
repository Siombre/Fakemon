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
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Start {

	public static void main(String[] args) {
		init();


		final Fakemon game = new Fakemon();
		try {

			game.init();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new Thread() {
			public void run() {
				game.start();
			}
		}.start();

		while (!Display.isCloseRequested()) {
			game.render();
			while(Mouse.next()) game.mouseEvent();
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);

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
			new Fluctuating();
			new Erratic();
			double[] mods = {1,1,1,1,1,1}; 
			new Nature("Default",mods);

			loadTypes(base + "/res/Types.csv");	
			loadPokemon(base + "/res/Pokemon");
			//loadPokemon(base + "/res/Pokemon/Test Dex.csv");
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

/*	public static void loadPokemon(String path){
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
				try{
					new PokemonInfo(line);
				}catch(Exception e){
					System.err.println(e.getLocalizedMessage());
				}
			}

			scan.close();

		} catch (FileNotFoundException e) {
			System.err.println("Type file not found");
		}
	}*/
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
		//Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		for(File f : files)
		{
			System.out.println(f.getName());
			StringBuilder contents = new StringBuilder();
			try {
				Scanner s = new Scanner(new FileReader(f));
				while(s.hasNextLine())
					contents.append(s.nextLine());
				
				new PokemonInfo(parser.parse(contents.toString()).getAsJsonObject());
				s.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
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
