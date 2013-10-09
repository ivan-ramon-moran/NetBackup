import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Configuracion {
	
	private static Configuracion config = null;
	public boolean bPrimeraVez = true;
	
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
			this.bPrimeraVez = Boolean.parseBoolean(br.readLine());
			br.close();
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
	
	
}
