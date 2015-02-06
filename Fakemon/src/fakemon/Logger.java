package fakemon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private static FileWriter f;
	private static boolean enabled = true;
	public static void log(String s){
		if(!enabled) return;
		try {
			if(f == null)
			{
				openLog();
			}
				f.append(s);
				f.append("\n");
				System.out.println(s);
				f.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void openLog() {
		
		new File(Start.getPath("log")).mkdir();
		File file = new File(Start.getPath("log"+"/"+System.currentTimeMillis()+".log"));
		try {
			file.createNewFile();
			f = new FileWriter(file);
		} catch (IOException e) {
			System.out.println(file.getAbsolutePath());
			enabled = false;
			e.printStackTrace();
		}
	}
	
}
