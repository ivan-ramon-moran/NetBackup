package Configuracion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Configuracion {
	
	private static Configuracion config = null;
	public boolean bPrimeraVez = true;
	public ArrayList<String> listaDirectoriosMon = new ArrayList<String>();
	private boolean bIniciarConSistema = false;
	private boolean bIniciarMinimizada = false;
	private String directorioEntrante = "/home";
	
	public Configuracion(){
		File fileConfig = new File("NetBackupConfig.cfg");
		
		if (fileConfig.exists()){
			//Si el archivo de configuracion existe, lo leemos.
			leerConfiguracion(fileConfig);
			System.out.println(bPrimeraVez);
		}else{
			//Si no existe lo creamos
			crearArchivo(fileConfig);
		}
	}
	
	public static Configuracion getInstance(){
		if (config == null)
			config = new Configuracion();
			
		return config;
	}
	
	private void leerConfiguracion(File _fileConfig){
		try {
			BufferedReader br = new BufferedReader(new FileReader(_fileConfig));
			String ficheroConfig = "";
			String linea = "";
			
			while ((linea = br.readLine()) != null)
				ficheroConfig += linea + "\n";
			
			br.close();

			String [] aConfig = ficheroConfig.split("\n");
			
			//-------------------------------------DIRECTORIOS A MONITOREAR---------------------
			for (int i = 0; i < aConfig.length; i++){
				if (aConfig[i].equals("<directorios-mon>")){
					while (!aConfig[i + 1].equals("</directorios-mon>") ){
						i++;
						listaDirectoriosMon.add(aConfig[i]);
					}
				}
			}
			
			/*for (int i = 0; i < listaDirectoriosMon.size(); i++)
				System.out.println(listaDirectoriosMon.get(i));*/
			//----------------------------------------------------------------------------------
			//-------------------------------------INICIAR CON EL SISTEMA-----------------------
			for (int i = 0; i < aConfig.length; i++){
				if (aConfig[i].equals("<iniciar-con-sistema>")){
					bIniciarConSistema = Boolean.parseBoolean(aConfig[i + 1]);
				}
			}
			
			System.out.println("Iniciar con el sistema: " + bIniciarConSistema);
			//----------------------------------------------------------------------------------
			//-------------------------------------INICIAR MINIMIZADA---------------------------
			for (int i = 0; i < aConfig.length; i++){
				if (aConfig[i].equals("<iniciar-minimizada>")){
					bIniciarMinimizada = Boolean.parseBoolean(aConfig[i + 1]);
				}
			}
			
			System.out.println("Iniciar minimizada: " + bIniciarMinimizada);
			//----------------------------------------------------------------------------------
			//-------------------------------------DIRECTORIO ENTRANTE--------------------------
			for (int i = 0; i < aConfig.length; i++){
				if (aConfig[i].equals("<directorio-entrante>")){
					directorioEntrante = aConfig[i + 1];
				}
			}
			
			System.out.println("Directorio entrante " + directorioEntrante);
			//----------------------------------------------------------------------------------
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void crearArchivo(File _fileConfig){
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(_fileConfig));			
			pw.println("true");
			pw.println("me llamo Ivan");
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void guardarConfiguracion(){
		
	}
	
	public ArrayList<String> getDirectoriosMon(){
		return this.listaDirectoriosMon;
	}
	
	public boolean getIniciarConSistema(){
		return this.bIniciarConSistema;
	}
	
	public boolean getIniciarMinimizada(){
		return this.bIniciarMinimizada;
	}
	
	public String getDirectorioEntrante(){
		return this.directorioEntrante;
	}
	
}
