package core;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Vector;

public class FileStreamController {

	private static final String rootDir = "C:\\FGene\\"; 
	
	public void start(){
		BufferedReader bf;
		try {
			if(FGene.getAllEquipes().isEmpty()){
				bf = new BufferedReader(new FileReader(rootDir+"start.txt"));
				String line;
				while((line = bf.readLine()) != null){
					String[] div = line.split(",");
					Piloto p1 = new Piloto(div[1],Integer.parseInt(div[2]));
					Piloto p2 = new Piloto(div[4],Integer.parseInt(div[5]));
					Equipe e = new Equipe(div[0],p1,Integer.parseInt(div[3]),p2,Integer.parseInt(div[6]));
					
					FGene.getAllPilots().add(p1);
					FGene.getAllPilots().add(p2);
					FGene.getAllEquipes().add(e);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save(){
		try{
	         FileOutputStream pilotsOut = new FileOutputStream(rootDir+"saves\\pilots.gene");
	         FileOutputStream equipesOut = new FileOutputStream(rootDir+"saves\\equipes.gene");
	         FileOutputStream seasonsOut = new FileOutputStream(rootDir+"saves\\seasons.gene");
	         
	         ObjectOutputStream out = new ObjectOutputStream(equipesOut);
	         out.writeObject(FGene.getAllEquipes());
	         out.close();
	         
	         ArrayList<Piloto> ps = new ArrayList<>();
	         for(Piloto p : FGene.getAllPilots()){
	        	 if(p.careerLeft == 0){
	        		 ps.add(p);
	        	 }
	         }
	         
	         out = new ObjectOutputStream(pilotsOut);
	         out.writeObject(ps);
	         out.close();
	         
	         out = new ObjectOutputStream(seasonsOut);
	         out.writeObject(FGene.getAllSeasons());
	         out.close();
	         
	         pilotsOut.close();
	         equipesOut.close();
	         seasonsOut.close();
	         
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public boolean load(){
	      try{
	         FileInputStream pilotsIn = new FileInputStream(rootDir+"saves\\pilots.gene");
	         FileInputStream equipesIn = new FileInputStream(rootDir+"saves\\equipes.gene");
	         FileInputStream seasonsIn = new FileInputStream(rootDir+"saves\\seasons.gene");
	         
	         ObjectInputStream in = new ObjectInputStream(equipesIn);
	         FGene.setAllEquipes((Vector<Equipe>) in.readObject());
	         in.close();
	         
	         for(Equipe e : FGene.getAllEquipes()){
	        	 FGene.getAllPilots().add(e.piloto1);
	        	 FGene.getAllPilots().add(e.piloto2);
	         }
	         
	         in = new ObjectInputStream(pilotsIn);
	         FGene.getAllPilots().addAll((ArrayList<Piloto>) in.readObject());
	         in.close();
	         
	         in = new ObjectInputStream(seasonsIn);
	         FGene.setAllSeasons((ArrayList<Season>) in.readObject());
	         in.close();
	         
	         
	         pilotsIn.close();
	         equipesIn.close();
	         seasonsIn.close();
	         
	         return true;
	         
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return false;
	      }catch(ClassNotFoundException c)
	      {
	         c.printStackTrace();
	         return false;
	      }
	}
	
	public ArrayList<Piloto> readDriverFiles() {
		try {
			File[] files = new File(rootDir+"Drivers\\").listFiles();
			ArrayList<Piloto> ps = new ArrayList<>();
			for(File f : files){
				Piloto p = new Piloto();
				p.name = f.getName().replace(".drv", "");
				//Piloto old = FGene.getPiloto(p.name);
				
				//FileInputStream in = new FileInputStream(f);
				//DataInputStream inData = new DataInputStream(in);
				RandomAccessFile raf = new RandomAccessFile(f, "r");
				
				//inData.skipBytes(40);
				raf.seek(40);
				p.totals.p1st = Integer.reverseBytes(raf.readInt());
				p.totals.p2nd = Integer.reverseBytes(raf.readInt());
				p.totals.p3rd = Integer.reverseBytes(raf.readInt());
				p.totals.p4th = Integer.reverseBytes(raf.readInt());
				p.totals.p5th = Integer.reverseBytes(raf.readInt());
				p.totals.p6th = Integer.reverseBytes(raf.readInt());
				raf.seek(84);
				p.AI = Integer.reverseBytes(raf.readInt());
				
				//inData.close();
				//in.close();
				raf.close();
				
				p.totals.updatePts();
				ps.add(p);
			}
			return ps;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("erro no readDrivers");
		return null;
	}
	
	public void updateDriverAI(Piloto p) {
		try {
			//new File(System.getProperty("user.dir")+"\\DriversSub\\").mkdir();
			File file = new File(rootDir+"Drivers\\"+p.name+".drv");
			//FileOutputStream out = new FileOutputStream(System.getProperty("user.dir")+"\\DriversSub\\"+p.name+".drv");
			//FileInputStream in = new FileInputStream(file);
			//byte[] b = new byte[100];
			
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			
			raf.seek(84);
			raf.writeInt(Integer.reverseBytes(p.AI));
			raf.close();
			
//			in.read(b);
//			b[84] = p.AI.byteValue();
//			b[84] = i.byteValue();
//			out.write(b);
//			
//			in.close();
//			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("erro no updateDriversAI");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("erro no updateDriversAI");
		}
	}

}
