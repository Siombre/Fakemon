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
	public static void render(){
		long time = System.currentTimeMillis();

		boundText = -1;
		sort();
		GL11.glPushMatrix();
		//	GL11.glScaled(1,-1,1);
		renderNum++;
		if(renderNum%100 == 0)
			System.out.println("Sorted in " + (-time + (time = System.currentTimeMillis()))/1000f + "s");
		for(double[] data : objects)
		{
			renderPolygon(data);
		}
		if(renderNum%100 == 0)
			System.out.println("Drawn in " + (-time + (time = System.currentTimeMillis()))/1000f + "s");

		GL11.glPopMatrix();
		objects.clear();

	}
	private static void sort(){
		//Lazy sort for now
		for(int i = 0;i < objects.size()-1;i++)
		{
			int farIndex = i;
			for(int i2 = i+1; i2< objects.size();i2++)
				if(fartherThan(objects.get(i2),objects.get(farIndex)))
					farIndex = i2;

			if(farIndex != i)
			{
				double[] temp = objects.get(farIndex);
				objects.set(farIndex, objects.get(i));
				objects.set(i, temp);
			}
		}
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
	public static boolean fartherThan(double[] d1, double[] d2){
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
}
