package fakemon;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class RenderManager {
	private static int boundText;
	private static ArrayList<double[]> objects = new ArrayList<double[]>();
	//x,y,x2,y2,s,t,s2,t2,z,id
	private static int ID = 9;
	private RenderManager(){};
	private static int renderNum = 0;
	private static boolean debug = false;
	public static void render(){
		long time = System.currentTimeMillis();

		boundText = -1;
		sort();
		renderNum++;
		if(debug && renderNum%100 == 0)
			System.out.println("Sorted in " + (-time + (time = System.currentTimeMillis()))/1000f + "s");
		
		for(double[] data : objects)
		{
			renderPolygon(data);
		}
		if(debug && renderNum%100 == 0)
			System.out.println("Drawn in " + (-time + (time = System.currentTimeMillis()))/1000f + "s");

		objects.clear();
	}
	private static void sort(){
		quickSort(0,objects.size()-1);
	}
	private static int partition(int start, int end){
		int pI = (start + end) / 2;
		//int pI = end;
		double[] pV = objects.get(pI);
		int r = end;
		int l = start;
		double[] temp;
		while(l <= r){
			while(furtherThan(objects.get(l),pV))
				l++;
			while(nearerThan(objects.get(r),pV))
				r--;
			if(l <= r)
			{
				temp = objects.get(l);
				objects.set(l, objects.get(r));
				objects.set(r, temp);
				l++;
				r--;
			}
		}
		return l;
	}
	private static void quickSort(int start, int end)
	{
		int pI = partition(start,end);
		if(start < pI-1)
			quickSort(start,pI-1);
		if(pI < end)
			quickSort(pI,end);
	}
	private static void renderPolygon(double[] data){
		if((int)data[ID] != boundText)
		{
			Color.white.bind();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)data[ID]);
			boundText = (int) data[ID];
		}
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(data[4],data[5]);
		GL11.glVertex2d(data[0],data[1]);
		GL11.glTexCoord2d(data[6],data[5]);
		GL11.glVertex2d(data[2],data[1]);
		GL11.glTexCoord2d(data[6],data[7]);
		GL11.glVertex2d(data[2],data[3]);
		GL11.glTexCoord2d(data[4],data[7]);
		GL11.glVertex2d(data[0],data[3]);
		GL11.glEnd();
	}
	public static void register(double[] object){
		objects.add(object);
	}
	public static boolean furtherThan(double[] d1, double[] d2){
		if(d1[8] - d2[8] < -.0001) // If z1 < z2
			return true;
		if(d1[8] - d2[8] > .0001)  // If z1 > z2
			return false;
		if(d1[3] - d2[3] < -.0001) // If y21 
			return true;
		if(d1[3] - d2[3] > .0001)
			return false;
		if(d1[0] - d2[0] < -.0001)
			return false;
		if(d1[0] - d2[0] > .0001)
			return true;
		return false;
	}
	public static boolean nearerThan(double[] d1, double[] d2){
		if(d1[8] - d2[8] < -.0001)
			return false;
		if(d1[8] - d2[8] > .0001)
			return true;
		if(d1[3] - d2[3] < -.0001)
			return false;
		if(d1[3] - d2[3] > .0001)
			return true;
		if(d1[0] - d2[0] < -.0001)
			return true;
		if(d1[0] - d2[0] > .0001)
			return false;
		return false;
	}
	public static boolean furtherOrEqual(double[] d1, double[] d2)
	{
		if(d1[8] - d2[8] < -.0001) // If z1 < z2
			return false;
		if(d1[8] - d2[8] > .0001)  // If z1 > z2
			return true;
		if(d1[3] - d2[3] < -.0001)
			return false;
		if(d1[3] - d2[3] > .0001)
			return true;
		if(d1[0] - d2[0] < -.0001)
			return true;
		if(d1[0] - d2[0] > .0001)
			return false;
		return true;
	}
}
